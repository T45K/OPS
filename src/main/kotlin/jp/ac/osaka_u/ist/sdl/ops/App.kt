package jp.ac.osaka_u.ist.sdl.ops

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import org.kohsuke.args4j.spi.PathOptionHandler
import java.nio.file.Path
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val config: Configuration = parseArguments(*args)

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
