kotlin {
    mingwX64 {
        compilations.getByName("main") {
            val wincred by cinterops.creating {
                defFile("src/nativeInterop/cinterop/wincred.def")
                packageName("com.microsoft.wincred")
                
                // Link Windows system libraries
                linkerOpts.addAll(listOf("-ladvapi32", "-lcredui"))
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val mingwX64Main by getting {
            dependsOn(nativeMain)
        }
    }
}