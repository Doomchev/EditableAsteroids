package mod.dragging

import Key
import MenuEvent
import SpriteAction
import SpriteClass
import actions
import canvases
import currentCanvas
import listener
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
class MenuListener(
  val spriteClass: SpriteClass?, val event: MenuEvent
  , val action: SpriteAction): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    if(event == MenuEvent.onPress || event == MenuEvent.onClick) {
      childFrame.setSize(200, 100)
      childFrame.addKeyListener(AnyKeyListener(spriteClass, event
        , action))
      childFrame.add(Label("Нажмите клавишу для действия"))
      childFrame.pack()
      childFrame.isVisible = true
    } else {
      menuItemAction(spriteClass, event, 0, action)
    }
  }

  override fun toString(): String {
    return action.toString()
  }
}

class AnyKeyListener(val spriteClass: SpriteClass?, val event: MenuEvent
, val action: SpriteAction): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.dispose()
    menuItemAction(spriteClass, event, e.keyChar.code, action)
  }
  override fun keyPressed(e: KeyEvent) {}
  override fun keyReleased(e: KeyEvent) {}
}

private fun menuItemAction(spriteClass: SpriteClass?, event: MenuEvent
                           , keyCode: Int, action: SpriteAction) {
  action.settings()
  if(spriteClass == null) {
    for(sprite in selectedSprites) {
      when(event) {
        MenuEvent.onCreate -> {}
        MenuEvent.onClick -> Key(keyCode).addOnClick(world, action.create(sprite))
        MenuEvent.onPress -> Key(keyCode).addOnPress(world, action.create(sprite))
        MenuEvent.always -> actions.add(action.create(sprite))
      }
    }
  } else {
    /*when(event) {
      MenuEvent.onClick -> Key(keyCode).addOnClick(world, action.create(null))
      MenuEvent.onPress -> Key(keyCode).addOnPress(world, action.create(null))
      else -> actions.add(action.create(sprite))
    }*/
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