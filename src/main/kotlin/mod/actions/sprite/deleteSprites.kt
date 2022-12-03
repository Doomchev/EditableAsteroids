package mod.actions.sprite

import Action
import mod.project
import mod.selectedSprites

object deleteSprites: Action {
  override fun execute() {
    for(sprite in selectedSprites) {
      project.remove(sprite)
    }
    selectedSprites.clear()
  }
}