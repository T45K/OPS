package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import jp.ac.osaka_u.ist.sdl.ops.ast.FileAST
import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import org.eclipse.jdt.core.dom.*

class FileASTVisitor(private val fileAST: FileAST) : ASTVisitor() {
    val methods: MutableList<Method> = mutableListOf()

    override fun visit(node: MethodDeclaration): Boolean {
        if (!isHadToCheck(node.parent, node)) {
            return false
        }

        val methodVisitor = MethodVisitor(fileAST.path, methods)
        node.accept(methodVisitor)
        return false
    }

    private fun isHadToCheck(astNode: ASTNode, methodDeclaration: MethodDeclaration): Boolean {
        return astNode is TypeDeclaration && !astNode.isInterface && methodDeclaration.modifiers and Modifier.ABSTRACT != 1
    }
}

fun takeOutMethods(fileAST: FileAST): List<Method> {
    val visitor = FileASTVisitor(fileAST)
    fileAST.ast.accept(visitor)
    return visitor.methods
}
