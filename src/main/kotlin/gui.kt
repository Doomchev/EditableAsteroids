package mod.dragging

import Formula
import Key
import MenuEvent
import Sprite
import SpriteClass
import SpriteFactory
import actions
import canvases
import classes
import currentCanvas
import frame
import listener
import newActions
import world
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Label
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*
import kotlin.random.Random

class Window : JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D
    for(action in actions) {
      action.execute()
    }
    for(action in newActions) {
      actions.add(action)
    }
    newActions.clear()
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
class MenuListener(private val spriteClass: SpriteClass?, private val event: MenuEvent, private val factory: SpriteFactory): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    if(event == MenuEvent.onPress || event == MenuEvent.onClick) {
      childFrame.setSize(200, 100)
      childFrame.addKeyListener(AnyKeyListener(spriteClass, event
        , factory))
      childFrame.add(Label("Нажмите клавишу для действия"))
      childFrame.pack()
      childFrame.isVisible = true
    } else {
      menuItemAction(spriteClass, event, 0, factory)
    }
  }

  override fun toString(): String = factory.toString()
}

class AnyKeyListener(
  private val spriteClass: SpriteClass?, private val event: MenuEvent
  , private val factory: SpriteFactory): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.dispose()
    menuItemAction(spriteClass, event, e.keyChar.code, factory)
  }
  override fun keyPressed(e: KeyEvent) {}
  override fun keyReleased(e: KeyEvent) {}
}

private fun applyToSprite(sprite: Sprite, event: MenuEvent, keyCode: Int, factory: SpriteFactory) {
  when(event) {
    MenuEvent.onCreate -> {}
    MenuEvent.onClick -> Key(keyCode).addOnClick(world, factory.create(sprite))
    MenuEvent.onPress -> Key(keyCode).addOnPress(world, factory.create(sprite))
    MenuEvent.always -> actions.add(factory.create(sprite))
  }
}


private fun menuItemAction(spriteClass: SpriteClass?, event: MenuEvent, keyCode: Int, factory: SpriteFactory) {
  if(spriteClass == null) {
    if(selectedSprites.isEmpty()) {
      applyToSprite(Sprite(), event, keyCode, factory.copy())
    } else {
      for(sprite in selectedSprites) {
        applyToSprite(sprite, event, keyCode, factory)
      }
    }
  } else {
    when(event) {
      MenuEvent.onCreate -> spriteClass.onCreate.add(factory.copy())
      MenuEvent.onClick -> {}
      MenuEvent.onPress -> {}
      MenuEvent.always -> spriteClass.always.add(factory.copy())
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

fun enterDouble(message: String): Formula {
  var string = enterString(message)
  var dir = false
  if(string.startsWith("+-")) {
    string = string.substring(2)
    dir = true
  }
  if(string.contains("..")) {
    val parts = string.split("..")
    val formula = RandomDoubleValue(parts[0].toDouble(), parts[1].toDouble())
    return if(dir) RandomDirection(formula) else formula
  }
  return DoubleValue(string.toDouble())
}

class DoubleValue(private val value: Double): Formula() {
  override fun get(): Double {
    return value
  }
}

class RandomDoubleValue(private val from: Double, private val size:Double): Formula() {
  override fun get(): Double {
    return from + size * Random.nextDouble()
  }
}

class RandomDirection(private val formula: Formula) : Formula() {
    override fun get(): Double {
      return if(Random.nextBoolean()) -formula.get() else formula.get()
    }
}

fun selectClass(): SpriteClass {
  val classesArray = Array<SpriteClass>(classes.size) {classes[it]}
  return classesArray[JOptionPane.showOptionDialog(frame, "Выберите класс:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, classesArray, classes.first)]
}