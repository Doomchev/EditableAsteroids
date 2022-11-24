package mod.drawing

import blocks
import mod.dragging.Drawing
import java.awt.Color
import java.awt.Graphics2D

object drawBlocks: Drawing {
  override fun draw(g: Graphics2D) {
    g.color = Color.white
    var y = 16
    for(block in blocks) {
      g.drawString(block.message, 0, y)
      y += 16
    }
    g.color = Color.black
  }
}
