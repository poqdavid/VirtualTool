# VirtualTool [![GitHub stars](https://img.shields.io/github/stars/poqdavid/VirtualTool.svg)](https://github.com/poqdavid/VirtualTool/stargazers) [![GitHub issues](https://img.shields.io/github/issues/poqdavid/VirtualTool.svg)](https://github.com/poqdavid/VirtualTool/issues) [![Build Status](https://travis-ci.org/poqdavid/VirtualTool.svg?branch=1.12.2)](https://travis-ci.org/poqdavid/VirtualTool)
A plugin for minecraft to provide virtual gui for all possible items.

## Features
&nbsp; &nbsp; ☑ Virtual Anvil </br>
&nbsp; &nbsp; ☑ Virtual EnderChest </br>
&nbsp; &nbsp; ☑ Virtual EnchantingTable </br>
&nbsp; &nbsp; ☑ Virtual Workbench </br>
&nbsp; &nbsp; ☑ Virtual Backpack

## Commands
    /anvil
    /enderchest, /ec
    /enchantingtable, /et <power 0-15>
    /workbench, /wb
	/backpack, /bp  [[<player>]] [<size>] [-m <m>]
	/backpacklock, /bplock  <player> [-l <l>] [-u <u>]

## Premissions
    VirtualTool.command.anvil
    VirtualTool.command.help
    VirtualTool.command.main
    VirtualTool.command.enderchest
    VirtualTool.command.enchantingtable
    VirtualTool.command.workbench
    VirtualTool.command.backpack
    VirtualTool.command.backpacklock
    VirtualTool.command.backpackadminread
    VirtualTool.command.backpackadminmodify
    VirtualTool.command.backpacksize.one
    VirtualTool.command.backpacksize.two
    VirtualTool.command.backpacksize.three
    VirtualTool.command.backpacksize.four
    VirtualTool.command.backpacksize.five
    VirtualTool.command.backpacksize.six
    VirtualTool.command.enchantingtablepower.0
    VirtualTool.command.enchantingtablepower.1
    VirtualTool.command.enchantingtablepower.2
    VirtualTool.command.enchantingtablepower.3
    VirtualTool.command.enchantingtablepower.4
    VirtualTool.command.enchantingtablepower.5
    VirtualTool.command.enchantingtablepower.6
    VirtualTool.command.enchantingtablepower.7
    VirtualTool.command.enchantingtablepower.8
    VirtualTool.command.enchantingtablepower.9
    VirtualTool.command.enchantingtablepower.10
    VirtualTool.command.enchantingtablepower.11
    VirtualTool.command.enchantingtablepower.12
    VirtualTool.command.enchantingtablepower.13
    VirtualTool.command.enchantingtablepower.14
    VirtualTool.command.enchantingtablepower.15
	
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
Special Thanks to [**PocketPixels**](http://pocketpixels.net/) for the help with building this plugin.