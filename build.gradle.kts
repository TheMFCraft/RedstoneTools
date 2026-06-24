plugins {
    id("dev.architectury.loom") version "1.13.467" apply false
    id("architectury-plugin") version "3.4.160" apply false
    id("com.gradleup.shadow") version "8.3.5" apply false
}

subprojects {
    group = "phiro.redstonetweaks"
    version = project.findProperty("mod_version") ?: "1.0.0"

    repositories {
        maven("https://maven.neoforged.net/releases/")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
}
