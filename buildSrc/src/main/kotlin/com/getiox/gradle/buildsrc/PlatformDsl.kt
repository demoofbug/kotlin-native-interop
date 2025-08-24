package com.getiox.gradle.buildsrc

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem

/**
 * Current platform check utilities
 */
val Project.isLinux: Boolean get() = OperatingSystem.current().isLinux
val Project.isWindows: Boolean get() = OperatingSystem.current().isWindows
val Project.isMacOS: Boolean get() = OperatingSystem.current().isMacOsX