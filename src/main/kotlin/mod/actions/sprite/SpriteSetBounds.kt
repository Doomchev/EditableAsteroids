package mod.actions.sprite

import Sprite
import SpriteAction
import currentCanvas
import mod.dragging.enterDouble
import mod.dragging.scene

class SpriteSetBounds: SpriteAction() {
  private var bounds: Sprite = Sprite()

  override fun settings() {
    val border = enterDouble("Введите ширину бордюра (ед.):")
    bounds.centerX = currentCanvas.centerX
    bounds.centerY = currentCanvas.centerY
    bounds.width = currentCanvas.width + 2.0 * border
    bounds.height = currentCanvas.height + 2.0 * border
  }

  override fun create(sprite: Sprite?): SpriteAction {
    val action = SpriteSetBounds()
    action.sprite = sprite
    action.bounds = bounds
    return action
  }

  override fun execute() {
    if(sprite!!.centerX >= bounds.leftX && sprite!!.centerX < bounds.rightX && sprite!!.centerY >= bounds.topY && sprite!!.centerY < bounds.bottomY) return
    scene.remove(sprite!!)
  }

  override fun toString(): String = "Установить границы"
}