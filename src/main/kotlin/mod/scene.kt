package mod.dragging

import Sprite
import java.awt.Graphics2D
import java.util.*

interface Drawing {
  fun draw(g: Graphics2D)
}

val selectedSprites = LinkedList<Sprite>()

abstract class SceneElement: Drawing {
  abstract fun select(selection: Sprite, selected: LinkedList<Sprite>)
  abstract fun remove(sprite: Sprite)
  abstract fun spriteUnderCursor(fx: Double, fy: Double): Sprite?
}

object scene: SceneElement() {
  private val elements = LinkedList<SceneElement>()
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
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    for(element in elements) {
      element.select(selection, selected)
    }
  }

  override fun remove(sprite: Sprite) {
    val it = elements.iterator()
    while(it.hasNext()) {
      val element = it.next()
      if(sprite == element) {
        it.remove()
      } else {
        element.remove(sprite)
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
}





