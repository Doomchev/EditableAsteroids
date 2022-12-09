package mod.actions

import Image
import ImageArray

fun splitImage(array: ImageArray, xquantity: Int, yquantity: Int) {
  val texture = array.images[0].texture
  val width = texture.image.width / xquantity
  val height = texture.image.height / yquantity
  array.images = Array(xquantity * yquantity) {Image(texture, (it % xquantity) * width, (it / xquantity) * height, width, height)
  }
}