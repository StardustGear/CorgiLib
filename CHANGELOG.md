## 1.0.0.32
* Use delegate's `listBuilder` & `mapBuilder` in `FromFileOps`.

## 1.0.0.31
* Better checks and exceptions for `TreeFromStructureNBTFeature`.

## 1.0.0.30
* Add to leave positions even if the leaves in question do not have a distance state property in `TreeFromStructureNBTFeature`.

## 1.0.0.29
* Don't use replaceable check when filling logs, check if the block at the position cannot occlude instead in `TreeFromStructureNBTFeature`.

## 1.0.0.28
* Don't use ground filter when filling logs, check if the block at the position is replaceable instead in `TreeFromStructureNBTFeature`.

## 1.0.0.27
* Fix additional blocks placed from NBT positions in `TreeFromStructureNBTFeature`. 

## 1.0.0.26
* Only post process if the placed state has distance property in `TreeFromStructureNBTFeature`.

## 1.0.0.25
* Allow the ability to place additional blocks from the NBT in `TreeFromStructureNBTFeature`.

## 1.0.0.24
* Don't fill logs under for Yellow wool in `TreeFromStructureNBTFeature`.

## 1.0.0.23
* Change chunk storage format to our own compound tag to easily isolate our chunk tag data.

## 1.0.0.22
* Use moving position when checking for ground instead of constantly checking the same position.
* Don't use heightmap to determine if the trunk is on the ground.

## 1.0.0.21
* Water log leaves placed in water by `TreeFromStructureNBTFeature`.

## 1.0.0.20
* Actually add scheduled random ticks to `LevelChunk` from `ProtoChunk`

## 1.0.0.19
* Don't random tick in `LevelChunk` constructor.

## 1.0.0.18
* Add schedule random ticks system to update grass blocks under logs.

## 1.0.0.17
* Try and fix ticking dirt blocks under filled logs.

## 1.0.0.16
* Invert check for ground when filling logs under for trees.

## 1.0.0.15
* Use BlockPredicates to determine whether we've hit ground and to determine whether leaves can place at a position.

## 1.0.0.14
* Get logs from the trunk palette correctly.
* Store leaves & log targets in a ObjectOpenHashSet in `TreeFromStructureNBTFeature`.

## 1.0.0.13
* Add the ability to have several leaves & log targets in `TreeFromStructureNBTFeature`.

## 1.0.0.12
* Fix StructureBoxEditor

## 1.0.0.11
* Add ability to edit structure boxes with a golden axe. Use LEFT_CTRL + SCROLL_WHEEL when to move the box in that direction, use LEFT_SHIFT + SCROLL_WHEEL to inflate the box in that direction.

## 1.0.0.10
* Place leaves on trunks.

## 1.0.0.9
* Fix canopy anchor pos.

## 1.0.0.8
* Add the ability to use yellow wool to anchor canopies from trunks.

## 1.0.0.7
* Switch to Access Transformers/Wideners.

## 1.0.0.6
* Use correct `/corgilib worldregistryexport` command data export path.
* Fix `RegistryAccessor` mixin being called exclusively on clients. Fixes servers crashing.

## 1.0.0.5
* Fix & optimize `/corgilib worldRegistryExport` command.

## 1.0.0.4
* Register `AnyCondition` condition.
* Better registry ID for `IsMobCategoryCondition`.

## 1.0.0.3
* Prevent duplicate initializations on fabric.

## 1.0.0.2
* Relocate Jankson on forge build.

## 1.0.0.1
* Allow CorgiLib Fabric to be initialized from elsewhere.
* Clean up network package.

## 1.0.0
* First Release.