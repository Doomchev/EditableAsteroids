package mod.actions

import Action
import Image
import ImageArray
import mod.actions.sprite.currentImageArray
import mod.dragging.enterInt

object cutImage: Action {
  override fun execute() {
    val xquantity = enterInt("Введите кол-во изображений по горизонтали:")
    val yquantity = enterInt("Введите кол-во изображений по вертикали:")
    cutSprite(currentImageArray!!, xquantity, yquantity)
  }
}

fun cutSprite(array: ImageArray, xquantity: Int, yquantity: Int) {
  val texture = array.images[0].texture
  val width = texture.width / xquantity
  val height = texture.height / yquantity
  array.images = Array(xquantity * yquantity) {
    Image(texture, (it % xquantity) * width, (it / xquantity) * height, width, height)
  }
}