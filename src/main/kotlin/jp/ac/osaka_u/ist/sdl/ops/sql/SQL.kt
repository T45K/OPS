package jp.ac.osaka_u.ist.sdl.ops.sql

import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import java.lang.RuntimeException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class SQL {
    private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:./db.sqlite3")
            ?: throw RuntimeException("Bad db connection")

    init {
        val statement: Statement = connection.createStatement()
        statement.executeUpdate("create table if not exists method (name string, isConstructor boolean, numOfParams integer, id integer primary key autoincrement)")
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
        val statement: Statement = connection.createStatement()
        statement.executeUpdate("insert into param values($id, $declaredOrder,$referredOrder)")
        statement.close()
    }

    private fun insertMethod(method: Method): Int {
        val statement: Statement = connection.createStatement()
        val name = "${method.path}#${method.name}"
        statement.executeUpdate("insert into method values('$name', ${method.isConstructor}, ${method.numOfParams})")
        statement.close()

        val resultSet: ResultSet = statement.generatedKeys
        return resultSet.getInt("id")
    }

    fun close() {
        connection.close()
    }

}
