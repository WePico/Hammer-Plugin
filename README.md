# Hammer Plugin

The Hammer Plugin is a Bukkit plugin for adding hammer functionality to Minecraft. It allows players to break blocks in a range using a diamond pickaxe with the Hammer enchantment.

## Features

- Hammer enchantment: Players can enchant their diamond pickaxes with the Hammer enchantment to break blocks in a range.
- Graphical User Interface (GUI): Provides a GUI for enchanting tools and repairing pickaxes.
- Configurable options: Various configurable options such as blacklisted blocks, durability loss, warning messages, and logging.

## Prerequisites

- Java 17
- Spigot or Paper server

## Installation

1. Download the latest release from the [Releases](https://github.com/WePico/Hammer-Plugin/releases) page.
2. Place the JAR file into the `plugins` folder of your Spigot or CraftBukkit server.
3. Start or reload your server.

## Configuration

The configuration file (`config.yml`) can be found in the plugin's folder under `plugins/HammerPlugin`. You can modify the following options:

- `blacklistedBlocks`: A list of block types that cannot be broken with the hammer.
- `loseDurability`: Whether the hammer should lose durability when breaking blocks.
- `warningDurability`: The durability threshold at which a warning message is displayed to the player.
- `warningTitle`: The title of the warning message displayed to the player.
- `warningSubtitle`: The subtitle of the warning message displayed to the player.
- `loggingEnabled`: Whether logging is enabled or not.

## Usage

To use the Hammer Plugin, follow these steps:

1. Obtain a diamond pickaxe.
2. Enchant the diamond pickaxe with the Hammer enchantment.
3. Right-click while sneaking to open the enchantment GUI.
4. Use the GUI to enchant your tool or repair your pickaxe.

## Contributing

Contributions are welcome! If you have any bug reports, feature requests, or code improvements, please open an issue or submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
