package mod.drawing

import drawBlocks
import mod.dragging.Drawing
import mod.dragging.scene
import java.awt.Graphics2D

object drawScene: Drawing {
  override fun draw(g: Graphics2D) {
    scene.draw(g)
    drawBlocks(g)
  }
}