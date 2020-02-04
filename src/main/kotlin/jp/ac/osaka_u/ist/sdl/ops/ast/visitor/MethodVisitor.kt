package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SimpleName
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import java.nio.file.Path

class MethodVisitor(private val path: Path, private val methods: MutableList<Method>) : ASTVisitor() {
    private lateinit var orders: Map<String, Locations>
    private var index = 0

    override fun visit(node: MethodDeclaration): Boolean {
        if (node.parameters().size <= 1) {
            methods.add(Method(path, node.name.identifier, node.isConstructor, node.parameters().size))
            return false
        }

        orders = node.parameters()
                .mapIndexed { i, param -> (param as SingleVariableDeclaration).name.identifier to Locations(i, -1) }
                .toMap()

        return false
    }

    override fun visit(node: SimpleName): Boolean {
        val locations: Locations = orders[node.identifier] ?: return false
        if(locations.referenced != -1){
            locations.referenced = index++
        }

        return false
    }
}

data class Locations(val declared: Int, var referenced: Int)
