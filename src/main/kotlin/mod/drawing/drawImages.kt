package mod.drawing

import distToScreen
import drawDashedRectangle
import imags
import mod.actions.currentImage
import mod.dragging.Drawing
import xToScreen
import yToScreen
import java.awt.Graphics
import java.awt.Graphics2D
import java.util.*

object drawImages: Drawing {
  override fun draw(g: Graphics2D) {
    var quantity = imags.size
    var fsize = 1.0 / 1.2
    var fx = -0.5 * fsize * quantity
    var fy = -0.5 * 1.2
    for(image in imags) {
      val sx = xToScreen(fx)
      val sy = yToScreen(fy)
      val ssize = distToScreen(fsize)
      g.drawImage(image, sx, sy, ssize, ssize, null)
      if(currentImage == image) {
        drawDashedRectangle(g, fx, fy, fsize, fsize)
      }
      fx += fsize
    }
  }
}