package mod.actions.tilemap

import TileMap
import mod.actions.sprite.currentImageArray
import mod.dragging.enterDouble
import mod.dragging.enterInt

fun createTileMap(): TileMap {
  return TileMap(enterInt("Введите кол-во столбцов:"), enterInt("Введите кол-во строк:"), enterDouble("Введите ширину клетки:").getDouble(), enterDouble("Введите высоту клетки:").getDouble(), currentImageArray!!)
}