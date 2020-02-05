package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.SingleVariableDeclaration

class MethodVisitor(parameters: List<SingleVariableDeclaration>) : ASTVisitor() {
    private var index = 0
    val orders: Map<String, Locations>

    init {
        orders = parameters
                .mapIndexed { i, param -> param.name.identifier to Locations(i, -1) }
                .toMap()
    }

    override fun visit(node: SimpleName): Boolean {
        val locations: Locations = orders[node.identifier] ?: return false
        if (locations.referenced == -1) {
            locations.referenced = index++
        }

        return false
    }
}

data class Locations(val declared: Int, var referenced: Int)
