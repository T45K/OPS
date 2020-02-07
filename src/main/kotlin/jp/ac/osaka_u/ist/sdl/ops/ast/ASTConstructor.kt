package jp.ac.osaka_u.ist.sdl.ops.ast

import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.jdt.core.dom.FileASTRequestor
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList


fun constructFileASTs(projectPath: Path): List<FileAST> {
    val parser: ASTParser = createParser()
    val fileASTs: MutableList<FileAST> = mutableListOf()
    val requestor: FileASTRequestor = object : FileASTRequestor() {
        override fun acceptAST(sourceFilePath: String, ast: CompilationUnit) {
            fileASTs.add(FileAST(Paths.get(sourceFilePath), ast))
        }
    }
    val javaFiles: Array<String> = getTargetFiles(projectPath)

    // TODO コメント周りのバグ，何らかの設定方法があるはず
    try {
        parser.createASTs(javaFiles, null, arrayOf(), requestor, NullProgressMonitor())
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return fileASTs
}

private fun createParser(): ASTParser {
    val parser: ASTParser = ASTParser.newParser(AST.JLS13)
    parser.setKind(ASTParser.K_COMPILATION_UNIT)

    @Suppress("UNCHECKED_CAST")
    val options: MutableMap<String, String> = DefaultCodeFormatterConstants.getEclipseDefaultSettings() as MutableMap<String, String>
    options[JavaCore.COMPILER_COMPLIANCE] = JavaCore.VERSION_13
    options[JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM] = JavaCore.VERSION_13
    options[JavaCore.COMPILER_SOURCE] = JavaCore.VERSION_13

    parser.setCompilerOptions(options)
    return parser
}

private fun getTargetFiles(projectPath: Path): Array<String> {
    return Files.walk(projectPath)
            .map { it.toString() }
            .filter { it.endsWith(".java") }
            .toList()
            .toTypedArray()
}
