package mod.drawing

import currentCanvas
import imags
import mod.dragging.Drawing
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.util.*

object drawImages: Drawing {
  override fun draw(g2d: Graphics2D) {
    var sx = currentCanvas.viewport.leftX
    var sy = currentCanvas.viewport.topY
    for(image in imags) {
      g2d.drawImage(image, sx, sy, 64, 64, null)
      sx += 64
    }
  }
}