package com.roadtripbingo.app.model

data class Prompt(
    val id: String,
    val name: String,
    val description: String = "",
    val category: String = "general",
    val tags: List<String> = emptyList(),
    val isUniversal: Boolean = true,
    val region: Region = Region.UNIVERSAL,
    val iconName: String = "road"
)
