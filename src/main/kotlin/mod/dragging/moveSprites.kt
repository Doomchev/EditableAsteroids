package mod.dragging

import DraggingAction
import mod.project
import mod.selectedSprites
import mousefx
import mousefy
import spriteUnderCursor
import snapX
import snapY
import java.awt.Graphics2D

object moveSprites: DraggingAction {
  var oldx: Double = 0.0
  var oldy: Double = 0.0

  override fun conditions(): Boolean {
    if(spriteUnderCursor(selectedSprites, mousefx, mousefy) != null) {
      return true
    }
    selectedSprites.clear()
    val sprite = project.spriteUnderCursor(mousefx, mousefy)
    if(sprite != null) {
      selectedSprites.add(sprite)
      return true
    }
    return false
  }

  override fun pressed() {
    oldx = snapX(mousefx)
    oldy = snapY(mousefy)
  }

  override fun dragged() {
    val fx = snapX(mousefx)
    val fy = snapY(mousefy)
    val dx = fx - oldx
    val dy = fy - oldy
    for(sprite in selectedSprites) {
      sprite.leftX += dx
      sprite.topY += dy
    }
    oldx = fx
    oldy = fy
  }

  override fun released() {
  }

  override fun drawWhileDragging(g: Graphics2D) {
  }
}