package mod.dragging

import CollisionEntry
import Formula
import ImageArray
import Key
import MenuEvent
import Node
import Sprite
import SpriteClass
import SpriteActionFactory
import actions
import canvases
import continuousActions
import currentCanvas
import discreteActions
import doubleToFormula
import frame
import imageArrays
import listener
import Serializer
import mod.*
import newActions
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

class Window: JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D

    for(spriteClass1 in project.classes) {
      for(entry in spriteClass1.onCollision) {
        val spriteClass2 = entry.spriteClass
        for(sprite1 in spriteClass1.sprites) {
          for(sprite2 in spriteClass2.sprites) {
            if(sprite1.collidesWidth(sprite2)) {
              sprite1Entry.sprite = sprite1
              sprite2Entry.sprite = sprite2
              for(factory in entry.factories) {
                factory.create(sprite1).execute()
              }
            }
          }
        }
      }
    }

    for(action in actions) {
      currentEntry.sprite = action.sprite
      action.execute()
    }

    for(action in newActions) {
      actions.add(action)
    }
    newActions.clear()

    for(sprite in spritesToRemove) {
      project.remove(sprite)
      val it = actions.iterator()
      while(it.hasNext()) {
        val action = it.next()
        if(action.sprite == sprite) it.remove()
      }
    }
    spritesToRemove.clear()

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
  project.classes.add(newClass)
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
      applyToSprite(selectSprite().resolve(), event, keyCode)
    } else {
      for(sprite in selectedSprites) {
        applyToSprite(sprite, event, keyCode)
      }
    }
  } else {
    when(event) {
      MenuEvent.onCreate -> spriteClass.onCreate.add(selectSerializer(true))
      MenuEvent.onClick -> {}
      MenuEvent.onPress -> {}
      MenuEvent.onCollision -> {
        val spriteClass2 = selectClass("Выберите второй класс:")
        for(entry in spriteClass.onCollision) {
          if(entry.spriteClass == spriteClass2) {
            entry.factories.add(selectSerializer(true))
            return
          }
        }
        spriteClass.onCollision.add(CollisionEntry(spriteClass2, selectSerializer(true)))
      }
      MenuEvent.always -> spriteClass.always.add(selectSerializer(false))
    }
  }
  updateActions()
}

private fun applyToSprite(sprite: Sprite, event: MenuEvent, keyCode: Int) {
  when(event) {
    MenuEvent.onCreate -> {}
    MenuEvent.onClick -> Key(keyCode, user).addOnClick(world, selectSerializer(true).create(sprite))
    MenuEvent.onPress -> Key(keyCode, user).addOnPress(world, selectSerializer(false).create(sprite))
    MenuEvent.onCollision -> {}
    MenuEvent.always -> actions.add(selectSerializer(false).create(sprite))
  }
}

fun enterString(message: String): String {
  return JOptionPane.showInputDialog(message)
}

fun enterInt(message: String): Int {
  return enterString(message).toInt()
}

fun enterDouble(message: String): Formula {
  return doubleToFormula(enterString(message))
}

fun selectClass(message:String = "Выберите класс:"): SpriteClass {
  val classesArray = Array(project.classes.size) { project.classes[it]}
  return classesArray[JOptionPane.showOptionDialog(frame, message, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, classesArray, project.classes.first)]
}

fun selectSerializer(discrete: Boolean): SpriteActionFactory {
  val serArray = if(discrete) discreteActions else continuousActions
  return (JOptionPane.showInputDialog(frame, "Выберите действие:", "", JOptionPane.QUESTION_MESSAGE, null, serArray, serArray[0]) as Serializer).newFactory()
}

fun selectImageArray(): ImageArray {
  val options = Array(imageArrays.size) { imageArrays[it] }
  return options[JOptionPane.showOptionDialog(frame, "Выберите изображение:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}

val nullSpriteEntry = SpriteEntry("")

open class SpriteEntry(private var caption: String, var sprite: Sprite? = null): Element {
  fun resolve(): Sprite = sprite!!
  override fun toString() = caption
  override fun toNode(node: Node) {
    node.setString("caption", caption)
  }
}

val objectsList = LinkedList<SpriteEntry>()
fun selectSprite(message:String = "Выберите спрайт:"): SpriteEntry {
  val options = Array(objectsList.size + 4) {
    when(it) {
      0 -> currentEntry
      1 -> parentEntry
      2 -> sprite1Entry
      3 -> sprite2Entry
      else -> objectsList[it - 4]
    }
  }
  return options[JOptionPane.showOptionDialog(frame, message, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}