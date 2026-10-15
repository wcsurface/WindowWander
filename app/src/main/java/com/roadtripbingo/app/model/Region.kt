package com.roadtripbingo.app.model

enum class Region(val displayName: String, val code: String) {
    UNIVERSAL("Universal / Any Region", "ALL"),
    MOUNTAIN_WEST("Mountain West & Rockies", "MTN"),
    PACIFIC_NORTHWEST("Pacific Northwest", "PNW"),
    PACIFIC_SOUTHWEST("Pacific Southwest", "PSW"),
    SOUTHWEST("Southwest & Desert", "SW"),
    MIDWEST("Midwest & Great Plains", "MW"),
    NORTHEAST("Northeast & Mid-Atlantic", "NE"),
    SOUTHEAST("Southeast & Gulf Coast", "SE"),
    TEXAS_SOUTH_CENTRAL("Texas & South Central", "TX");

    companion object {
        val STATE_TO_REGION = mapOf(
            // Mountain West
            "CO" to MOUNTAIN_WEST, "UT" to MOUNTAIN_WEST, "WY" to MOUNTAIN_WEST,
            "MT" to MOUNTAIN_WEST, "ID" to MOUNTAIN_WEST, "NV" to MOUNTAIN_WEST,
            // Pacific Northwest
            "WA" to PACIFIC_NORTHWEST, "OR" to PACIFIC_NORTHWEST, "AK" to PACIFIC_NORTHWEST,
            // Pacific Southwest
            "CA" to PACIFIC_SOUTHWEST, "HI" to PACIFIC_SOUTHWEST,
            // Southwest & Desert
            "AZ" to SOUTHWEST, "NM" to SOUTHWEST,
            // Midwest & Great Plains
            "IL" to MIDWEST, "IN" to MIDWEST, "IA" to MIDWEST, "KS" to MIDWEST,
            "MI" to MIDWEST, "MN" to MIDWEST, "MO" to MIDWEST, "NE" to MIDWEST,
            "ND" to MIDWEST, "OH" to MIDWEST, "SD" to MIDWEST, "WI" to MIDWEST,
            // Northeast & Mid-Atlantic
            "CT" to NORTHEAST, "DE" to NORTHEAST, "ME" to NORTHEAST, "MD" to NORTHEAST,
            "MA" to NORTHEAST, "NH" to NORTHEAST, "NJ" to NORTHEAST, "NY" to NORTHEAST,
            "PA" to NORTHEAST, "RI" to NORTHEAST, "VT" to NORTHEAST, "VA" to NORTHEAST,
            "WV" to NORTHEAST, "DC" to NORTHEAST,
            // Southeast & Gulf Coast
            "AL" to SOUTHEAST, "FL" to SOUTHEAST, "GA" to SOUTHEAST, "KY" to SOUTHEAST,
            "MS" to SOUTHEAST, "NC" to SOUTHEAST, "SC" to SOUTHEAST, "TN" to SOUTHEAST,
            // Texas & South Central
            "TX" to TEXAS_SOUTH_CENTRAL, "OK" to TEXAS_SOUTH_CENTRAL,
            "AR" to TEXAS_SOUTH_CENTRAL, "LA" to TEXAS_SOUTH_CENTRAL
        )

        fun fromStateCode(stateCode: String): Region {
            return STATE_TO_REGION[stateCode.uppercase()] ?: UNIVERSAL
        }
    }
}
