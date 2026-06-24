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
    sourceCompatibility = JavaVersion.toVersion(17)
    targetCompatibility = JavaVersion.toVersion(17)
}

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    silentMojangMappingsLicense()
}

configurations {
    create("common")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentFabric").extendsFrom(configurations["common"])
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.withType<ProcessResources>().configureEach {
    filesMatching(listOf("fabric.mod.json")) {
        expand(
            "version" to (project.findProperty("mod_version") ?: "1.0.0"),
            "mod_version" to (project.findProperty("mod_version") ?: "1.0.0")
        )
    }
}

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.14.24")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.77.0+1.18.2")
    modImplementation("dev.architectury:architectury-fabric:4.10.88")
    "common"(project(":1.18.2_common")) { isTransitive = false }
    shadow(project(":1.18.2_common")) { isTransitive = false }
}

val java21 = javaToolchains.launcherFor { languageVersion.set(JavaLanguageVersion.of(21)) }
tasks.named<JavaExec>("runClient") { javaLauncher.set(java21) }
