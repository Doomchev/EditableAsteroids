package mod.dragging

import Action
import Key
import Sprite
import actions
import canvases
import currentCanvas
import world
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Label
import java.awt.event.*
import java.util.LinkedList
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

abstract class SpriteAction: Action {
  var sprite: Sprite? = null
  abstract fun create(sprite: Sprite): SpriteAction
}

class AnyKeyListener(val action: SpriteAction): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.isVisible = false
    action.settings()
    for(shape in selectedSprites) {
      Key(e.keyChar.code).add(world, action.create(shape))
    }
  }

  override fun keyPressed(e: KeyEvent) {
  }

  override fun keyReleased(e: KeyEvent) {
  }
}

val childFrame: JFrame = JFrame("Key")

class ShapeKeyMenuListener(val action: SpriteAction): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    childFrame.setSize(200, 100)
    childFrame.addKeyListener(AnyKeyListener(action))
    childFrame.add(Label("Нажмите клавишу для действия"))
    childFrame.pack()
    childFrame.isVisible = true
  }
}

class ShapeMenuListener(val action: SpriteAction): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    action.settings()
    for(sprite in selectedSprites) {
      actions.add(action.create(sprite))
    }
  }
}

class MenuListener(val action: Action): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    action.execute()
  }
}

fun addMenuItem(menu: JMenu, caption: String, listener: ActionListener) {
  val menuItem = JMenuItem(caption)
  menuItem.addActionListener(listener)
  menu.add(menuItem)
}

fun addMenuItem(menu: JPopupMenu, caption: String, listener: ActionListener) {
  val menuItem = JMenuItem(caption)
  menuItem.addActionListener(listener)
  menu.add(menuItem)
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