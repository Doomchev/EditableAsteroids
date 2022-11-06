package mod.actions

import Action
import Image
import ImageArray
import imageArrays
import mod.dragging.selectedShapes
import kotlin.math.floor

var currentImageArray:ImageArray? = null
object selectImage: Action {
  override fun execute(x: Double, y: Double) {
    var fsize = 1.0 / 1.2
    val imageNum = floor(x / fsize + 0.5 * imageArrays.size).toInt()
    if(imageNum < 0 || imageNum >= imageArrays.size) return
    currentImageArray = imageArrays[imageNum]
    for(shape in selectedShapes) {
      shape.image = currentImageArray!!.images[0]
    }
  }
}