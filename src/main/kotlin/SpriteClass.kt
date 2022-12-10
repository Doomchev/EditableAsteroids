import mod.Element
import mod.SceneElement
import java.awt.Graphics2D
import java.util.*

val emptyClass = SpriteClass("")

object collisionEntrySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val entry = CollisionEntry(node.getField("spriteClass") as SpriteClass)
    node.getChildren(entry.factories)
    return entry
  }
}

class CollisionEntry(var spriteClass: SpriteClass): Element {
  val factories = LinkedList<SpriteFactory>()

  constructor(spriteClass: SpriteClass, factory: SpriteFactory) : this(spriteClass) {
    factories.add(factory)
  }

  override fun toString(): String {
    return "При столкновении $spriteClass"
  }

  override fun toNode(node: Node) {
    node.setField("spriteClass", spriteClass)
    node.setChildren(factories)
  }
}

object spriteClassSerializer: ElementSerializer {
  override fun fromNode(node: Node): SceneElement {
    val spriteClass = SpriteClass(node.getString("name"))
    node.getField("onCreate", spriteClass.onCreate)
    node.getField("onCollision", spriteClass.onCollision)
    node.getField("always", spriteClass.always)
    node.getChildren(spriteClass.sprites)
    return spriteClass
  }
}

class SpriteClass(var name: String = ""): SceneElement() {
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

  override fun toNode(node: Node) {
    node.setString("name", name)
    node.setField("onCreate", onCreate)
    node.setField("onCollision", onCollision)
    node.setField("always", always)
    node.setChildren(sprites)
  }

  override fun draw(g: Graphics2D) {
    for(sprite in sprites) {
      sprite.draw(g)
    }
  }

  override fun toString(): String = name
}