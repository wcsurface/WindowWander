package app.vercel.windowwander.data

import android.content.Context
import android.media.MediaPlayer
import app.vercel.windowwander.R
import app.vercel.windowwander.viewmodel.Screen

object BackgroundMusicManager {
    private var mediaPlayer: MediaPlayer? = null

    fun syncMusicState(context: Context, currentScreen: Screen, soundEnabled: Boolean) {
        if (!soundEnabled) {
            pauseMusic()
            return
        }

        if (currentScreen == Screen.BINGO_GAME) {
            pauseMusic()
        } else {
            startOrResumeMusic(context)
        }
    }

    private fun startOrResumeMusic(context: Context) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.road_trip_bingo_theme)?.apply {
                    isLooping = true
                    setVolume(0.18f, 0.18f) // Quiet 18% background music volume
                }
            }

            if (mediaPlayer?.isPlaying == false) {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {}
    }

    fun pauseMusic() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {}
    }

    fun stopAndRelease() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {}
    }
}
