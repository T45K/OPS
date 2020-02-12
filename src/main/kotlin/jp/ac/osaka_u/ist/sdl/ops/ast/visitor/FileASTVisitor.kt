package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import jp.ac.osaka_u.ist.sdl.ops.ast.FileAST
import jp.ac.osaka_u.ist.sdl.ops.entity.Method

class FileASTVisitor(private val fileAST: FileAST) : VoidVisitorAdapter<Void?>() {
    val methods: MutableList<Method> = mutableListOf()

    override fun visit(node: MethodDeclaration, arg: Void?) {
        if (node.body.isEmpty || node.body.get().isEmpty) {
            return
        }

        val method = Method(fileAST.path, node.name.identifier, false, node.parameters.size)
        if (node.parameters.size <= 1) {
            methods.add(method)
            return
        }

        val methodVisitor = MethodVisitor(node.parameters)
        node.body.get().accept(methodVisitor, null)
        method.order = methodVisitor.orders
                .map { it.value }
                .sortedBy { it.declared }
                .map { it.referenced }
        methods.add(method)
    }
}

fun takeOutMethods(fileAST: FileAST): List<Method> {
    val visitor = FileASTVisitor(fileAST)
    fileAST.ast.accept(visitor, null)
    return visitor.methods
}
