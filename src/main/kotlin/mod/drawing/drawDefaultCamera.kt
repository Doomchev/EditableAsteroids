package mod.drawing

import mod.dragging.Drawing
import world
import java.awt.Graphics2D

object drawDefaultCamera: Drawing {
  override fun draw(g: Graphics2D) {
    world.drawDefaultCamera(g)
  }
}