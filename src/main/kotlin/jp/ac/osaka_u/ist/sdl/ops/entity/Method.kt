package jp.ac.osaka_u.ist.sdl.ops.entity

import java.nio.file.Path

data class Method(val path: Path, val name: String, val isConstructor: Boolean, val numOfParams: Int) {
    lateinit var order: List<Int>
}
