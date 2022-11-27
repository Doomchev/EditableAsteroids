import mod.dragging.*
import java.awt.Graphics2D
import java.util.LinkedList

val classes = LinkedList<SpriteClass>()
val emptyClass = SpriteClass("")

class CollisionEntry(var spriteClass: SpriteClass, vararg factoriesArray: SpriteFactory): Element {
  val factories = LinkedList<SpriteFactory>()
  init {
    for(factory in factoriesArray) {
      factories.add(factory)
    }
  }

  override fun toString(): String {
    return "При столкновении $spriteClass"
  }

  override fun getClassName(): String = "CollisionEntry"

  override fun store(node: Node) {
    node.setObject("spriteClass", spriteClass)
    node.setChildren(factories)
  }
}

class SpriteClass(var name: String): SceneElement() {
  val sprites = LinkedList<Sprite>()
  val onCreate = LinkedList<SpriteFactory>()
  val onCollision = LinkedList<CollisionEntry>()
  val always = LinkedList<SpriteFactory>()

  fun add(sprite: Sprite) {
    sprites.add(sprite)
  }

  override fun select(selection: Sprite, selected: LinkedList<Sprite>) {
    for(sprite in sprites.descendingIterator()) {
      sprite.select(selection, selected)
    }
  }

  override fun remove(shape: Shape) {
    sprites.remove(shape)
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return spriteUnderCursor(sprites, fx, fy)
  }

  override fun toString(): String = name
  fun add(spriteClass2: SpriteClass, spriteFactory: SpriteFactory) {
    for(entry in onCollision) {
      if(spriteClass2 == entry.spriteClass) {
        entry.factories.add(spriteFactory)
        return
      }
    }
    onCollision.add(CollisionEntry(spriteClass2, spriteFactory))
  }

  fun addOnCollision(spriteClass: SpriteClass, factory: SpriteFactory) {
    for(entry in onCollision) {
      if(entry.spriteClass == spriteClass) {
        entry.factories.add(factory)
        return
      }
    }
    onCollision.add(CollisionEntry(spriteClass, factory))
  }

  override fun getClassName(): String = "SpriteClass"

  override fun store(node: Node) {
    node.setField("sprites", sprites)
    node.setField("onCreate", onCreate)
    node.setField("onCollision", onCollision)
    node.setField("always", always)
  }

  override fun draw(g: Graphics2D) {
    for(sprite in sprites) {
      sprite.draw(g)
    }
  }
}