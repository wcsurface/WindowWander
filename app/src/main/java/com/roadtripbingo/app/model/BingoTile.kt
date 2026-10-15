package com.roadtripbingo.app.model

data class BingoTile(
    val index: Int, // 0..24
    val row: Int,   // 0..4
    val col: Int,   // 0..4
    val prompt: Prompt,
    val isCenter: Boolean = false,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val claimedBy: String? = null // Player nickname in multiplayer
)
