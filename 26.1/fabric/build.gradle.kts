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
    minecraft("com.mojang:minecraft:26.1.2")
    modImplementation("net.fabricmc:fabric-loader:0.19.3")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.153.0+26.1.2")
    modImplementation("dev.architectury:architectury-fabric:20.0.7")
    "common"(project(":26.1_common")) { isTransitive = false }
    shadow(project(":26.1_common")) { isTransitive = false }
}
