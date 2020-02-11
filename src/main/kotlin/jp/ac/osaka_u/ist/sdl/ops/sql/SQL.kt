package jp.ac.osaka_u.ist.sdl.ops.sql

import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement

class SQL {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:./db.sqlite3")
            ?: throw RuntimeException("Bad db connection")

    companion object {
        const val paramInsertionQuery: String = "insert into param values(?, ?, ?)"
        const val methodInsertionQuery: String = "insert into method(name, isConstructor, numOfParams) values(?, ?, ?)"
    }

    init {
        val statement: Statement = connection.createStatement()
        statement.executeUpdate("create table if not exists method (name string, isConstructor integer, numOfParams integer, id integer primary key autoincrement)")
        statement.executeUpdate("create table if not exists param (id integer, declaredOrder integer, referredOrder integer)")
    }

    fun insert(method: Method) {
        val id: Int = insertMethod(method)
        if (method.numOfParams <= 1) {
            return
        }

        for ((declaredOrder, referredOrder) in method.order.withIndex()) {
            insertParam(id, declaredOrder, referredOrder)
        }
    }

    private fun insertParam(id: Int, declaredOrder: Int, referredOrder: Int) {
        val statement: PreparedStatement = connection.prepareStatement(paramInsertionQuery)
        statement.setInt(1, id)
        statement.setInt(2, declaredOrder)
        statement.setInt(3, referredOrder)
        statement.executeUpdate()
        statement.close()
    }

    private fun insertMethod(method: Method): Int {
        val statement: PreparedStatement = connection.prepareStatement(methodInsertionQuery)
        val name = "${method.path}#${method.name}"
        val isConstructor: Int = if (method.isConstructor) 1 else 0
        statement.setString(1, name)
        statement.setInt(2, isConstructor)
        statement.setInt(3, method.numOfParams)
        statement.executeUpdate()

        val generatedKey: ResultSet = statement.generatedKeys
        generatedKey.next()
        val id: Int = generatedKey.getInt(1)

        generatedKey.close()
        statement.close()

        return id
    }

    fun close() {
        connection.close()
    }
}
