package mod.actions

import Action
import frame
import mod.dragging.selectedSprites
import mousesx
import mousesy
import javax.swing.JMenu
import javax.swing.JPopupMenu

class showMenu(private val menu: JPopupMenu):
  Action {
  override fun conditions(): Boolean {
    return true
  }

  override fun execute() {
    menu.show(frame, mousesx, mousesy)
  }

  override fun toString(): String {
    return "$menu"
  }
}