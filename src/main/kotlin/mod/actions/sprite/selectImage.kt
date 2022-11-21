package mod.actions.sprite

import Action
import ImageArray
import imageArrays
import mod.dragging.selectedSprites
import mousefx
import kotlin.math.floor

var currentImageArray:ImageArray? = null
object selectImage: Action {
  override fun execute() {
    var fsize = 1.0 / 1.2
    val imageNum = floor(mousefx / fsize + 0.5 * imageArrays.size).toInt()
    if(imageNum < 0 || imageNum >= imageArrays.size) return
    currentImageArray = imageArrays[imageNum]
    for(sprite in selectedSprites) {
      sprite.image = currentImageArray!!.images[0]
    }
  }
}