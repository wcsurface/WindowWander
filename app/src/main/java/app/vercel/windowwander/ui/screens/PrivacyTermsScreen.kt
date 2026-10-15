package app.vercel.windowwander.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.vercel.windowwander.ui.theme.MediumNavy
import app.vercel.windowwander.ui.theme.MidnightNavy
import app.vercel.windowwander.ui.theme.SunnyYellow
import app.vercel.windowwander.ui.theme.VividOrange

@Composable
fun PrivacyTermsScreen(
    initialTab: Int = 0, // 0 = FAQ, 1 = T&C, 2 = Privacy
    onBackToHome: () -> Unit
) {
    var activeTab by remember { mutableStateOf(initialTab) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MidnightNavy)
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp, top = 38.dp, bottom = 24.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (activeTab) {
                    0 -> "❓ FAQ & Guide"
                    1 -> "📜 Terms & Conditions"
                    else -> "🔒 Privacy Policy"
                },
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MediumNavy)
                    .clickable { onBackToHome() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Back",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // TAB SWITCHER ROW (FAQ - T&C - PRIVACY)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MediumNavy)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // TAB 0: FAQ
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeTab == 0) SunnyYellow else Color.Transparent)
                    .clickable { activeTab = 0 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "❓ FAQ",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (activeTab == 0) MidnightNavy else Color.White
                )
            }

            // TAB 1: T&C
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeTab == 1) SunnyYellow else Color.Transparent)
                    .clickable { activeTab = 1 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📜 T&C",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (activeTab == 1) MidnightNavy else Color.White
                )
            }

            // TAB 2: PRIVACY
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (activeTab == 2) SunnyYellow else Color.Transparent)
                    .clickable { activeTab = 2 }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔒 Privacy",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (activeTab == 2) MidnightNavy else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TAB CONTENT CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MediumNavy),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                when (activeTab) {
                    0 -> {
                        // FAQ PAGE CONTENT
                        Text("Frequently Asked Questions", fontSize = 18.sp, fontWeight = FontWeight.Black, color = SunnyYellow)
                        Spacer(modifier = Modifier.height(12.dp))

                        FaqItem(
                            q = "How do I play Road Trip Bingo?",
                            a = "Look out the car window during your road trip! As you spot roadside items shown on your 5x5 Bingo card (like a Red Truck, Speed Limit Sign, or Wind Turbine), tap the tile to complete it. Get 5 completed tiles in a row to score a BINGO!"
                        )

                        FaqItem(
                            q = "How does local In-Car Multiplayer work?",
                            a = "One passenger taps 'Start a Game (Host)' to generate a 4-letter room code (e.g. ROAD). Other passengers in the vehicle tap 'Join a Game' and enter the 4-letter code to join the same car room. Everyone plays together on their own cards!"
                        )

                        FaqItem(
                            q = "What are Regional Cards?",
                            a = "Road Trip Bingo includes regional cards specifically tailored to different parts of America, including Mountain West, Desert Southwest, Northeast, The South, West Coast, and Heart of the Country!"
                        )

                        FaqItem(
                            q = "How do Daily Free Limits & Rewarded Ads work?",
                            a = "Players get 2 free games every day. After completing 2 games, you can watch a quick video ad to unlock additional rounds instantly! The daily limit resets automatically every midnight."
                        )

                        FaqItem(
                            q = "Is driver safety strictly enforced?",
                            a = "YES! Road Trip Bingo is designed strictly for vehicle PASSENGERS. Drivers must keep their full attention on driving at all times."
                        )
                    }

                    1 -> {
                        // T&C PAGE CONTENT
                        Text("Terms & Conditions", fontSize = 18.sp, fontWeight = FontWeight.Black, color = SunnyYellow)
                        Spacer(modifier = Modifier.height(12.dp))

                        LegalSection(
                            title = "1. Passenger Safety Disclaimer",
                            content = "Road Trip Bingo is strictly an entertainment application for vehicle PASSENGERS. Drivers must never operate or interact with this application while operating a motor vehicle. By using this app, you agree to comply with all local traffic laws and safety regulations."
                        )

                        LegalSection(
                            title = "2. User Accounts & Fair Play",
                            content = "Users may play as Guests or log in to persist their XP level, badges, and lifetime wins. Users agree not to tamper with or exploit game state network calls."
                        )

                        LegalSection(
                            title = "3. Free-to-Play & Advertising Terms",
                            content = "Road Trip Bingo is free to play and supported by optional rewarded video ads. Watching optional video ads unlocks additional daily game rounds."
                        )

                        LegalSection(
                            title = "4. Limitation of Liability",
                            content = "The developers of Road Trip Bingo are not liable for any accidents, injuries, or damages resulting from improper driver distraction or misuse of the application while operating a vehicle."
                        )
                    }

                    else -> {
                        // PRIVACY POLICY CONTENT
                        Text("Privacy Policy", fontSize = 18.sp, fontWeight = FontWeight.Black, color = SunnyYellow)
                        Spacer(modifier = Modifier.height(12.dp))

                        LegalSection(
                            title = "1. Data Protection & Privacy",
                            content = "Road Trip Bingo respects your privacy. We do not track continuous background GPS, record driver location history, or transmit vehicle telemetry."
                        )

                        LegalSection(
                            title = "2. Local & Cloud Data Storage",
                            content = "Your game progress (XP, level, earned badges, and lifetime wins) is stored locally on your device via SharedPreferences. If you create an account, progress is backed up securely to enable cross-device restore."
                        )

                        LegalSection(
                            title = "3. Advertising & Analytics",
                            content = "Optional rewarded video ads are served to unlock extra daily game rounds. Advertising partners may process anonymous device signals in accordance with standard mobile privacy guidelines."
                        )

                        LegalSection(
                            title = "4. Contact Us",
                            content = "For questions, feedback, or support, contact us at roadtripbingo@gmail.com."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FaqItem(q: String, a: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = "Q: $q", fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = a, fontSize = 12.5.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
    }
}

@Composable
fun LegalSection(title: String, content: String) {
    Column(modifier = Modifier.padding(bottom = 14.dp)) {
        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Black, color = Color.White)
        Spacer(modifier = Modifier.height(3.dp))
        Text(text = content, fontSize = 12.5.sp, color = Color.White.copy(alpha = 0.85f), lineHeight = 18.sp)
    }
}
