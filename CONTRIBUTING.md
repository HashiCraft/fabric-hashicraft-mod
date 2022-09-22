# Contributing to HashiCraft Mod

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup)
that relates to the IDE that you are using.

## License

This template is available under the CC0 license. Feel free to
learn from it and incorporate it in your own projects.

## Running the World Locally

### Prerequisites

- `world.zip` file from the developer advocate team
- Minecraft Launcher
- Minecraft for Java Edition license
- [Docker for Desktop](https://www.docker.com/products/docker-desktop/)
- [Shipyard](https://shipyard.run/docs/install)

### Setup

1. Download `hashiconf-global-world.zip` and `hashiconf-global-mods.zip` from
  [Releases](https://github.com/HashiCraft/fabric-hashicraft-mod/releases)

1. Extract the `world` directory from `hashiconf-global-world.zip`.

1. Extract the JAR files from `hashiconf-global-mods.zip`. There should be three:
   - `fabric-api-*.jar`
   - `fabric-hashicraft-mod-*.jar`
   - `projector-*.jar`

1. Open your default directory for Minecraft.
   - Windows: `%APPDATA%\.minecraft`
   - MacOS: `~/Library/Application Support/minecraft`
   - Linux: `~/.minecraft`

1. Open the `saves` folder.

1. Copy the `world` folder to the `saves` folder. It should result in the
   following:
   ```shell
   $ ls saves
   world/
   ```

1. Open the `mods` folder.

1. Copy the JAR files from `hashiconf-global-mods.zip` into the mods folder.

1. Open Minecraft Launcher.

1. Go to Minecraft Java Edition (preferable).

1. The play screen should have a dropdown menu with various Minecraft versions.
   Choose the`fabric-loader` package listed.
   ![fabric loader mod for Minecraft Java edition](assets/instructions/fabric.png)

1. Select **PLAY**.

1. This will open Minecraft for Java. When you go into **SINGLEPLAYER** mode,
   you should see the pre-loaded world available to you.