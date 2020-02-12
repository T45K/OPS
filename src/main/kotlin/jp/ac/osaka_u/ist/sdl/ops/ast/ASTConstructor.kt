package jp.ac.osaka_u.ist.sdl.ops.ast

import com.github.javaparser.StaticJavaParser.parse
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList

fun constructFileASTs(projectPath: Path): List<FileAST> {
    return Files.walk(projectPath)
            .filter { it.endsWith(".java") }
            .map { FileAST(it, parse(it)) }
            .toList()
}
