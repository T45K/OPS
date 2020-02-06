package jp.ac.osaka_u.ist.sdl.ops.sql

data class Method(val name: String, val isConstructor: Boolean, val numOfParams: Int)

data class Param(val methodName: String, val declaredOrder: Int, val referredOrder: Int)
