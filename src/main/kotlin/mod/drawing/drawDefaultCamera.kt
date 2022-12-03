package mod.drawing

import mod.Drawing
import world
import java.awt.Graphics2D

object drawDefaultCamera: Drawing {
  override fun draw(g: Graphics2D) {
    world.drawDefaultCamera(g)
  }
}