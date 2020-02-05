package jp.ac.osaka_u.ist.sdl.ops.ast.visitor

import jp.ac.osaka_u.ist.sdl.ops.ast.FileAST
import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.dom.ASTParser
import org.eclipse.jdt.core.dom.CompilationUnit
import org.junit.Assert.assertEquals
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.test.BeforeTest
import kotlin.test.Test

class FileASTVisitorTest {
    companion object {
        const val PATH: String = "./src/test/sample/ast/Sample.java"
    }

    lateinit var methods: List<Method>

    @BeforeTest
    fun testTakeOutMethods() {
        val fileAST: FileAST = constructFileAST(Paths.get(PATH))
        methods = takeOutMethods(fileAST)
    }

    @Test
    fun testConstructor() {
        val constructor: Method = methods[0]
        assertEquals("Sample", constructor.name)
        assertEquals(true, constructor.isConstructor)
        assertEquals(0, constructor.numOfParams)
    }

    @Test
    fun testMethodWithParamsInOrder() {
        val method1: Method = methods[1]
        assertEquals("method1", method1.name)
        assertEquals(false, method1.isConstructor)
        assertEquals(2, method1.numOfParams)
        assertEquals(listOf(0, 1), method1.order)
    }

    @Test
    fun testMethodWithParamsNotInOrder() {
        val method2: Method = methods[2]
        assertEquals("method2", method2.name)
        assertEquals(false, method2.isConstructor)
        assertEquals(2, method2.numOfParams)
        assertEquals(listOf(1, 0), method2.order)
    }

    private fun constructFileAST(path: Path): FileAST {
        val parser: ASTParser = ASTParser.newParser(AST.JLS13)
        val contents: ByteArray = Files.readAllBytes(path)
        parser.setSource(String(contents).toCharArray())

        val ast: CompilationUnit = parser.createAST(NullProgressMonitor()) as CompilationUnit
        return FileAST(path, ast)
    }
}