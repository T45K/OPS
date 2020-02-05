package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import jp.ac.osaka_u.ist.sdl.ops.ast.FileAST
import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import org.eclipse.jdt.core.dom.ASTVisitor
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration

class FileASTVisitor(private val fileAST: FileAST) : ASTVisitor() {
    val methods: MutableList<Method> = mutableListOf()

    override fun visit(node: MethodDeclaration): Boolean {
        if (node.body == null) {
            return false
        }

        val method = Method(fileAST.path, node.name.identifier, node.isConstructor, node.parameters().size)
        if (node.parameters().size <= 1) {
            methods.add(method)
            return false
        }

        @Suppress("UNCHECKED_CAST")
        val methodVisitor = MethodVisitor(node.parameters() as List<SingleVariableDeclaration>)
        node.body.accept(methodVisitor)
        method.order = methodVisitor.orders
                .map { it.value }
                .sortedBy { it.declared }
                .map { it.referenced }
        methods.add(method)

        return false
    }
}

fun takeOutMethods(fileAST: FileAST): List<Method> {
    val visitor = FileASTVisitor(fileAST)
    fileAST.ast.accept(visitor)
    return visitor.methods
}
