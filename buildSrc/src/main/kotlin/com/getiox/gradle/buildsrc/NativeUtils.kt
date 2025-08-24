package com.getiox.gradle.buildsrc

import org.gradle.api.Project

/**
 * Executes the block only on Linux hosts
 */
fun Project.linuxOnly(block: () -> Unit) {

    if (System.getProperty("os.name").contains("Linux", ignoreCase = true)) {
        block()
    }
}

/**
 * Executes the block only on macOS hosts
 */
fun Project.macosOnly(block: () -> Unit) {
    if (System.getProperty("os.name").contains("Mac", ignoreCase = true)) {
        block()
    }
}

/**
 * Executes the block only on Windows hosts
 */
fun Project.windowsOnly(block: () -> Unit) {
    if (System.getProperty("os.name").contains("Windows", ignoreCase = true)) {
        block()
    }
}
