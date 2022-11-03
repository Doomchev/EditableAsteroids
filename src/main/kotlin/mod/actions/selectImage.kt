package mod.actions

import Action
import imags
import mod.dragging.selectedShapes
import mod.dragging.shapes
import kotlin.math.floor

object selectImage: Action {
  override fun execute(x: Int, y: Int) {
    val imageNum = floor(x / 64.0).toInt()
    if(imageNum < 0 || imageNum >= imags.size) return
    val image = imags[imageNum]
    for(shape in selectedShapes) {
      shape.image = image
    }
  }
}