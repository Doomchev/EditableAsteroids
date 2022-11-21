package mod.dragging

import Action
import mousefx
import mousefy
import spriteUnderCursor

object selectSprite: Action {
  override fun execute() {
    if(spriteUnderCursor(selectedSprites, mousefx, mousefy) != null) {
      return
    }
    selectedSprites.clear()
    val sprite = scene.spriteUnderCursor(mousefx, mousefy)
    if(sprite != null) {
      selectedSprites.add(sprite)
    }
  }
}