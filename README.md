# VirtualTool [![Build Status](https://travis-ci.org/poqdavid/VirtualTool.svg?branch=master)](https://travis-ci.org/poqdavid/VirtualTool)
A plugin for minecraft to provide virtual gui for all possible items.

##Features
- [ ] Virtual Anvil
- [ ] Virtual EnderChest
- [ ] Virtual EnchantingTable
- [ ] Virtual Workbench
- [x] Virtual Backpack

## Commands
    /anvil
    /enderchest, /ec
    /enchantingtable, /et
    /workbench, /wb
	/backpack  <player> [-m <m>], /bp  <player> [-m <m>]

## Premissions
    VirtualTool.command.anvil
    VirtualTool.command.help
    VirtualTool.command.main
    VirtualTool.command.enderchest
    VirtualTool.command.enchantingtable
    VirtualTool.command.workbench
    VirtualTool.command.backpack
    VirtualTool.command.backpackadminread
    VirtualTool.command.backpackadminmodify
    VirtualTool.command.backpacksize.one
    VirtualTool.command.backpacksize.two
    VirtualTool.command.backpacksize.three
    VirtualTool.command.backpacksize.four
    VirtualTool.command.backpacksize.five
    VirtualTool.command.backpacksize.six
	
## Configuration
**Config path:** (\config\virtualtool\config.json)
```json
{
  "commands": {
    "enable_enderchest": true,
    "enable_anvil": true,
    "enable_workbench": true,
    "enable_enchantingtable": true,
    "enable_backpack": true
  }
}
```
Special Thanks to **PocketPixels** for the help with building this plugin.