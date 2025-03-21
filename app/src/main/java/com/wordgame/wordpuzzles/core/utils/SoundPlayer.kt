package com.wordgame.wordpuzzles.core.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object SoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun init() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer().apply {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()

                setAudioAttributes(audioAttributes)
                setVolume(1.0f, 1.0f)
            }
        }
    }

    private val soundPlayerLock = Any()

    fun changeVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
    }

    fun playSound(context: Context, soundResId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            synchronized(soundPlayerLock) {
                mediaPlayer?.apply {
                    reset()
                    val fileDescriptor = context.resources.openRawResourceFd(soundResId)
                    setDataSource(
                        fileDescriptor.fileDescriptor,
                        fileDescriptor.startOffset,
                        fileDescriptor.length
                    )
                    fileDescriptor.close()
                    prepare()
                    start()
                }
            }
        }
    }

    fun playBackgroundMusic(context: Context, soundResId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            synchronized(soundPlayerLock) {
                mediaPlayer?.apply {
                    reset()
                    val fileDescriptor = context.resources.openRawResourceFd(soundResId)
                    setDataSource(
                        fileDescriptor.fileDescriptor,
                        fileDescriptor.startOffset,
                        fileDescriptor.length
                    )
                    fileDescriptor.close()
                    prepare()
                    isLooping = true
                    start()
                }
            }
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun pauseBackgroundMusic() {
        mediaPlayer?.pause()
    }
}