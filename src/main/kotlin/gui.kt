package mod.dragging

import CollisionEntry
import Formula
import ImageArray
import Key
import MenuEvent
import Sprite
import SpriteAction
import SpriteClass
import SpriteFactory
import actions
import actionsToRemove
import canvases
import classes
import currentCanvas
import frame
import imageArrays
import listener
import mod.actions.SoundPlayFactory
import mod.actions.sprite.*
import newActions
import nullSprite
import spritesToRemove
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
import java.util.*
import javax.swing.*
import kotlin.random.Random

var parentSprite = nullSprite

class Window: JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D

    for(spriteClass1 in classes) {
      for(entry in spriteClass1.onCollision) {
        val spriteClass2 = entry.spriteClass
        for(sprite1 in spriteClass1.sprites) {
          parentSprite = sprite1
          for(sprite2 in spriteClass2.sprites) {
            if(sprite1.collidesWidth(sprite2)) {
              for(factory in entry.factories) {
                factory.create(sprite1).execute()
              }
            }
          }
        }
      }
    }

    for(action in actions) {
      action.execute()
    }

    for(action in newActions) {
      actions.add(action)
    }
    newActions.clear()

    for(sprite in spritesToRemove) {
      scene.remove(sprite)
      val it = actions.iterator()
      while(it.hasNext()) {
        val action = it.next()
        if(action.sprite == sprite) it.remove()
      }
    }

    for(action in actionsToRemove) {
      actions.remove(action)
    }
    actionsToRemove.clear()

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

fun addEventMenu(spriteEvent: JMenu, forClass: Boolean, caption: String, event: MenuEvent) {
  val item = JMenuItem(caption)
  item.addActionListener(MenuListener(forClass, event))
  spriteEvent.add(item)
}

val childFrame: JFrame = JFrame("Key")
class MenuListener(private val forClass: Boolean, private val event: MenuEvent): ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    val spriteClass = if(forClass) selectClass() else null
    if(event == MenuEvent.onPress || event == MenuEvent.onClick) {
      childFrame.setSize(200, 100)
      childFrame.addKeyListener(AnyKeyListener(spriteClass, event))
      childFrame.add(Label("Нажмите клавишу для действия"))
      childFrame.pack()
      childFrame.isVisible = true
    } else {
      menuItemAction(spriteClass, event, 0)
    }
  }
}

class AnyKeyListener(private val spriteClass: SpriteClass?, private val event: MenuEvent): KeyListener {
  override fun keyTyped(e: KeyEvent) {
    childFrame.removeKeyListener(this)
    childFrame.dispose()
    menuItemAction(spriteClass, event, e.keyChar.code)
  }
  override fun keyPressed(e: KeyEvent) {}
  override fun keyReleased(e: KeyEvent) {}
}

private fun menuItemAction(spriteClass: SpriteClass?, event: MenuEvent, keyCode: Int) {
  if(spriteClass == null) {
    if(selectedSprites.isEmpty()) {
      applyToSprite(selectSprite(), event, keyCode)
    } else {
      for(sprite in selectedSprites) {
        applyToSprite(sprite, event, keyCode)
      }
    }
  } else {
    when(event) {
      MenuEvent.onCreate -> spriteClass.onCreate.add(selectFactory(true))
      MenuEvent.onClick -> {}
      MenuEvent.onPress -> {}
      MenuEvent.onCollision -> {
        val spriteClass2 = selectClass("Выберите второй класс:")
        for(entry in spriteClass.onCollision) {
          if(entry.spriteClass == spriteClass2) {
            entry.factories.add(selectFactory(true))
            return
          }
        }
        spriteClass.onCollision.add(CollisionEntry(spriteClass2, selectFactory(true)))
      }
      MenuEvent.always -> spriteClass.always.add(selectFactory(false))
    }
  }
  updateActions()
}

private fun applyToSprite(sprite: Sprite, event: MenuEvent, keyCode: Int) {
  when(event) {
    MenuEvent.onCreate -> {}
    MenuEvent.onClick -> Key(keyCode, user).addOnClick(world, selectFactory(true).create(sprite))
    MenuEvent.onPress -> Key(keyCode, user).addOnPress(world, selectFactory(false).create(sprite))
    MenuEvent.onCollision -> {}
    MenuEvent.always -> actions.add(selectFactory(false).create(sprite))
  }
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

fun selectClass(message:String = "Выберите класс:"): SpriteClass {
  val classesArray = Array(classes.size) {classes[it]}
  return classesArray[JOptionPane.showOptionDialog(frame, message, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, classesArray, classes.first)]
}

val discreteActions = arrayOf(SpriteCreateFactory(), SpritePositionAsFactory(), SpritePositionInAreaFactory(), SpriteSetSizeFactory(), SpriteSetAngleFactory(), SpriteDirectAsFactory(), SpriteSetMovingVectorFactory(), SpriteSetSpeedFactory(), SoundPlayFactory(), SpriteSetImageFactory(), SpriteRemoveFactory())

val continuousActions = arrayOf(SpriteDelayedCreateFactory(), SpriteRotationFactory(), SpriteMoveFactory(), SpriteAccelerationFactory(), SpriteAnimationFactory(), SpriteSetBoundsFactory(), SpriteLoopAreaFactory(), SpriteDelayedRemoveFactory())

fun selectFactory(discrete: Boolean): SpriteFactory {
  val factoriesArray = if(discrete) discreteActions else continuousActions
  return (JOptionPane.showInputDialog(frame, "Выберите действие:", "", JOptionPane.QUESTION_MESSAGE, null, factoriesArray, factoriesArray[0]) as SpriteFactory).copy()
}

val spritesList = LinkedList<Sprite>()
fun selectSprite(): Sprite {
  val options = Array(spritesList.size) { spritesList[it] }
  return options[JOptionPane.showOptionDialog(frame, "Выберите спрайт:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}

fun selectImageArray(): ImageArray {
  val options = Array(imageArrays.size) { imageArrays[it] }
  return options[JOptionPane.showOptionDialog(frame, "Выберите спрайт:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}