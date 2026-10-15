package com.roadtripbingo.app.model

enum class GameMode(val displayName: String, val description: String) {
    CLASSIC_LINE("Classic Line Bingo", "Complete any row, column, or diagonal line to win."),
    TWO_LINES("Two Lines Bingo", "Complete any two winning lines to win."),
    BLACKOUT("Full Card Blackout", "Mark every single square on your board to win."),
    RACE_MODE("Race Mode", "First player in the car/room to get Bingo wins!"),
    DAILY_CHALLENGE("Daily Road Trip Challenge", "A special daily selection of road trip targets.")
}
