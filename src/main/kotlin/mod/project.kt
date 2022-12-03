package mod


import Node
import Shape
import Sprite
import SpriteClass
import actions
import toRemove
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

interface Drawing {
  fun draw(g: Graphics2D)
}

val selectedSprites = LinkedList<Sprite>()

interface Element {
  fun toNode(node: Node)
  fun fromNode(node: Node)
}

abstract class SceneElement: Element, Drawing {
  abstract fun select(selection: Sprite, selected: LinkedList<Sprite>)
  abstract fun remove(shape: Shape)
  abstract fun spriteUnderCursor(fx: Double, fy: Double): Sprite?
}

object project: SceneElement() {
  private val elements = LinkedList<SceneElement>()
  val classes = LinkedList<SpriteClass>()

  fun add(element: SceneElement) {
    elements.add(element)
  }

  fun addFirst(sprite: Sprite) {
    elements.addFirst(sprite)
  }

  override fun draw(g: Graphics2D) {
    for(element in elements) {
      element.draw(g)
    }
    g.color = Color.white
    g.drawString("${actions.size}", 300, 18)
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    for(element in elements) {
      element.select(selection, selected)
    }
  }

  override fun remove(shape: Shape) {
    val it = elements.iterator()
    while(it.hasNext()) {
      val element = it.next()
      if(shape == element) {
        it.remove()
      } else {
        element.remove(shape)
      }
    }
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    for(element in elements.descendingIterator()) {
      val sprite = element.spriteUnderCursor(fx, fy)
      if(sprite != null) return sprite
    }
    return null
  }

  override fun toNode(node: Node) {
    node.setField("classes", classes)
    node.setChildren(elements)
    for(node in toRemove.values) {
      node.removeAttribute("id")
    }
  }

  override fun fromNode(node: Node) {
    node.getField("classes", classes)
    node.getChildren(elements)
  }
}