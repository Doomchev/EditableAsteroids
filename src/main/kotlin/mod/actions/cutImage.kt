package mod.actions

import Action
import Image
import mod.dragging.enterInt

object cutImage: Action {
  override fun execute(x: Double, y: Double) {
    val xquantity = enterInt("Введите кол-во изображений по горизонтали:")
    val yquantity = enterInt("Введите кол-во изображений по вертикали:")
    val texture = currentImageArray!!.images[0].texture
    val width = texture.width / xquantity
    val height = texture.height / yquantity
    currentImageArray!!.images = Array(xquantity * yquantity) {
      Image(texture, (it % xquantity) * width, (it / xquantity) * height, width, height)
    }
  }
}