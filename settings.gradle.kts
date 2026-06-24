pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases/")
        mavenCentral()
        gradlePluginPortal()
    }

}

rootProject.name = "RedstoneTweaks"

fun addVersion(mcVersion: String, loaders: List<String>) {
    for (loader in loaders) {
        include(":${mcVersion}_${loader}")
        project(":${mcVersion}_${loader}").projectDir = file("$mcVersion/$loader")
    }
}

addVersion("1.18.2", listOf("common", "fabric"))
addVersion("1.19.4", listOf("common", "fabric"))
addVersion("1.20.6", listOf("common", "fabric", "neoforge"))
addVersion("1.21.5", listOf("common", "fabric", "neoforge"))
// 26.1 requires mappings not yet published (Mojang/Yarn/Intermediary unavailable for 26.1)
// addVersion("26.1", listOf("common", "fabric", "neoforge"))
