package mod.drawing

import mod.Drawing
import mod.project
import java.awt.Graphics2D

object drawScene: Drawing {
  override fun draw(g: Graphics2D) {
    project.draw(g)
  }
}