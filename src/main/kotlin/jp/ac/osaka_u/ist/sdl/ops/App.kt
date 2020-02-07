package jp.ac.osaka_u.ist.sdl.ops

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import jp.ac.osaka_u.ist.sdl.ops.ast.constructFileASTs
import jp.ac.osaka_u.ist.sdl.ops.ast.visitor.FileASTVisitor
import jp.ac.osaka_u.ist.sdl.ops.ast.visitor.takeOutMethods
import jp.ac.osaka_u.ist.sdl.ops.entity.Method
import jp.ac.osaka_u.ist.sdl.ops.sql.SQL
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.kohsuke.args4j.spi.PathOptionHandler
import java.nio.file.Files
import java.nio.file.Path
import kotlin.streams.toList
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val config: Configuration = parseArguments(*args)
    val projectDirs: List<Path> = Files.list(config.inputDir)
            .flatMap { Files.list(it) }
            .toList()

    val methods: List<Method> = Observable.fromIterable(projectDirs)
            .subscribeOn(Schedulers.computation())
            .map { constructFileASTs(it) }
            .flatMap {
                Observable.fromIterable(it.flatMap { fileAST -> takeOutMethods(fileAST) })
            }
            .toList()
            .blockingGet()

    val sql = SQL()
    methods.forEach(sql::insert)
    sql.close()
}

private fun parseArguments(vararg args: String): Configuration {
    val config = Configuration()
    val parser = CmdLineParser(config)

    try {
        parser.parseArgument(*args)
    } catch (e: CmdLineException) {
        parser.printUsage(System.out)
        exitProcess(1)
    }
    return config
}

class Configuration {
    @Option(name = "-i", aliases = ["--input"], usage = "input dir", required = true, handler = PathOptionHandler::class)
    lateinit var inputDir: Path
}
