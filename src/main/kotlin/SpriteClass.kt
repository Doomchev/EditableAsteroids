import mod.Element
import mod.SceneElement
import mod.project
import java.awt.Graphics2D
import java.util.*
import javax.swing.JOptionPane

val emptyClass = SpriteClass("")

class NewSprite(var spriteClass: SpriteClass, var sprite: Sprite)
val newSprites = mutableListOf<NewSprite>()

object collisionEntrySerializer: ElementSerializer {
  override fun fromNode(node: Node): Element {
    val entry = CollisionEntry(node.getField("spriteClass") as SpriteClass)
    node.getChildren(entry.factories)
    return entry
  }
}

class CollisionEntry(var spriteClass: SpriteClass): Element {
  val factories = mutableListOf<SpriteActionFactory>()

  constructor(spriteClass: SpriteClass, newFactories: Array<out SpriteActionFactory>) : this(spriteClass) {
    factories.addAll(newFactories)
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

class SpriteClass(private var name: String = ""): SceneElement() {
  val sprites = mutableListOf<Sprite>()
  val onCreate = mutableListOf<SpriteActionFactory>()
  val onCollision = mutableListOf<CollisionEntry>()
  val always = mutableListOf<SpriteActionFactory>()

  fun add(sprite: Sprite) {
    sprites.add(sprite)
  }

  override fun select(selection: Sprite, selected: MutableList<Sprite>) {
    for(sprite in sprites.reversed()) {
      sprite.select(selection, selected)
    }
  }

  override fun remove(shape: Shape) {
    sprites.remove(shape)
  }

  override fun spriteUnderCursor(fx: Double, fy: Double): Sprite? {
    return spriteUnderCursor(sprites, fx, fy)
  }
  
  fun add(spriteClass2: SpriteClass, vararg factories: SpriteActionFactory) {
    for(entry in onCollision) {
      if(spriteClass2 == entry.spriteClass) {
        entry.factories.addAll(factories)
        return
      }
    }
    onCollision.add(CollisionEntry(spriteClass2, factories))
  }

  fun addOnCollision(spriteClass: SpriteClass, vararg factories: SpriteActionFactory) {
    for(entry in onCollision) {
      if(entry.spriteClass == spriteClass) {
        entry.factories.addAll(factories)
        return
      }
    }
    onCollision.add(CollisionEntry(spriteClass, factories))
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

fun addClass(caption: String): SpriteClass {
  val newClass = SpriteClass(caption)
  project.classes.add(newClass)
  return newClass
}

fun selectClass(message:String = "Выберите класс:"): SpriteClass {
  val classesArray = Array(project.classes.size) { project.classes[it]}
  return classesArray[JOptionPane.showOptionDialog(frame, message, "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, classesArray, project.classes.first())]
}