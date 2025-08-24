package com.getiox.gradle.buildsrc

import org.gradle.api.Project

/**
 * Generic skip function
 * @param condition if true → skip project
 * @param reason message to show in logs
 */
fun Project.skipBuildIf(condition: Boolean, reason: String) {
    if (condition) {
        logger.lifecycle("Skipping project '$path': $reason")
        tasks.configureEach { enabled = false }
        return
    }
}

fun Project.skipBuildIfNotLinux() =
    skipBuildIf(!isLinux, "current OS is not Linux")

fun Project.skipBuildIfNotWindows() =
    skipBuildIf(!isWindows, "current OS is not Windows")

fun Project.skipBuildIfNotMac() =
    skipBuildIf(!isMacOS, "current OS is not macOS")