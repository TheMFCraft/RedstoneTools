#!/bin/bash
set -e

ROOT="/home/max/Documents/Development/PhiroStudios/RedstoneTweaks"
SHARED_SRC="shared-core/src/main/java/phiro/redstonetweaks"

# ============================================================
# 1. COMMON MODULE BUILD FILES
# ============================================================

# 1.18 common
cat > "$ROOT/1.18.2/common/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
}

architectury {
    common("fabric")
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

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.32:v2")
    modImplementation("net.fabricmc:fabric-loader:0.14.24")
    modImplementation("dev.architectury:architectury:4.10.88")
}
EOF

# 1.19 common
cat > "$ROOT/1.19.4/common/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
}

architectury {
    common("fabric")
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

dependencies {
    minecraft("com.mojang:minecraft:1.19.4")
    mappings("net.fabricmc:yarn:1.19.4+build.2:v2")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("dev.architectury:architectury:8.2.91")
}
EOF

# 1.20 common
cat > "$ROOT/1.20.6/common/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
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

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("dev.architectury:architectury:12.1.4")
}
EOF

# 1.21 common
cat > "$ROOT/1.21.5/common/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
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

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("dev.architectury:architectury:15.5.1")
}
EOF

# 26.1 common - Uses loom-no-remap (unobfuscated)
cat > "$ROOT/26.1/common/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom-no-remap")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
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

dependencies {
    minecraft("com.mojang:minecraft:26.1.2")
    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("dev.architectury:architectury:19.0.1")
}
EOF

echo "Common build files done."

# ============================================================
# 2. FABRIC MODULE BUILD FILES
# ============================================================

# 1.18 fabric
cat > "$ROOT/1.18.2/fabric/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:1.18.2")
    mappings("net.fabricmc:yarn:1.18.2+build.32:v2")
    modImplementation("net.fabricmc:fabric-loader:0.14.24")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.77.0+1.18.2")
    modImplementation("dev.architectury:architectury-fabric:4.10.88")
    "common"(project(":1.18.2_common")) { isTransitive = false }
    shadow(project(":1.18.2_common")) { isTransitive = false }
}
EOF

# 1.19 fabric
cat > "$ROOT/1.19.4/fabric/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:1.19.4")
    mappings("net.fabricmc:yarn:1.19.4+build.2:v2")
    modImplementation("net.fabricmc:fabric-loader:0.15.11")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.87.1+1.19.4")
    modImplementation("dev.architectury:architectury-fabric:8.2.91")
    "common"(project(":1.19.4_common")) { isTransitive = false }
    shadow(project(":1.19.4_common")) { isTransitive = false }
}
EOF

# 1.20 fabric
cat > "$ROOT/1.20.6/fabric/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.100.1+1.20.6")
    modImplementation("dev.architectury:architectury-fabric:12.1.4")
    "common"(project(":1.20.6_common")) { isTransitive = false }
    shadow(project(":1.20.6_common")) { isTransitive = false }
}
EOF

# 1.21 fabric
cat > "$ROOT/1.21.5/fabric/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:0.16.10")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.119.3+1.21.5")
    modImplementation("dev.architectury:architectury-fabric:15.5.1")
    "common"(project(":1.21.5_common")) { isTransitive = false }
    shadow(project(":1.21.5_common")) { isTransitive = false }
}
EOF

# 26.1 fabric
cat > "$ROOT/26.1/fabric/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom-no-remap")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:26.1.2")
    modImplementation("net.fabricmc:fabric-loader:0.18.4")
    modImplementation("net.fabricmc.fabric-api:fabric-api:0.120.0+26.1")
    modImplementation("dev.architectury:architectury-fabric:19.0.1")
    "common"(project(":26.1_common")) { isTransitive = false }
    shadow(project(":26.1_common")) { isTransitive = false }
}
EOF

echo "Fabric build files done."

# ============================================================
# 3. NEOFORGE MODULE BUILD FILES
# ============================================================

# 1.20 neoforge
cat > "$ROOT/1.20.6/neoforge/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:1.20.6")
    mappings(loom.officialMojangMappings())
    "neoForge"("net.neoforged:neoforge:1.20.6-53.0.39")
    modImplementation("dev.architectury:architectury-neoforge:12.1.4")
    "common"(project(":1.20.6_common")) { isTransitive = false }
    shadow(project(":1.20.6_common")) { isTransitive = false }
}
EOF

# 1.21 neoforge
cat > "$ROOT/1.21.5/neoforge/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:1.21.5")
    mappings(loom.officialMojangMappings())
    "neoForge"("net.neoforged:neoforge:1.21.5-55.0.0")
    modImplementation("dev.architectury:architectury-neoforge:15.5.1")
    "common"(project(":1.21.5_common")) { isTransitive = false }
    shadow(project(":1.21.5_common")) { isTransitive = false }
}
EOF

# 26.1 neoforge
cat > "$ROOT/26.1/neoforge/build.gradle.kts" << 'EOF'
plugins {
    id("dev.architectury.loom-no-remap")
    id("architectury-plugin")
    id("redstonetweaks.conventions")
    id("com.gradleup.shadow")
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

dependencies {
    minecraft("com.mojang:minecraft:26.1.2")
    "neoForge"("net.neoforged:neoforge:26.1.2-0.0.1")
    modImplementation("dev.architectury:architectury-neoforge:19.0.1")
    "common"(project(":26.1_common")) { isTransitive = false }
    shadow(project(":26.1_common")) { isTransitive = false }
}
EOF

echo "NeoForge build files done."
echo "All build files generated successfully."
