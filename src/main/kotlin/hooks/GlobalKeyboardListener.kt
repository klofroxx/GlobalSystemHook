package com.roselia.hooks

import com.roselia.hooks.listener.KeyboardListener

object GlobalKeyboardListener : KeyboardListener() {
    private var isInitialized = false

    private val keyCallbacks = mutableListOf<KeyCallback>()

    data class KeyCallback(
        val onKeyPressed: ((Int) -> Unit)? = null,
        val onKeyReleased: ((Int) -> Unit)? = null,
        val onKeyHolded: ((Int) -> Unit)? = null
    )

    fun addKeyCallback(callback: KeyCallback) {
        keyCallbacks.add(callback)
    }

    fun removeKeyCallback(callback: KeyCallback) {
        keyCallbacks.remove(callback)
    }

    override fun onKeyPressed(keyCode: Int) {
        keyCallbacks.forEach { it.onKeyPressed?.invoke(keyCode) }
    }

    override fun onKeyHolded(keyCode: Int) {
        keyCallbacks.forEach { it.onKeyHolded?.invoke(keyCode) }
    }

    override fun onKeyReleased(keyCode: Int) {
        keyCallbacks.forEach { it.onKeyReleased?.invoke(keyCode) }
    }

    fun startGlobalListening() {
        if (!isInitialized) {
            try {
                super.startListening()
            } catch (e: Exception) {
                println("Hata oluştu: ${e.message}")
                e.printStackTrace()
            }
            isInitialized = true
        } else {
            println("GlobalKeyboardListener zaten çalışıyor.")
        }
    }

    fun stopGlobalListening() {
        if (isInitialized) {
            println("GlobalKeyboardListener durduruluyor...")
            super.stopListening()
            isInitialized = false
        } else {
            println("GlobalKeyboardListener zaten durdurulmuş.")
        }
    }
}
