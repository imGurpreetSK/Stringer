package com.gurpreetsk.internal

fun String.clean(): String = this.trim().toLowerCase().replace(" ", "_")
