package com.getiox.gradle.buildsrc

import org.gradle.api.Project
import java.io.ByteArrayOutputStream

/**
 * A small utility to interact with pkg-config.
 * Handles compiler flags, linker flags, version info, and library checks.
 */
class PkgConfig(private val project: Project) {

    /**
     * Get compiler flags (-I, -D, etc.) for one or more libraries.
     * Example: compilerFlags("openssl", "zlib")
     */
    fun compilerFlags(vararg libs: String): List<String> =
        libs.flatMap { exec(listOf("--cflags", it)) }.distinct()

    /**
     * Get linker flags (-L, -l, etc.) for libraries.
     * Use `static = true` if you need static linking.
     */
    fun linkerFlags(vararg libs: String, static: Boolean = false): List<String> =
        libs.flatMap { lib ->
            val args = if (static) listOf("--libs", "--static", lib) else listOf("--libs", lib)
            exec(args)
        }.distinct()

    /**
     * Check if a library exists on the system.
     * Returns true if pkg-config can find it.
     */
    fun exists(library: String): Boolean {
        val exitCode = project.exec {
            commandLine("pkg-config", "--exists", library)
            isIgnoreExitValue = true
        }.exitValue
        return exitCode == 0
    }

    /**
     * Get the version of a library, or null if it's not available.
     */
    fun version(library: String): String? =
        exec(listOf("--modversion", library)).firstOrNull()

    /**
     * Internal helper to run pkg-config.
     * Captures output as a list of flags and logs warnings if needed.
     */
    private fun exec(args: List<String>): List<String> {
        val stdout = ByteArrayOutputStream()
        val stderr = ByteArrayOutputStream()

        val exitCode = project.exec {
            commandLine = listOf("pkg-config") + args
            standardOutput = stdout
            errorOutput = stderr
            isIgnoreExitValue = true
        }.exitValue

        if (exitCode != 0 && stderr.toString().isNotBlank()) {
            project.logger.warn("pkg-config ${args.joinToString(" ")} failed: ${stderr.toString().trim()}")
        }

        return stdout.toString()
            .trim()
            .split("\\s+".toRegex())
            .filter { it.isNotBlank() }
    }
}