package mod.actions

import Action
import Image
import ImageArray

object cutImage: Action {
  override fun execute() {
  }

  override fun toString(): String = "Разрезать"
}

fun cutImage(array: ImageArray, xquantity: Int, yquantity: Int) {
  val texture = array.images[0].texture
  val width = texture.width / xquantity
  val height = texture.height / yquantity
  array.images = Array(xquantity * yquantity) {
    Image(texture, (it % xquantity) * width, (it / xquantity) * height, width, height)
  }
}