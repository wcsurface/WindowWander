package com.roadtripbingo.app.model

data class Deck(
    val id: String,
    val title: String,
    val description: String,
    val coverIcon: String,
    val audience: String, // "Family", "Teens & Adults", "Couples & Friends", etc.
    val difficulty: String, // "Easy", "Medium", "Hard"
    val isFree: Boolean = true,
    val price: String = "$0.99",
    val region: Region = Region.UNIVERSAL,
    val prompts: List<Prompt>
)
