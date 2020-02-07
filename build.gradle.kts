plugins {
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.3.61"

    // Apply the application plugin to add support for building a CLI application.
    application
}

repositories {
    // Use jcenter for resolving dependencies.
    // You can declare any Maven/Ivy/file repository here.
    jcenter()
}

dependencies {
    // Align versions of all Kotlin components
    compile(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use JDK
    compile("org.eclipse.jdt:org.eclipse.jdt.core:3.20.0")

    // Use Rx
    compile("io.reactivex.rxjava2:rxkotlin:2.4.0")

    // Use args4j
    compile("args4j:args4j:2.33")

    // Use SQLite
    compile("org.xerial:sqlite-jdbc:3.30.1")

    // Use the Kotlin JDK 8 standard library.
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    // Define the main class for the application.
    mainClassName = "jp.ac.osaka_u.ist.sdl.ops.AppKt"
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "jp.ac.osaka_u.ist.sdl.ops.AppKt"
    }

    from(
            configurations.compile.get().map {
                if (it.isDirectory) it else zipTree(it)
            }
    )
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
}

val run by tasks.getting(JavaExec::class) {
    if (project.hasProperty("args")) {
        args = (project.property("args") as String).split(" ")
    }
}
