package mod.actions

import Image
import ImageArray

fun splitImage(array: ImageArray, xquantity: Int, yquantity: Int) {
  val texture = array.images[0].texture
  val width = texture.width / xquantity
  val height = texture.height / yquantity
  array.images = Array(xquantity * yquantity) {
    Image(texture, (it % xquantity) * width, (it / xquantity) * height, width, height)
  }
}