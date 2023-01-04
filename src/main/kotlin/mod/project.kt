package mod


import Node
import Pushable
import Shape
import Sprite
import SpriteClass
import SpriteEntry
import actions
import buttons
import imageArrays
import objectsList
import state.State
import toRemove
import user
import java.awt.Color
import java.awt.Graphics2D
import java.util.*

interface Drawing {
  fun draw(g: Graphics2D)
}

val selectedSprites = mutableListOf<Sprite>()

interface Element {
  fun toNode(node: Node)
}

abstract class SceneElement: Element, Drawing {
  abstract fun select(selection: Sprite, selected: MutableList<Sprite>)
  abstract fun remove(shape: Shape)
  abstract fun spriteUnderCursor(fx: Double, fy: Double): Sprite?
}

var currentEntry = SpriteEntry("текущий")
var parentEntry = SpriteEntry("родитель")
var sprite1Entry = SpriteEntry("спрайт 1")
var sprite2Entry = SpriteEntry("спрайт 2")

object project: SceneElement() {
  private val elements = mutableListOf<SceneElement>()
  val classes = mutableListOf<SpriteClass>()
  val states = mutableListOf<State>()

  fun add(element: SceneElement) {
    elements.add(element)
  }

  fun addFirst(sprite: Sprite) {
    elements.add(0, sprite)
  }

  override fun draw(g: Graphics2D) {
    for(element in elements) {
      element.draw(g)
    }
    g.color = Color.white
    g.drawString("${actions.size}", 300, 18)
  }

  override fun select(selection: Sprite, selected: MutableList<Sprite>) {
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
    for(element in elements.reversed()) {
      val sprite = element.spriteUnderCursor(fx, fy)
      if(sprite != null) return sprite
    }
    return null
  }

  fun fromNode(node: Node) {
    currentEntry = node.getField("current") as SpriteEntry
    parentEntry = node.getField("parent") as SpriteEntry
    sprite1Entry = node.getField("sprite1") as SpriteEntry
    sprite2Entry = node.getField("sprite2") as SpriteEntry
    node.getField("objects", objectsList)
    node.getField("images", imageArrays)
    node.getField("classes", classes)
    node.getField("buttons", mutableListOf<Pushable>())
    node.getField("actions", actions)
    node.getChildren(elements)
  }

  override fun toNode(node: Node) {
    node.setField("current", currentEntry)
    node.setField("parent", parentEntry)
    node.setField("sprite1", sprite1Entry)
    node.setField("sprite2", sprite2Entry)

    node.setField("images", imageArrays)
    node.setField("classes", classes)
    val list = mutableListOf<Pushable>()
    for(button in buttons) {
      if(button.project == user) list.add(button)
    }
    node.setField("buttons", list)
    node.setField("actions", actions)
    node.setChildren(elements)
    for(node2 in toRemove.values) {
      node2.removeAttribute("id")
    }
  }
}