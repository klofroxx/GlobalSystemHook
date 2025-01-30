package com.roselia.hooks.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import lc.kra.system.mouse.GlobalMouseHook
import lc.kra.system.mouse.event.GlobalMouseEvent
import lc.kra.system.mouse.event.GlobalMouseListener
import kotlin.concurrent.fixedRateTimer

open class MouseListener {
    private val mouseHook = GlobalMouseHook()
    private val pressedButtons = mutableSetOf<Int>()
    private val buttonHoldTimers = mutableMapOf<Int, java.util.Timer>()
    private val isListening = AtomicBoolean(false)

    init {
        mouseHook.addMouseListener(object : GlobalMouseListener {
            override fun mousePressed(event: GlobalMouseEvent) {
                if (!isListening.get()) return

                val buttonCode = event.button
                if (!pressedButtons.contains(buttonCode)) {
                    pressedButtons.add(buttonCode)
                    onMousePressed(buttonCode, event.x, event.y)

                    val timer = fixedRateTimer(period = 100) {
                        onMouseHolded(buttonCode, event.x, event.y)
                    }
                    buttonHoldTimers[buttonCode] = timer
                }
            }

            override fun mouseReleased(event: GlobalMouseEvent) {
                if (!isListening.get()) return

                val buttonCode = event.button
                pressedButtons.remove(buttonCode)
                buttonHoldTimers[buttonCode]?.cancel()
                buttonHoldTimers.remove(buttonCode)
                onMouseReleased(buttonCode, event.x, event.y)
            }

            override fun mouseMoved(event: GlobalMouseEvent) {
                if (!isListening.get()) return
                onMouseMoved(event.x, event.y)
            }

            override fun mouseWheel(event: GlobalMouseEvent) {
                if (!isListening.get()) return
                onMouseWheel(event.delta)
            }
        })
    }

    open fun onMousePressed(buttonCode: Int, x: Int, y: Int) {}
    open fun onMouseHolded(buttonCode: Int, x: Int, y: Int) {}
    open fun onMouseReleased(buttonCode: Int, x: Int, y: Int) {}
    open fun onMouseMoved(x: Int, y: Int) {}
    open fun onMouseWheel(scrollAmount: Int) {}

    fun isMouseButtonPressed(buttonCode: Int): Boolean {
        return pressedButtons.contains(buttonCode)
    }

    fun getButtonName(buttonCode: Int): String {
        return when (buttonCode) {
            GlobalMouseEvent.BUTTON_LEFT -> "BUTTON_LEFT"
            GlobalMouseEvent.BUTTON_RIGHT -> "BUTTON_RIGHT"
            GlobalMouseEvent.BUTTON_MIDDLE -> "BUTTON_MIDDLE"
            GlobalMouseEvent.BUTTON_X1 -> "BUTTON_X1"
            GlobalMouseEvent.BUTTON_X2 -> "BUTTON_X2"
            else -> "UNKNOWN_BUTTON"
        }
    }

    fun getButtonCode(buttonName: String): Int {
        return when (buttonName.uppercase()) {
            "BUTTON_LEFT" -> GlobalMouseEvent.BUTTON_LEFT
            "BUTTON_RIGHT" -> GlobalMouseEvent.BUTTON_RIGHT
            "BUTTON_MIDDLE" -> GlobalMouseEvent.BUTTON_MIDDLE
            "BUTTON_X1" -> GlobalMouseEvent.BUTTON_X1
            "BUTTON_X2" -> GlobalMouseEvent.BUTTON_X2
            else -> -1
        }
    }

    fun startListening() {
        isListening.set(true)
        CoroutineScope(Dispatchers.IO).launch {
            while (isListening.get()) {
                delay(1)
            }
        }
    }

    fun stopListening() {
        isListening.set(false)
        mouseHook.shutdownHook()
        buttonHoldTimers.values.forEach { it.cancel() }
        buttonHoldTimers.clear()
        pressedButtons.clear()
    }
}
