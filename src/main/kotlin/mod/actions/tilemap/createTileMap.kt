package mod.actions.tilemap

import ImageArray
import TileMap
import mod.actions.sprite.currentImageArray
import mod.dragging.enterDouble
import mod.dragging.enterInt

fun createTileMap(): TileMap {
  return TileMap(enterInt("Введите кол-во столбцов:"), enterInt("Введите кол-во строк:"), enterDouble("Введите ширину клетки:").get(), enterDouble("Введите высоту клетки:").get(), currentImageArray!!)
}