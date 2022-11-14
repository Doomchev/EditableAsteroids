package mod.actions

import Action
import frame
import mod.dragging.selectedSprites
import mousesx
import mousesy
import javax.swing.JMenu
import javax.swing.JPopupMenu

class showMenu(val menu: JPopupMenu, val forShape: Boolean): Action {
  override fun conditions(): Boolean {
    return (!forShape || !selectedSprites.isEmpty()) && !menu.isVisible
  }

  override fun execute() {
    menu.show(frame, mousesx, mousesy);
  }
}