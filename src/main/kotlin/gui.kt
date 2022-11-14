package mod.dragging

import Key
import SpriteAction
import SpriteClass
import actions
import canvases
import currentCanvas
import listener
import menuOnClick
import menuOnPress
import world
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Label
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*

class Window(): JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D
    for(action in actions) {
      action.execute()
    }
    for(cnv in canvases) {
      cnv.draw(g2d)
    }
    currentCanvas = oldCanvas
  }
}

val panel = Window()
object updatePanel: ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    panel.repaint()
    listener.onKeyDown()
  }
}

val childFrame: JFrame = JFrame("Key")
class MenuListener(val spriteClass: SpriteClass?, val eventNumber: Int
                   , val action: SpriteAction): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    if(eventNumber <= 1) {
      childFrame.setSize(200, 100)
      childFrame.addKeyListener(AnyKeyListener(spriteClass, eventNumber
        , action))
      childFrame.add(Label("Нажмите клавишу для действия"))
      childFrame.pack()
      childFrame.isVisible = true
    } else {
      menuItemAction(spriteClass, eventNumber, 0, action)
    }
  }

  override fun toString(): String {
    return action.toString()
  }
}

class AnyKeyListener(val spriteClass: SpriteClass?, val eventNumber: Int
, val action: SpriteAction): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.dispose()
    menuItemAction(spriteClass, eventNumber, e.keyChar.code, action)
  }
  override fun keyPressed(e: KeyEvent) {}
  override fun keyReleased(e: KeyEvent) {}
}

private fun menuItemAction(spriteClass: SpriteClass?, eventNumber: Int
                           , keyCode: Int, action: SpriteAction) {
  action.settings()
  for(sprite in selectedSprites) {
    when(eventNumber) {
      menuOnClick -> Key(keyCode).addOnClick(world, action.create(sprite))
      menuOnPress -> Key(keyCode).addOnPress(world, action.create(sprite))
      else -> actions.add(action.create(sprite))
    }
  }
}

fun addMenuItem(parent: JMenu, listener: ActionListener): JMenuItem {
  val item = JMenuItem(listener.toString())
  item.addActionListener(listener)
  parent.add(item)
  return item
}

fun enterString(message: String): String {
  return JOptionPane.showInputDialog(message)
}

fun enterInt(message: String): Int {
  return enterString(message).toInt()
}

fun enterDouble(message: String): Double {
  return enterString(message).toDouble()
}