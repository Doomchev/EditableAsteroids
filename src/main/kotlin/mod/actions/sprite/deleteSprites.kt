package mod.actions.sprite

import Action
import mod.dragging.project
import mod.dragging.selectedSprites

object deleteSprites: Action {
  override fun execute() {
    for(sprite in selectedSprites) {
      project.remove(sprite)
    }
    selectedSprites.clear()
  }
}