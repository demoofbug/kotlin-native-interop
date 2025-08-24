import com.getiox.gradle.buildsrc.isWindows
import com.getiox.gradle.buildsrc.skipBuildIfNotWindows

skipBuildIfNotWindows()

kotlin {
    if(isWindows) {
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
    }
}