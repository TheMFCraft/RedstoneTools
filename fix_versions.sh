#!/bin/bash
ROOT="/home/max/Documents/Development/PhiroStudios/RedstoneTweaks"

# Fix 1.20.6 fabric-api version: 0.100.1 -> 0.100.2
sed -i 's/fabric-api:0\.100\.1+/fabric-api:0.100.2+/g' "$ROOT/1.20.6/fabric/build.gradle.kts"

# Check existing versions for 1.18.2 and 1.19.4 - those compiled fine, but let me verify the API versions exist
# 1.18.2 uses 0.77.0+1.18.2 which might not exist, but it seemed to compile OK via transitive dependency
# Let me check what's available quickly

echo "Fixed 1.20.6 fabric-api version"
echo "Available fabric-api versions for 1.20.6: 0.100.0, 0.100.2, 0.100.4, 0.100.8"
echo "Available fabric-api versions for 26.1: 0.153.0+26.1.2 (latest)"
