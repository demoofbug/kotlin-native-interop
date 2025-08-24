import com.getiox.gradle.buildsrc.PkgConfig

val pkg = PkgConfig(project)

kotlin {
    linuxX64 {
        compilations.getByName("main") {
            val libsecret by cinterops.creating {
                defFile("src/nativeInterop/cinterop/libsecret.def")
                packageName("org.gnome.libsecret")
                
                // Get compile and link flags via pkg-config
                compilerOpts.addAll(pkg.compilerFlags("libsecret-1"))
                linkerOpts.addAll(pkg.linkerFlags("libsecret-1"))
            }
        }
    }
    
    linuxArm64 {
        compilations.getByName("main") {
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
