package jp.ac.osaka_u.ist.sdl.ops.ast

import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class ASTConstructorTest {
    companion object {
        const val ROOT_PATH: String = "src/test/sample/ast"
    }

    @Test
    fun testConstructFileAST() {
        val fileASTs: List<FileAST> = constructFileASTs(Paths.get(ROOT_PATH))
        assertEquals(1, fileASTs.size)
        assertEquals(Paths.get(ROOT_PATH, "Sample.java"), fileASTs[0].path)
    }
}
