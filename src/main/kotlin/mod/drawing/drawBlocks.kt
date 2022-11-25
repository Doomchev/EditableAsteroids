package mod.drawing

import Block
import blocks
import currentCanvas
import mod.dragging.Drawing
import mousesy
import java.awt.Color
import java.awt.Graphics2D

var selectedBlock: Block? = null

object drawBlocks: Drawing {
  override fun draw(g: Graphics2D) {
    g.color = Color.white
    selectedBlock = null
    var y = 0
    for(block in blocks) {
      if(mousesy >= y && mousesy < y + 16) {
        g.color = Color.GRAY
        g.fillRect(0, y, currentCanvas.viewport.width, 16)
        g.color = Color.white
        selectedBlock = block
      }
      g.drawString(block.message, 0, y + 12)
      y += 16
    }
    g.color = Color.black
  }
}
