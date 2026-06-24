plugins {
    java
    id("dev.architectury.loom")
    id("architectury-plugin")
}

group = "phiro.redstonetweaks"
version = rootProject.findProperty("mod_version") ?: "1.0.0"

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion(25)
    targetCompatibility = JavaVersion.toVersion(25)
}

architectury {
    common("fabric", "neoforge")
}

loom {
    silentMojangMappingsLicense()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
}

dependencies {
    minecraft("com.mojang:minecraft:26.1.2")
    modImplementation("net.fabricmc:fabric-loader:0.19.3")
    modImplementation("dev.architectury:architectury:20.0.7")
}
