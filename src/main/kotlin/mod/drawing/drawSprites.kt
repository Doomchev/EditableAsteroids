package mod.drawing

import mod.dragging.Drawing
import mod.dragging.sprites
import java.awt.Graphics2D

object drawSprites: Drawing {
  override fun draw(g: Graphics2D) {
    for(sprite in sprites) {
      sprite.draw(g)
    }
  }
}