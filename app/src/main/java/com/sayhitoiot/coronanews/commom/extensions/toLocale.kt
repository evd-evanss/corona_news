package com.sayhitoiot.coronanews.commom.extensions

fun String.toLocale() : String {
    val stringList = this.split("-").toMutableList()
    return "Atualizado em: ${stringList[2]}/${stringList[1]}/${stringList[0]}"
}