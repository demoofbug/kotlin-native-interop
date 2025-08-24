import com.getiox.gradle.buildsrc.PkgConfig
import com.getiox.gradle.buildsrc.isLinux
import com.getiox.gradle.buildsrc.skipBuildIfNotLinux

skipBuildIfNotLinux()

val pkg = PkgConfig(project)
kotlin {
    if (isLinux) {
        listOf(linuxX64(), linuxArm64()).forEach {
            it.compilations.getByName("main") {
                val libsecret by cinterops.creating {
                    defFile("src/nativeInterop/cinterop/libsecret.def")
                    packageName("org.gnome.libsecret")

                    // Get compile and link flags via pkg-config
                    compilerOpts.addAll(pkg.compilerFlags("libsecret-1"))
                    linkerOpts.addAll(pkg.linkerFlags("libsecret-1"))
                }
            }
        }
    }
}