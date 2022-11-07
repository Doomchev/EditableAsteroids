package mod.actions

import Action
import frame
import mod.dragging.selectedSprites
import xToScreen
import yToScreen
import javax.swing.JPopupMenu

class showMenu(val menu: JPopupMenu, val forShape: Boolean): Action {
  override fun conditions(x: Double, y: Double): Boolean {
    return !forShape || !selectedSprites.isEmpty() && !menu.isVisible
  }

  override fun execute(x: Double, y: Double) {
    menu.show(frame, xToScreen(x), yToScreen(y));
  }
}