package mod.actions.sprite

import Action
import mod.dragging.selectedSprites
import mod.dragging.sprites

object deleteSprites: Action {
  override fun execute(x: Double, y: Double) {
    sprites.removeAll(selectedSprites)
    selectedSprites.clear()
  }
}