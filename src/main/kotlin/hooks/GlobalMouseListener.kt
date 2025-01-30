package com.roselia.hooks

import com.roselia.hooks.listener.MouseListener

object GlobalMouseListener : MouseListener() {
    private var isInitialized = false

    private val mouseCallbacks = mutableListOf<MouseCallback>()

    data class MouseCallback(
        val onMousePressed: ((Int, Int, Int) -> Unit)? = null,
        val onMouseReleased: ((Int, Int, Int) -> Unit)? = null,
        val onMouseHolded: ((Int, Int, Int) -> Unit)? = null,
        val onMouseMoved: ((Int, Int) -> Unit)? = null,
        val onMouseWheel: ((Int) -> Unit)? = null,
    )

    fun addMouseCallback(callback: MouseCallback) {
        mouseCallbacks.add(callback)
    }

    fun removeMouseCallback(callback: MouseCallback) {
        mouseCallbacks.remove(callback)
    }

    override fun onMousePressed(buttonCode: Int, x: Int, y: Int) {
        mouseCallbacks.forEach { it.onMousePressed?.invoke(buttonCode, x, y) }
    }

    override fun onMouseHolded(buttonCode: Int, x: Int, y: Int) {
        mouseCallbacks.forEach { it.onMouseHolded?.invoke(buttonCode, x, y) }
    }

    override fun onMouseReleased(buttonCode: Int, x: Int, y: Int) {
        mouseCallbacks.forEach { it.onMouseReleased?.invoke(buttonCode, x, y) }
    }

    override fun onMouseMoved(x: Int, y: Int) {
        mouseCallbacks.forEach { it.onMouseMoved?.invoke(x, y) }
    }

    override fun onMouseWheel(scrollAmount: Int) {
        mouseCallbacks.forEach { it.onMouseWheel?.invoke(scrollAmount) }
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
            println("GlobalMouseListener zaten çalışıyor.")
        }
    }

    fun stopGlobalListening() {
        if (isInitialized) {
            println("GlobalMouseListener durduruluyor...")
            super.stopListening()
            isInitialized = false
        } else {
            println("GlobalMouseListener zaten durdurulmuş.")
        }
    }
}
