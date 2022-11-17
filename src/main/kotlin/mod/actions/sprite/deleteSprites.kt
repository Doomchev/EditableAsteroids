package mod.actions.sprite

import Action
import mod.dragging.scene
import mod.dragging.selectedSprites

object deleteSprites: Action {
  override fun execute() {
    for(sprite in selectedSprites) {
      scene.remove(sprite)
    }
    selectedSprites.clear()
  }
}