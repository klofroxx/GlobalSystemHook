package com.roselia

import com.roselia.hooks.GlobalKeyboardListener
import com.roselia.hooks.GlobalKeyboardListener.KeyCallback
import com.roselia.hooks.GlobalKeyboardListener.getKeyName
import com.roselia.hooks.GlobalMouseListener
import com.roselia.hooks.listener.MouseListener
import lc.kra.system.keyboard.event.GlobalKeyEvent
import lc.kra.system.mouse.event.GlobalMouseEvent

fun main() {
    println("Global keyboard listener is starting...")

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Program is closing, global keyboard listener is stoping...")
        GlobalKeyboardListener.stopGlobalListening()
        GlobalMouseListener.stopGlobalListening()
    })

    GlobalKeyboardListener.addKeyCallback(KeyCallback(
        onKeyPressed = { keyCode ->
            //for example
            if (keyCode == GlobalKeyEvent.VK_A) { //on pressed key a
                println("Pressed Key A")
            }
            println("Key pressed: ${getKeyName(keyCode)}")
        },
        onKeyReleased = { keyCode ->
            println("Key released: ${getKeyName(keyCode)}")
        },
        onKeyHolded = { keyCode ->
            println("Key is holding: ${getKeyName(keyCode)}")
        }
    ))

    GlobalMouseListener.addMouseCallback(GlobalMouseListener.MouseCallback(
        onMousePressed = { buttonCode, x, y ->
            //for example
            if (buttonCode == GlobalMouseEvent.BUTTON_LEFT) { //on clicked left button
                println("Pressed Left Button")
            }
            println("Button pressed: ${GlobalMouseListener.getButtonName(buttonCode)}")
        },
        onMouseReleased = { buttonCode, x, y ->
            println("Button released: ${GlobalMouseListener.getButtonName(buttonCode)}")
        },
        onMouseHolded = { buttonCode, x, y ->
            println("Button is holding: ${GlobalMouseListener.getButtonName(buttonCode)}")
        }
    ))

    GlobalKeyboardListener.startGlobalListening()
    GlobalMouseListener.startGlobalListening()

    while (true) {
        Thread.sleep(1000)
    }
}