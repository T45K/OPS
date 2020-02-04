package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import jp.ac.osaka_u.ist.sdl.ops.ast.FileAST
import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import org.eclipse.jdt.core.dom.*

class FileASTVisitor(private val fileAST: FileAST) : ASTVisitor() {
    val methods: MutableList<Method> = mutableListOf()

    override fun visit(node: MethodDeclaration): Boolean {
        if (!isHadToCheck(node)) {
            return false
        }

        val methodVisitor = MethodVisitor(fileAST.path, methods)
        node.accept(methodVisitor)
        return false
    }

    private fun isHadToCheck(methodDeclaration: MethodDeclaration): Boolean {
        return methodDeclaration.body != null && methodDeclaration.modifiers and Modifier.ABSTRACT == 0
    }
}

fun takeOutMethods(fileAST: FileAST): List<Method> {
    val visitor = FileASTVisitor(fileAST)
    fileAST.ast.accept(visitor)
    return visitor.methods
}
