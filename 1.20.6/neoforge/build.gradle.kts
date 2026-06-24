plugins {
    java
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.gradleup.shadow")
}

group = "phiro.redstonetweaks"
version = rootProject.findProperty("mod_version") ?: "1.0.0"

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(21)
    targetCompatibility = JavaVersion.toVersion(21)
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    silentMojangMappingsLicense()
    neoForge { }
}

configurations {
    create("common")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<ProcessResources>().configureEach {
    filesMatching(listOf("META-INF/neoforge.mods.toml")) {
        expand(
            "version" to (project.findProperty("mod_version") ?: "1.0.0"),
            "mod_version" to (project.findProperty("mod_version") ?: "1.0.0")
        )
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings(loom.officialMojangMappings())
    "neoForge"("net.neoforged:neoforge:20.6.139")
    modImplementation("dev.architectury:architectury-neoforge:12.1.4")
    "common"(project(":1.20.6_common")) { isTransitive = false }
    shadow(project(":1.20.6_common")) { isTransitive = false }
}

val java21 = javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) }
tasks.named<JavaExec>("runClient") { javaLauncher.set(java21) }
