package mod.actions

import Action
import Sprite
import currentCanvas
import mod.dragging.enterDouble
import mod.dragging.sprites

object setBounds: Action {
  val bounds: Sprite = Sprite()

  override fun settings() {
    val border = enterDouble("Введите ширину бордюра (ед.):")
    bounds.centerX = currentCanvas.centerX
    bounds.centerY = currentCanvas.centerY
    bounds.width = currentCanvas.width + 2.0 * border
    bounds.height = currentCanvas.height + 2.0 * border
  }

  override fun execute() {
    for(sprite in sprites) {
      if(sprite.centerX < bounds.leftX) sprite.centerX += bounds.width
      if(sprite.centerX >= bounds.rightX) sprite.centerX -= bounds.width
      if(sprite.centerY < bounds.topY) sprite.centerY += bounds.height
      if(sprite.centerY >= bounds.bottomY) sprite.centerY -= bounds.height
    }
  }
}