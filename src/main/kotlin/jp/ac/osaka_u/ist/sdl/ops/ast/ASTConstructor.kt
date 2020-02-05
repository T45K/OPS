package jp.ac.osaka_u.ist.sdl.ops.ast

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.FileASTRequestor
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.HashSet
import kotlin.streams.toList

fun constructFileASTs(projectPath: Path): List<FileAST> {
    val parser: ASTParser = ASTParser.newParser(AST.JLS13)
    val fileASTs: MutableList<FileAST> = mutableListOf()
    val requestor: FileASTRequestor = object : FileASTRequestor() {
        override fun acceptAST(sourceFilePath: String, ast: CompilationUnit) {
            fileASTs.add(FileAST(Paths.get(sourceFilePath), ast))
        }
    }
    val javaFiles: Array<String> = getTargetFiles(projectPath)
    parser.createASTs(javaFiles, null, arrayOf(), requestor, NullProgressMonitor())
    return fileASTs
}

private fun getTargetFiles(projectPath: Path): Array<String> {
    return Files.walk(projectPath)
            .map { it.toString() }
            .filter { it.endsWith(".java") }
            .toList()
            .toTypedArray()
}
