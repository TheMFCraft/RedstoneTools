#!/bin/bash
ROOT="/home/max/Documents/Development/PhiroStudios/RedstoneTweaks"

# Helper: write common build file
write_common() {
  local dir="$1" mc="$2" yarn="$3" loader="$4" api="$5" target_java="$6" extra_deps="$7"
  cat > "$dir/common/build.gradle.kts" << EOF
plugins {
    java
    id("dev.architectury.loom")
    id("architectury-plugin")
}

group = "phiro.redstonetweaks"
version = rootProject.findProperty("mod_version") ?: "1.0.0"

java {
    withSourcesJar()
    sourceCompatibility = JavaVersion.toVersion($target_java)
    targetCompatibility = JavaVersion.toVersion($target_java)
}

architectury {
    common($extra_deps)
}

loom {
    silentMojangMappingsLicense()
}

sourceSets {
    main {
        java {
            srcDir(rootProject.file("shared-core/src/main/java"))
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set($target_java)
}

dependencies {
    minecraft("com.mojang:minecraft:$mc")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loader")
    modImplementation("dev.architectury:architectury:$api")
}
EOF
}

# Helper: write fabric build file
write_fabric() {
  local dir="$1" mc="$2" loader="$3" fabric_api="$4" arch_api="$5" target_java="$6" common_ref="$7"
  cat > "$dir/fabric/build.gradle.kts" << EOF
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
    sourceCompatibility = JavaVersion.toVersion($target_java)
    targetCompatibility = JavaVersion.toVersion($target_java)
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
    options.release.set($target_java)
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
    minecraft("com.mojang:minecraft:$mc")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loader")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabric_api")
    modImplementation("dev.architectury:architectury-fabric:$arch_api")
    "common"(project(":${common_ref}")) { isTransitive = false }
    shadow(project(":${common_ref}")) { isTransitive = false }
}
EOF
}

# Helper: write neoforge build file
write_neoforge() {
  local dir="$1" mc="$2" neoforge="$3" arch_api="$4" target_java="$5" common_ref="$6"
  cat > "$dir/neoforge/build.gradle.kts" << EOF
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
    sourceCompatibility = JavaVersion.toVersion($target_java)
    targetCompatibility = JavaVersion.toVersion($target_java)
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    silentMojangMappingsLicense()
}

configurations {
    create("common")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set($target_java)
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
    minecraft("com.mojang:minecraft:$mc")
    mappings(loom.officialMojangMappings())
    "neoForge"("net.neoforged:neoforge:$neoforge")
    modImplementation("dev.architectury:architectury-neoforge:$arch_api")
    "common"(project(":${common_ref}")) { isTransitive = false }
    shadow(project(":${common_ref}")) { isTransitive = false }
}
EOF
}

# ==============================
# 1.18.2 (Java 17, Fabric only)
# ==============================
write_common "$ROOT/1.18.2" "1.18.2" "" "0.14.24" "4.10.88" 17 '"fabric"'
write_fabric "$ROOT/1.18.2" "1.18.2" "0.14.24" "0.77.0+1.18.2" "4.10.88" 17 "1.18.2_common"

# ==============================
# 1.19.4 (Java 17, Fabric only)
# ==============================
write_common "$ROOT/1.19.4" "1.19.4" "" "0.15.11" "8.2.91" 17 '"fabric"'
write_fabric "$ROOT/1.19.4" "1.19.4" "0.15.11" "0.87.1+1.19.4" "8.2.91" 17 "1.19.4_common"

# ==============================
# 1.20.6 (Java 21, Fabric + NeoForge)
# ==============================
write_common "$ROOT/1.20.6" "1.20.6" "" "0.16.10" "12.1.4" 21 '"fabric", "neoforge"'
write_fabric "$ROOT/1.20.6" "1.20.6" "0.16.10" "0.100.1+1.20.6" "12.1.4" 21 "1.20.6_common"
write_neoforge "$ROOT/1.20.6" "1.20.6" "1.20.6-53.0.39" "12.1.4" 21 "1.20.6_common"

# ==============================
# 1.21.5 (Java 21, Fabric + NeoForge)
# ==============================
write_common "$ROOT/1.21.5" "1.21.5" "" "0.16.10" "15.5.1" 21 '"fabric", "neoforge"'
write_fabric "$ROOT/1.21.5" "1.21.5" "0.16.10" "0.119.3+1.21.5" "15.5.1" 21 "1.21.5_common"
write_neoforge "$ROOT/1.21.5" "1.21.5" "1.21.5-55.0.0" "15.5.1" 21 "1.21.5_common"

# ==============================
# 26.1 (Java 25, Fabric + NeoForge, no-remap loom)
# ==============================
# 26.1 common uses architectury.loom-no-remap
cat > "$ROOT/26.1/common/build.gradle.kts" << 'EOF'
plugins {
    java
    id("dev.architectury.loom-no-remap")
    id("architectury-plugin")
}

group = "phiro.redstonetweaks"
version = rootProject.findProperty("mod_version") ?: "1.0.0"

java {
    withSourcesJar()
}

architectury {
    common("fabric", "neoforge")
}

loom {
    silentMojangMappingsLicense()
}

sourceSets {
    main {
        java {
            srcDir(rootProject.file("shared-core/src/main/java"))
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

dependencies {
    minecraft("com.mojang:minecraft:26.1.2")
    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("dev.architectury:architectury:19.0.1")
}
EOF

# 26.1 fabric
cat > "$ROOT/26.1/fabric/build.gradle.kts" << 'EOF'
plugins {
    java
    id("dev.architectury.loom-no-remap")
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
    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("dev.architectury:architectury-fabric:19.0.1")
    "common"(project(":26.1_common")) { isTransitive = false }
    shadow(project(":26.1_common")) { isTransitive = false }
}
EOF

# 26.1 neoforge
cat > "$ROOT/26.1/neoforge/build.gradle.kts" << 'EOF'
plugins {
    java
    id("dev.architectury.loom-no-remap")
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
    neoForge()
}

loom {
    silentMojangMappingsLicense()
}

configurations {
    create("common")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
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
    minecraft("com.mojang:minecraft:26.1.2")
    "neoForge"("net.neoforged:neoforge:26.1.2-0.0.1")
    modImplementation("dev.architectury:architectury-neoforge:19.0.1")
    "common"(project(":26.1_common")) { isTransitive = false }
    shadow(project(":26.1_common")) { isTransitive = false }
}
EOF

echo "All build files regenerated!"
