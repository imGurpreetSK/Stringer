package com.gurpreetsk.internal

sealed class Line

data class Comment(val text: String) : Line()

data class Resource(val key: ResourceKey, val value: ResourceValue) : Line()
