package mod.dragging

import Action
import Key
import Shape
import canvases
import currentCanvas
import world
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Label
import java.awt.event.*
import javax.swing.*

class Window(): JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D
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

abstract class ShapeAction: Action {
  var shape: Shape? = null
  abstract fun create(shape: Shape): ShapeAction
}

class AnyKeyListener(val action: ShapeAction): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.isVisible = false
    action.settings()
    for(shape in selectedShapes) {
      Key(e.keyChar.code).add(world, action.create(shape))
    }
  }

  override fun keyPressed(e: KeyEvent) {
  }

  override fun keyReleased(e: KeyEvent) {
  }
}

val childFrame: JFrame = JFrame("Key")

class ShapeMenuListener(val action: ShapeAction): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    childFrame.setSize(200, 100)
    childFrame.addKeyListener(AnyKeyListener(action))
    childFrame.add(Label("Нажмите клавишу для действия"))
    childFrame.pack()
    childFrame.isVisible = true
  }
}

class MenuListener(val action: Action): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    action.execute()
  }
}

fun addMenuItem(menu: JMenu, caption: String, action: ShapeAction) {
  var menuItem = JMenuItem(caption)
  menuItem.addActionListener(ShapeMenuListener(action))
  menu.add(menuItem)
}

fun addMenuItem(menu: JPopupMenu, caption: String, action: ShapeAction) {
  var menuItem = JMenuItem(caption)
  menuItem.addActionListener(ShapeMenuListener(action))
  menu.add(menuItem)
}

fun addMenuItem(menu: JPopupMenu, caption: String, action: Action) {
  var menuItem = JMenuItem(caption)
  menuItem.addActionListener(MenuListener(action))
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