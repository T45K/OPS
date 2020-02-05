package jp.ac.osaka_u.ist.sdl.ops.ast

import org.eclipse.jdt.core.dom.CompilationUnit
import java.nio.file.Path

data class FileAST(val path: Path, val ast: CompilationUnit)