package mod.dragging

import Action
import mousefx
import mousefy
import shapeUnderCursor

object selectSprite: Action {
  override fun execute() {
    if(shapeUnderCursor(selectedSprites, mousefx, mousefy) != null) {
      return
    }
    selectedSprites.clear()
    val shape = shapeUnderCursor(sprites, mousefx, mousefy)
    if(shape != null) {
      selectedSprites.add(shape)
    }
  }
}