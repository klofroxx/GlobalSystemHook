package com.roselia.hooks.listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lc.kra.system.keyboard.GlobalKeyboardHook
import lc.kra.system.keyboard.event.GlobalKeyEvent
import lc.kra.system.keyboard.event.GlobalKeyListener
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.fixedRateTimer

open class KeyboardListener {
    private val keyboardHook = GlobalKeyboardHook()
    private val pressedKeys = mutableSetOf<Int>()
    private val keyHoldTimers = mutableMapOf<Int, java.util.Timer>()
    private val isListening = AtomicBoolean(false)

    init {
        keyboardHook.addKeyListener(object : GlobalKeyListener {
            override fun keyPressed(event: GlobalKeyEvent) {
                if (!isListening.get()) return

                val keyCode = event.virtualKeyCode

                if (!pressedKeys.contains(keyCode)) {
                    pressedKeys.add(keyCode)
                    onKeyPressed(keyCode)

                    val timer = fixedRateTimer(period = 100) {
                        onKeyHolded(keyCode)
                    }
                    keyHoldTimers[keyCode] = timer
                }
            }

            override fun keyReleased(event: GlobalKeyEvent) {
                if (!isListening.get()) return

                val keyCode = event.virtualKeyCode

                pressedKeys.remove(keyCode)
                keyHoldTimers[keyCode]?.cancel()
                keyHoldTimers.remove(keyCode)

                onKeyReleased(keyCode)
            }
        })
    }

    open fun onKeyPressed(keyCode: Int) {
    }

    open fun onKeyHolded(keyCode: Int) {
    }
    fun isKeyPressed(keyCode: Int): Boolean {
        return pressedKeys.contains(keyCode)
    }

    open fun onKeyReleased(keyCode: Int) {
    }

    fun getKeyName(virtualKeyCode: Int): String {
        return when (virtualKeyCode) {
            GlobalKeyEvent.VK_LBUTTON -> "L_BUTTON"
            GlobalKeyEvent.VK_RBUTTON -> "R_BUTTON"
            GlobalKeyEvent.VK_CANCEL -> "CANCEL"
            GlobalKeyEvent.VK_MBUTTON -> "M_BUTTON"
            GlobalKeyEvent.VK_XBUTTON1 -> "X_BUTTON1"
            GlobalKeyEvent.VK_XBUTTON2 -> "X_BUTTON2"
            GlobalKeyEvent.VK_BACK -> "BACK"
            GlobalKeyEvent.VK_TAB -> "TAB"
            GlobalKeyEvent.VK_CLEAR -> "CLEAR"
            GlobalKeyEvent.VK_RETURN -> "RETURN"
            GlobalKeyEvent.VK_LSHIFT -> "L_SHIFT"
            GlobalKeyEvent.VK_RSHIFT -> "R_SHIFT"
            GlobalKeyEvent.VK_LCONTROL -> "L_CONTROL"
            GlobalKeyEvent.VK_RCONTROL -> "R_CONTROL"
            GlobalKeyEvent.VK_LMENU -> "L_ALT"
            GlobalKeyEvent.VK_RMENU -> "R_ALT"
            GlobalKeyEvent.VK_PAUSE -> "PAUSE"
            GlobalKeyEvent.VK_CAPITAL -> "CAPITAL"
            GlobalKeyEvent.VK_KANA -> "KANA"
            GlobalKeyEvent.VK_HANGUEL, GlobalKeyEvent.VK_HANGUL -> "HANGUL"
            GlobalKeyEvent.VK_JUNJA -> "JUNJA"
            GlobalKeyEvent.VK_FINAL -> "FINAL"
            GlobalKeyEvent.VK_HANJA, GlobalKeyEvent.VK_KANJI -> "KANJI"
            GlobalKeyEvent.VK_ESCAPE -> "ESCAPE"
            GlobalKeyEvent.VK_CONVERT -> "CONVERT"
            GlobalKeyEvent.VK_NONCONVERT -> "NON_CONVERT"
            GlobalKeyEvent.VK_ACCEPT -> "ACCEPT"
            GlobalKeyEvent.VK_MODECHANGE -> "MODE_CHANGE"
            GlobalKeyEvent.VK_SPACE -> "SPACE"
            GlobalKeyEvent.VK_PRIOR -> "PRIOR"
            GlobalKeyEvent.VK_NEXT -> "NEXT"
            GlobalKeyEvent.VK_END -> "END"
            GlobalKeyEvent.VK_HOME -> "HOME"
            GlobalKeyEvent.VK_LEFT -> "LEFT"
            GlobalKeyEvent.VK_UP -> "UP"
            GlobalKeyEvent.VK_RIGHT -> "RIGHT"
            GlobalKeyEvent.VK_DOWN -> "DOWN"
            GlobalKeyEvent.VK_SELECT -> "SELECT"
            GlobalKeyEvent.VK_PRINT -> "PRINT"
            GlobalKeyEvent.VK_EXECUTE -> "EXECUTE"
            GlobalKeyEvent.VK_SNAPSHOT -> "SNAPSHOT"
            GlobalKeyEvent.VK_INSERT -> "INSERT"
            GlobalKeyEvent.VK_DELETE -> "DELETE"
            GlobalKeyEvent.VK_HELP -> "HELP"
            in GlobalKeyEvent.VK_0..GlobalKeyEvent.VK_9 -> (virtualKeyCode - GlobalKeyEvent.VK_0).toString()
            in GlobalKeyEvent.VK_A..GlobalKeyEvent.VK_Z -> (virtualKeyCode - GlobalKeyEvent.VK_A + 'A'.code).toChar().toString()
            GlobalKeyEvent.VK_LWIN -> "L_WIN"
            GlobalKeyEvent.VK_RWIN -> "R_WIN"
            GlobalKeyEvent.VK_APPS -> "APPS"
            GlobalKeyEvent.VK_SLEEP -> "SLEEP"
            in GlobalKeyEvent.VK_NUMPAD0..GlobalKeyEvent.VK_NUMPAD9 -> "NUMPAD${virtualKeyCode - GlobalKeyEvent.VK_NUMPAD0}"
            GlobalKeyEvent.VK_MULTIPLY -> "MULTIPLY"
            GlobalKeyEvent.VK_ADD -> "ADD"
            GlobalKeyEvent.VK_SEPARATOR -> "SEPARATOR"
            GlobalKeyEvent.VK_SUBTRACT -> "SUBTRACT"
            GlobalKeyEvent.VK_DECIMAL -> "DECIMAL"
            GlobalKeyEvent.VK_DIVIDE -> "DIVIDE"
            in GlobalKeyEvent.VK_F1..GlobalKeyEvent.VK_F12 -> "F${virtualKeyCode - GlobalKeyEvent.VK_F1 + 1}"
            else -> "UNKNOWN_KEY"
        }
    }
    fun getVirtualKeyCode(keyName: String): Int {
        return when (keyName.uppercase()) {
            "L_BUTTON" -> GlobalKeyEvent.VK_LBUTTON
            "R_BUTTON" -> GlobalKeyEvent.VK_RBUTTON
            "CANCEL" -> GlobalKeyEvent.VK_CANCEL
            "M_BUTTON" -> GlobalKeyEvent.VK_MBUTTON
            "X_BUTTON1" -> GlobalKeyEvent.VK_XBUTTON1
            "X_BUTTON2" -> GlobalKeyEvent.VK_XBUTTON2
            "BACK" -> GlobalKeyEvent.VK_BACK
            "TAB" -> GlobalKeyEvent.VK_TAB
            "CLEAR" -> GlobalKeyEvent.VK_CLEAR
            "RETURN" -> GlobalKeyEvent.VK_RETURN
            "L_SHIFT" -> GlobalKeyEvent.VK_LSHIFT
            "R_SHIFT" -> GlobalKeyEvent.VK_RSHIFT
            "L_CONTROL" -> GlobalKeyEvent.VK_LCONTROL
            "R_CONTROL" -> GlobalKeyEvent.VK_RCONTROL
            "L_ALT" -> GlobalKeyEvent.VK_LMENU
            "R_ALT" -> GlobalKeyEvent.VK_RMENU
            "PAUSE" -> GlobalKeyEvent.VK_PAUSE
            "CAPITAL" -> GlobalKeyEvent.VK_CAPITAL
            "KANA" -> GlobalKeyEvent.VK_KANA
            "HANGUL" -> GlobalKeyEvent.VK_HANGUL
            "JUNJA" -> GlobalKeyEvent.VK_JUNJA
            "FINAL" -> GlobalKeyEvent.VK_FINAL
            "KANJI" -> GlobalKeyEvent.VK_KANJI
            "ESCAPE" -> GlobalKeyEvent.VK_ESCAPE
            "CONVERT" -> GlobalKeyEvent.VK_CONVERT
            "NON_CONVERT" -> GlobalKeyEvent.VK_NONCONVERT
            "ACCEPT" -> GlobalKeyEvent.VK_ACCEPT
            "MODE_CHANGE" -> GlobalKeyEvent.VK_MODECHANGE
            "SPACE" -> GlobalKeyEvent.VK_SPACE
            "PRIOR" -> GlobalKeyEvent.VK_PRIOR
            "NEXT" -> GlobalKeyEvent.VK_NEXT
            "END" -> GlobalKeyEvent.VK_END
            "HOME" -> GlobalKeyEvent.VK_HOME
            "LEFT" -> GlobalKeyEvent.VK_LEFT
            "UP" -> GlobalKeyEvent.VK_UP
            "RIGHT" -> GlobalKeyEvent.VK_RIGHT
            "DOWN" -> GlobalKeyEvent.VK_DOWN
            "SELECT" -> GlobalKeyEvent.VK_SELECT
            "PRINT" -> GlobalKeyEvent.VK_PRINT
            "EXECUTE" -> GlobalKeyEvent.VK_EXECUTE
            "SNAPSHOT" -> GlobalKeyEvent.VK_SNAPSHOT
            "INSERT" -> GlobalKeyEvent.VK_INSERT
            "DELETE" -> GlobalKeyEvent.VK_DELETE
            "HELP" -> GlobalKeyEvent.VK_HELP
            in "0".."9" -> GlobalKeyEvent.VK_0 + keyName.toInt()
            in "A".."Z" -> GlobalKeyEvent.VK_A + (keyName[0] - 'A')
            "L_WIN" -> GlobalKeyEvent.VK_LWIN
            "R_WIN" -> GlobalKeyEvent.VK_RWIN
            "APPS" -> GlobalKeyEvent.VK_APPS
            "SLEEP" -> GlobalKeyEvent.VK_SLEEP
            in "NUMPAD0".."NUMPAD9" -> GlobalKeyEvent.VK_NUMPAD0 + keyName.removePrefix("NUMPAD").toInt()
            "MULTIPLY" -> GlobalKeyEvent.VK_MULTIPLY
            "ADD" -> GlobalKeyEvent.VK_ADD
            "SEPARATOR" -> GlobalKeyEvent.VK_SEPARATOR
            "SUBTRACT" -> GlobalKeyEvent.VK_SUBTRACT
            "DECIMAL" -> GlobalKeyEvent.VK_DECIMAL
            "DIVIDE" -> GlobalKeyEvent.VK_DIVIDE
            in "F1".."F12" -> GlobalKeyEvent.VK_F1 + (keyName.removePrefix("F").toInt() - 1)
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
        keyboardHook.shutdownHook()
    }
}
