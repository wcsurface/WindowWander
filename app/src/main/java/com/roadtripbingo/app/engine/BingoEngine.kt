package com.roadtripbingo.app.engine

import com.roadtripbingo.app.data.DeckRepository
import com.roadtripbingo.app.model.BingoTile
import com.roadtripbingo.app.model.Deck
import com.roadtripbingo.app.model.GameMode
import com.roadtripbingo.app.model.Prompt
import com.roadtripbingo.app.model.Region
import kotlin.random.Random

data class WinResult(
    val hasWon: Boolean,
    val winningLinesCount: Int,
    val completedTileIndices: List<Int>,
    val winningLineIndices: List<List<Int>>
)

object BingoEngine {

    // Center tile index for 5x5 grid
    const val CENTER_INDEX = 12

    fun generateBoard(
        deck: Deck = DeckRepository.DECKS[0],
        selectedRegion: Region = Region.UNIVERSAL,
        kidsModeEasy: Boolean = false,
        seed: Long? = null
    ): List<BingoTile> {
        val random = if (seed != null) Random(seed) else Random.Default

        val allPrompts = deck.prompts
        val regionalPrompts = allPrompts.filter { !it.isUniversal || it.region == selectedRegion }
        val universalPrompts = allPrompts.filter { it.isUniversal }

        // Select 24 unique prompts (~60% universal, ~40% regional if available)
        val selectedPrompts = mutableSetOf<Prompt>()

        if (regionalPrompts.size >= 10 && selectedRegion != Region.UNIVERSAL) {
            val regionalCount = 10
            val shuffledRegional = regionalPrompts.shuffled(random)
            selectedPrompts.addAll(shuffledRegional.take(regionalCount))
        }

        val remainingNeeded = 24 - selectedPrompts.size
        val shuffledUniversal = universalPrompts.filter { !selectedPrompts.contains(it) }.shuffled(random)
        selectedPrompts.addAll(shuffledUniversal.take(remainingNeeded))

        // If still under 24, fill with any remaining prompts from deck
        if (selectedPrompts.size < 24) {
            val remainingAll = allPrompts.filter { !selectedPrompts.contains(it) }.shuffled(random)
            selectedPrompts.addAll(remainingAll.take(24 - selectedPrompts.size))
        }

        val promptList = selectedPrompts.toList().shuffled(random)

        val centerPrompt = Prompt(
            id = "free_center",
            name = "FREE SPACE",
            description = "Safe travels! Free tile for your trip.",
            category = "free",
            isUniversal = true,
            iconName = "star"
        )

        val tiles = mutableListOf<BingoTile>()
        var promptIdx = 0

        for (i in 0 until 25) {
            val row = i / 5
            val col = i % 5

            if (i == CENTER_INDEX) {
                tiles.add(
                    BingoTile(
                        index = i,
                        row = row,
                        col = col,
                        prompt = centerPrompt,
                        isCenter = true,
                        isCompleted = true,
                        completedAt = System.currentTimeMillis()
                    )
                )
            } else {
                val prompt = promptList.getOrElse(promptIdx) {
                    Prompt(id = "fallback_$i", name = "Sight #$i", description = "Look out the window!")
                }
                promptIdx++

                tiles.add(
                    BingoTile(
                        index = i,
                        row = row,
                        col = col,
                        prompt = prompt,
                        isCenter = false,
                        isCompleted = false
                    )
                )
            }
        }

        return tiles
    }

    fun evaluateWin(tiles: List<BingoTile>, gameMode: GameMode = GameMode.CLASSIC_LINE): WinResult {
        if (tiles.size != 25) {
            return WinResult(hasWon = false, winningLinesCount = 0, completedTileIndices = emptyList(), winningLineIndices = emptyList())
        }

        val grid = Array(5) { BooleanArray(5) }
        val completedIndices = mutableListOf<Int>()

        tiles.forEach { tile ->
            if (tile.isCompleted) {
                grid[tile.row][tile.col] = true
                completedIndices.add(tile.index)
            }
        }

        val winningLines = mutableListOf<List<Int>>()

        // Rows
        for (r in 0 until 5) {
            if ((0 until 5).all { c -> grid[r][c] }) {
                winningLines.add((0 until 5).map { c -> r * 5 + c })
            }
        }

        // Columns
        for (c in 0 until 5) {
            if ((0 until 5).all { r -> grid[r][c] }) {
                winningLines.add((0 until 5).map { r -> r * 5 + c })
            }
        }

        // Diagonal Top-Left to Bottom-Right (0, 6, 12, 18, 24)
        if ((0 until 5).all { i -> grid[i][i] }) {
            winningLines.add((0 until 5).map { i -> i * 5 + i })
        }

        // Diagonal Top-Right to Bottom-Left (4, 8, 12, 16, 20)
        if ((0 until 5).all { i -> grid[i][4 - i] }) {
            winningLines.add((0 until 5).map { i -> i * 5 + (4 - i) })
        }

        val linesCount = winningLines.size
        val hasBlackout = completedIndices.size == 25

        val hasWon = when (gameMode) {
            GameMode.CLASSIC_LINE, GameMode.RACE_MODE, GameMode.DAILY_CHALLENGE -> linesCount >= 1
            GameMode.TWO_LINES -> linesCount >= 2
            GameMode.BLACKOUT -> hasBlackout
        }

        return WinResult(
            hasWon = hasWon,
            winningLinesCount = linesCount,
            completedTileIndices = completedIndices,
            winningLineIndices = winningLines
        )
    }

    fun toggleTile(tiles: List<BingoTile>, index: Int): List<BingoTile> {
        return tiles.map { tile ->
            if (tile.index == index && !tile.isCenter) {
                tile.copy(
                    isCompleted = !tile.isCompleted,
                    completedAt = if (!tile.isCompleted) System.currentTimeMillis() else null
                )
            } else {
                tile
            }
        }
    }
}
