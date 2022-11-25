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
import mod.actions.SoundPlayFactory
import mod.actions.sprite.*
import newActions
import updateActions
import user
import world
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Label
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.LinkedList
import javax.swing.*
import kotlin.random.Random

class Window: JPanel() {
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

fun addClass(caption: String): SpriteClass {
  val newClass = SpriteClass(caption)
  classes.add(newClass)
  return newClass
}

fun addEventMenu(spriteEvent: JMenu, forClass: Boolean, caption: String, event: MenuEvent, discrete: Boolean) {
  val item = JMenuItem(caption)
  item.addActionListener(MenuListener(forClass, event, discrete))
  spriteEvent.add(item)
}

val childFrame: JFrame = JFrame("Key")
class MenuListener(private val forClass: Boolean, private val event: MenuEvent, private val discrete: Boolean): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    val spriteClass = if(forClass) selectClass() else null
    val factory = selectFactory(discrete)
    if(event == MenuEvent.onPress || event == MenuEvent.onClick) {
      childFrame.setSize(200, 100)
      childFrame.addKeyListener(AnyKeyListener(spriteClass, event, factory))
      childFrame.add(Label("Нажмите клавишу для действия"))
      childFrame.pack()
      childFrame.isVisible = true
    } else if(event == MenuEvent.onCollision) {

    } else {
      menuItemAction(spriteClass, event, 0, factory)
    }
  }

  //override fun toString(): String = factory.toString()
}

class AnyKeyListener(private val spriteClass: SpriteClass?, private val event: MenuEvent, private val factory: SpriteFactory): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.dispose()
    menuItemAction(spriteClass, event, e.keyChar.code, factory)
  }
  override fun keyPressed(e: KeyEvent) {}
  override fun keyReleased(e: KeyEvent) {}
}

private fun menuItemAction(spriteClass: SpriteClass?, event: MenuEvent, keyCode: Int, factory: SpriteFactory) {
  if(spriteClass == null) {
    if(selectedSprites.isEmpty()) {
      applyToSprite(selectSprite(), event, keyCode, factory.copy())
    } else {
      for(sprite in selectedSprites) {
        applyToSprite(sprite, event, keyCode, factory.copy())
      }
    }
  } else {
    when(event) {
      MenuEvent.onCreate -> spriteClass.onCreate.add(factory.copy())
      MenuEvent.onClick -> {}
      MenuEvent.onPress -> {}
      MenuEvent.onCollision -> {}
      MenuEvent.always -> spriteClass.always.add(factory.copy())
    }
  }
  updateActions()
}

private fun applyToSprite(sprite: Sprite, event: MenuEvent, keyCode: Int, factory: SpriteFactory) {
  when(event) {
    MenuEvent.onCreate -> {}
    MenuEvent.onClick -> Key(keyCode, user).addOnClick(world, factory.create(sprite))
    MenuEvent.onPress -> Key(keyCode, user).addOnPress(world, factory.create(sprite))
    MenuEvent.onCollision -> {}
    MenuEvent.always -> actions.add(factory.create(sprite))
  }
}

fun addMenuItem(caption: String, parent: JMenu, listener: ActionListener): JMenuItem {
  val item = JMenuItem(caption)
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

  override fun toString(): String {
    return "$value"
  }
}

class RandomDoubleValue(private val from: Double, private val size:Double): Formula() {
  override fun get(): Double {
    return from + size * Random.nextDouble()
  }

  override fun toString(): String {
    return "$from..$size"
  }
}

class RandomDirection(private val formula: Formula) : Formula() {
    override fun get(): Double {
      return if(Random.nextBoolean()) -formula.get() else formula.get()
    }

  override fun toString(): String {
    return "+-$formula"
  }
}

fun selectClass(): SpriteClass {
  val classesArray = Array(classes.size) {classes[it]}
  return classesArray[JOptionPane.showOptionDialog(frame, "Выберите класс:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, classesArray, classes.first)]
}

val discreteActions = arrayOf(SpriteCreateFactory(), SpritePositionAsFactory(), SpritePositionInAreaFactory(), SpriteSetSizeFactory(), SpriteSetAngleFactory(), SpriteDirectAsFactory(), SpriteSetMovingVectorFactory(), SpriteSetSpeedFactory(), SoundPlayFactory(), SpriteSetImageFactory(), SpriteRemoveFactory())
val continuousActions = arrayOf(SpriteRotationFactory(), SpriteMoveFactory(), SpriteAccelerationFactory(), SpriteAnimationFactory(), SpriteSetBoundsFactory(), SpriteLoopAreaFactory())

fun selectFactory(discrete: Boolean): SpriteFactory {
  val factoriesArray = if(discrete) discreteActions else continuousActions
  return factoriesArray[JOptionPane.showOptionDialog(frame, "Выберите действие:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, factoriesArray, factoriesArray[0])]
}

val spritesList = LinkedList<Sprite>()
fun selectSprite(): Sprite {
  val options = Array(spritesList.size) { spritesList[it] }
  return options[JOptionPane.showOptionDialog(frame, "Выберите спрайт:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}