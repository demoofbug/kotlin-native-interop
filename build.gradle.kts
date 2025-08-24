import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
   alias(libs.plugins.kotlin.multiplatform) apply false
   alias(libs.plugins.kotlin.jvm) apply false
   alias(libs.plugins.dokka) apply false
   alias(libs.plugins.ktlint) apply false
   alias(libs.plugins.android.library) apply false
   alias(libs.plugins.maven.publish) apply false
}

allprojects {
   repositories {
      maven("https://maven.aliyun.com/repository/public/")
      mavenLocal()
      google()
      mavenCentral()
   }
}

subprojects{
   apply(plugin = "org.jetbrains.kotlin.multiplatform")
   apply(plugin = "com.vanniktech.maven.publish")

   tasks.withType<KotlinCompile>().configureEach {
      compilerOptions {
         jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
      }
   }
}

// Unified build tasks
tasks.register("buildAllKlibs") {
    group = "build"
    description = "Build KLIB files for all platforms"
    
    dependsOn(
        ":klib-libsecret:build",
        ":klib-wincred:build"
    )
}

tasks.register("packageAllKlibs") {
    group = "build" 
    description = "Package all KLIB files for release"
    
    dependsOn(
        ":klib-libsecret:build",
        ":klib-wincred:build"
    )
    
    doLast {
        val releaseDir = file("build/release-klibs")
        releaseDir.mkdirs()
        
        // Collect all KLIB files to unified directory
        copy {
            from("klib-libsecret/build/bin")
            into("build/release-klibs/libsecret")
            include("**/*.klib")
        }
        
        copy {
            from("klib-wincred/build/bin")
            into("build/release-klibs/wincred")
            include("**/*.klib")
        }
        
        // Generate checksum file
        val checksumFile = file("build/release-klibs/checksums.txt")
        checksumFile.writeText("")
        
        fileTree("build/release-klibs").matching {
            include("**/*.klib")
        }.forEach { klibFile ->
            val relativePath = klibFile.relativeTo(file("build/release-klibs"))
            val checksum = klibFile.readBytes().let { bytes ->
                java.security.MessageDigest.getInstance("SHA-256")
                    .digest(bytes)
                    .joinToString("") { "%02x".format(it) }
            }
            checksumFile.appendText("$checksum  $relativePath\n")
        }
        
        println("KLIB files packaged to: ${releaseDir.absolutePath}")
    }
}

// Clean release directory
tasks.register("cleanRelease") {
    group = "build"
    description = "Clean release directory"
    
    doLast {
        delete("build/release-klibs")
    }
}