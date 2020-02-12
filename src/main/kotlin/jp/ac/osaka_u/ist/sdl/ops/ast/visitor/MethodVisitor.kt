package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.Parameter
import com.github.javaparser.ast.expr.SimpleName
import com.github.javaparser.ast.visitor.VoidVisitorAdapter

class MethodVisitor(parameterNames: NodeList<Parameter>) : VoidVisitorAdapter<Void>() {
    private var index = 0
    val orders: Map<String, Locations>

    init {
        orders = parameterNames
                .mapIndexed { i, param -> param.name.identifier to Locations(i, -1) }
                .toMap()
    }

    override fun visit(node: SimpleName, arg: Void) {
        val locations: Locations = orders[node.identifier] ?: return
        if (locations.referenced == -1) {
            locations.referenced = index++
        }
    }
}

data class Locations(val declared: Int, var referenced: Int)
