package mod.actions

import Action
import world

class restoreCamera: Action {
  override fun execute() {
    world.restorePosition()
  }
}