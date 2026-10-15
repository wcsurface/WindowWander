package app.vercel.windowwander

import app.vercel.windowwander.data.DeckRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DeckRepositoryTest {

    @Test
    fun testAllDecksHaveAtLeast24Prompts() {
        val decks = DeckRepository.DECKS
        assertTrue("Expected at least 9 decks", decks.size >= 9)

        decks.forEach { deck ->
            assertTrue(
                "Deck ${deck.title} (${deck.id}) should have at least 24 prompts, but has ${deck.prompts.size}",
                deck.prompts.size >= 24
            )
        }
    }

    @Test
    fun testPromptIDsAreUniqueWithinDecks() {
        DeckRepository.DECKS.forEach { deck ->
            val promptIds = deck.prompts.map { it.id }
            val uniqueIds = promptIds.toSet()
            assertEquals("Duplicate prompt IDs found in deck ${deck.id}", promptIds.size, uniqueIds.size)
        }
    }

    @Test
    fun testPromptsHaveValidDescriptions() {
        DeckRepository.DECKS.forEach { deck ->
            deck.prompts.forEach { prompt ->
                assertTrue("Prompt ${prompt.id} in ${deck.id} has empty name", prompt.name.isNotBlank())
                assertTrue("Prompt ${prompt.id} in ${deck.id} has empty description", prompt.description.isNotBlank())
            }
        }
    }
}
