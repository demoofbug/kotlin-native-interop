import com.getiox.gradle.buildsrc.PkgConfig
import com.getiox.gradle.buildsrc.skipBuildIfNotLinux

skipBuildIfNotLinux()

val pkg = PkgConfig(project)
kotlin {
    listOf(linuxX64(), linuxArm64()).forEach {
        it.compilations.getByName("main") {
            val libsecret by cinterops.creating {
                defFile("src/nativeInterop/cinterop/libsecret.def")

                // Get compile and link flags via pkg-config
                compilerOpts += pkg.compilerFlags("libsecret-1")
                linkerOpts += pkg.linkerFlags("libsecret-1")
            }
        }
    }
}