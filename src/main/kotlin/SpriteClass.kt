import mod.dragging.*
import java.awt.Graphics2D
import java.util.LinkedList

val classes = LinkedList<SpriteClass>()

class SpriteClass(var name: String): SceneElement() {
  private val sprites = LinkedList<Sprite>()
  val onCreate = LinkedList<SpriteAction>()
  val always = LinkedList<SpriteAction>()
  fun add(sprite: Sprite) {
    sprites.add(sprite)
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    for(sprite in sprites.descendingIterator()) {
      sprite.select(selection, selected)
    }
  }

  override fun remove(sprite: Sprite) {
    sprites.remove(sprite)
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return spriteUnderCursor(sprites, fx, fy)
  }

  override fun draw(g: Graphics2D) {
    for(sprite in sprites) {
      sprite.draw(g)
    }
  }
}