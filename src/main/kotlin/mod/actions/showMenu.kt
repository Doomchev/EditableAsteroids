package mod.actions

import Action
import frame
import mod.dragging.selectSprites
import mod.dragging.selectedShapes
import xToScreen
import yToScreen
import javax.swing.JPopupMenu

class showMenu(val menu: JPopupMenu, val forShape: Boolean) : Action {
  override fun conditions(x: Double, y: Double): Boolean {
    return !forShape || !selectedShapes.isEmpty()
  }

  override fun execute(x: Double, y: Double) {
    menu.show(frame, xToScreen(x), yToScreen(y));
  }
}