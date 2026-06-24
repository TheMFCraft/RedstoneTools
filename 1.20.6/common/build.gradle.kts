plugins {
    java
    id("dev.architectury.loom")
    id("architectury-plugin")
}

group = "phiro.redstonetweaks"
version = rootProject.findProperty("mod_version") ?: "1.0.0"

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(21)
    targetCompatibility = JavaVersion.toVersion(21)
}

architectury {
    common("fabric", "neoforge")
}

loom {
    silentMojangMappingsLicense()
}

tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    targetNamespace = "named"
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("dev.architectury:architectury:12.1.4")
}
