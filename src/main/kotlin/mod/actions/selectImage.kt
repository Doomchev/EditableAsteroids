package mod.actions

import Action
import imags
import mod.dragging.selectedShapes
import mod.dragging.shapes
import java.awt.image.BufferedImage
import kotlin.math.floor

var currentImage:BufferedImage? = null
object selectImage: Action {
  override fun execute(x: Double, y: Double) {
    var fsize = 1.0 / 1.2
    val imageNum = floor(x / fsize + 0.5 * imags.size).toInt()
    if(imageNum < 0 || imageNum >= imags.size) return
    currentImage = imags[imageNum]
    for(shape in selectedShapes) {
      shape.image = currentImage
    }
  }
}