# HashiCraft Mod for Minecraft

VERSION: Minecraft 1.20.1, mod 1.4.0

Contains custom blocks that allow interaction with the following HashiCorp products
* Nomad
* Consul
* Vault

## Prerequisites

* Java 17 JDK
* VSCode Microsoft Java Extension Pack

```
brew install openjdk@17
```

* VSCode Settings

Example VSCode Settings for Java 17 JDK on Windows:
```json
{
  "java.jdt.ls.java.home": "C:\\Program Files\\Microsoft\\jdk-17.0.19.10-hotspot",
  "java.import.gradle.java.home": "C:\\Program Files\\Microsoft\\jdk-17.0.19.10-hotspot"
}
```

Example VSCode Settings for Java 17 JDK on Mac:
```json
{
  "java.jdt.ls.java.home": "/opt/homebrew/opt/openjdk@17",
  "java.import.gradle.java.home": "/opt/homebrew/opt/openjdk@17"
}
```

After setup clear the language server settings, `ctrl+p` and run `>Java: Clear Java Language Server Workspace`.

## Running the Mod
* Run the gradle task `ide > vscode` to generate the VSCode project files and to
  enable debugging in VSCode
* Run the gradle task `fabric > runClient` to run the mod in a Minecraft client