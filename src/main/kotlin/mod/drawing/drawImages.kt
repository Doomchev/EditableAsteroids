package mod.drawing

import blankImage
import distToScreen
import drawDashedRectangle
import fpsk
import imageArrays
import mod.actions.sprite.currentImageArray
import mod.Drawing
import xToScreen
import yToScreen
import java.awt.Graphics2D

object drawImages: Drawing {
  var frame: Double = 0.0

  override fun draw(g: Graphics2D) {
    val quantity = imageArrays.size
    val fsize = 1.0 / 1.2
    var fx = -0.5 * fsize * quantity
    val fy = -0.5 * 1.2
    frame += 8 * fpsk
    for(array in imageArrays) {
      val image = array.images[frame.toInt() % array.images.size]
      val swidth = distToScreen(fsize)
      val sheight = if(image.width != 0) swidth * image.height / image.width else swidth
      val sx = xToScreen(fx)
      val sy = yToScreen(fy) + (swidth - sheight) / 2
      if(image != blankImage) {
        image.draw(g, sx, sy, swidth, sheight, 0.0, true)
      }
      if(currentImageArray == array) {
        drawDashedRectangle(g, fx, fy, fsize, fsize, 4f)
      }
      fx += fsize
    }
  }
}