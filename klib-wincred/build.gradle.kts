import com.getiox.gradle.buildsrc.skipBuildIfNotWindows

skipBuildIfNotWindows()

kotlin {
    mingwX64 {
        compilations.getByName("main") {
            val wincred by cinterops.creating {
                defFile("src/nativeInterop/cinterop/wincred.def")
            }
        }
    }
}