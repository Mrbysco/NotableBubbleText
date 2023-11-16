[![build](https://github.com/Mrbysco/NotableBubbleText/actions/workflows/build.yml/badge.svg)](https://github.com/Mrbysco/NotableBubbleText/actions/workflows/build.yml) 
[![](http://cf.way2muchnoise.eu/versions/nbt.svg)](https://www.curseforge.com/minecraft/mc-mods/nbt)

# Notable Bubble Text (NBT)#

## About ##
This mod adds text bubbles above player heads when they say something in chat. 

Using the `/bubbletext <author> <message>` command you can get a text bubble to render above a mob that has a custom nbt string tag with that author as the value (Perfect for integration mods like CCI). The default nbt key is "BubbleOwner" but can be changed in the config file.

An example would be `/summon zombie ~ ~2 ~ {ForgeData: {BubbleOwner: "Cranberries"}}` <br>
and then using `/bubbletext Cranberries Zombie, zombie, zombie-ie-ie` to make the zombie say "Zombie, zombie, zombie-ie-ie".

## License ##
* Notable Bubble Text is licensed under the MIT License
  - (c) 2023 Mrbysco
  - [![License](https://img.shields.io/badge/License-MIT-red.svg?style=flat)](http://opensource.org/licenses/MIT)

## Downloads ##
Downloads will be located on [CurseForge](https://www.curseforge.com/minecraft/mc-mods/nbt)
