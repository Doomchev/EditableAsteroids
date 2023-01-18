package mod.dragging

import CollisionEntry
import Formula
import Key
import MenuEvent
import Sprite
import SpriteClass
import actions
import doubleToFormula
import listener
import Window
import mod.*
import selectClass
import selectSerializer
import selectSprite
import updateActions
import user
import world
import java.awt.Label
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.*

val panel = Window()
object updatePanel: ActionListener {
  override fun actionPerformed(e: ActionEvent) {
    panel.repaint()
    listener.onKeyDown()
  }
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
        spriteClass.onCollision.add(CollisionEntry(spriteClass2, arrayOf(
          selectSerializer(true)
        )))
      }
      MenuEvent.always -> spriteClass.always.add(selectSerializer(false))
    }
  }
  updateActions()
}

private fun applyToSprite(sprite: Sprite, event: MenuEvent, keyCode: Int) {
  when(event) {
    MenuEvent.onCreate -> {}
    MenuEvent.onClick -> Key(keyCode, user, world).onClickActions.add(selectSerializer(true).create(sprite))
    MenuEvent.onPress -> Key(keyCode, user, world).onPressActions.add(selectSerializer(false).create(sprite))
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

