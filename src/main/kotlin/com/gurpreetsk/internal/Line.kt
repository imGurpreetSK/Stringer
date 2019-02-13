package com.gurpreetsk.internal

sealed class Line

class Comment(val text: String) : Line()

class Resource(val key: ResourceKey, val value: ResourceValue, val type: ResourceType? = null) : Line()
