package mod.drawing

import distToScreen
import drawDashedRectangle
import imageArrays
import mod.actions.sprite.currentImageArray
import mod.dragging.Drawing
import xToScreen
import yToScreen
import java.awt.Graphics2D

object drawImages: Drawing {
  override fun draw(g: Graphics2D) {
    val quantity = imageArrays.size
    val fsize = 1.0 / 1.2
    var fx = -0.5 * fsize * quantity
    val fy = -0.5 * 1.2
    for(array in imageArrays) {
      val image = array.images[0]
      val sx = xToScreen(fx)
      val sy = yToScreen(fy)
      val ssize = distToScreen(fsize)
      image.draw(g, sx, sy, ssize, ssize, 0.0)
      if(currentImageArray == array) {
        drawDashedRectangle(g, fx, fy, fsize, fsize)
      }
      fx += fsize
    }
  }
}