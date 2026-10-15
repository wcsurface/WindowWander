package com.roadtripbingo.app

import com.roadtripbingo.app.data.DeckRepository
import com.roadtripbingo.app.engine.BingoEngine
import com.roadtripbingo.app.model.GameMode
import com.roadtripbingo.app.model.Region
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BingoEngineTest {

    @Test
    fun testGenerateBoardHas25TilesAndCenterFreeSpace() {
        val board = BingoEngine.generateBoard(
            deck = DeckRepository.DECKS[0],
            selectedRegion = Region.MOUNTAIN_WEST,
            seed = 12345L
        )

        assertEquals(25, board.size)
        val centerTile = board[BingoEngine.CENTER_INDEX]
        assertTrue(centerTile.isCenter)
        assertTrue(centerTile.isCompleted)
        assertEquals("FREE SPACE", centerTile.prompt.name)
    }

    @Test
    fun testHorizontalWinLineDetection() {
        var board = BingoEngine.generateBoard(seed = 100L)

        // Complete top row (indices 0, 1, 2, 3, 4)
        for (i in 0..4) {
            board = BingoEngine.toggleTile(board, i)
        }

        val result = BingoEngine.evaluateWin(board, GameMode.CLASSIC_LINE)
        assertTrue(result.hasWon)
        assertEquals(1, result.winningLinesCount)
    }

    @Test
    fun testDiagonalWinLineDetection() {
        var board = BingoEngine.generateBoard(seed = 200L)

        // Complete main diagonal (0, 6, 12, 18, 24). Note 12 is center (already completed).
        val diagonalIndices = listOf(0, 6, 18, 24)
        for (i in diagonalIndices) {
            board = BingoEngine.toggleTile(board, i)
        }

        val result = BingoEngine.evaluateWin(board, GameMode.CLASSIC_LINE)
        assertTrue(result.hasWon)
        assertEquals(1, result.winningLinesCount)
    }

    @Test
    fun testTwoLinesGameMode() {
        var board = BingoEngine.generateBoard(seed = 300L)

        // Complete row 0 (0..4)
        for (i in 0..4) {
            board = BingoEngine.toggleTile(board, i)
        }

        // With 1 line, TWO_LINES mode should NOT win yet
        var result = BingoEngine.evaluateWin(board, GameMode.TWO_LINES)
        assertFalse(result.hasWon)

        // Complete column 0 (0, 5, 10, 15, 20)
        for (i in listOf(5, 10, 15, 20)) {
            board = BingoEngine.toggleTile(board, i)
        }

        result = BingoEngine.evaluateWin(board, GameMode.TWO_LINES)
        assertTrue(result.hasWon)
        assertEquals(2, result.winningLinesCount)
    }

    @Test
    fun testBlackoutGameMode() {
        var board = BingoEngine.generateBoard(seed = 400L)

        // Complete 24 tiles (all except tile 1)
        for (i in 0 until 25) {
            if (i != 1 && !board[i].isCenter) {
                board = BingoEngine.toggleTile(board, i)
            }
        }

        var result = BingoEngine.evaluateWin(board, GameMode.BLACKOUT)
        assertFalse(result.hasWon)

        // Complete last tile
        board = BingoEngine.toggleTile(board, 1)
        result = BingoEngine.evaluateWin(board, GameMode.BLACKOUT)
        assertTrue(result.hasWon)
    }
}
