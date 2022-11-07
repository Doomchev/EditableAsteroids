package mod.dragging

import Sprite
import java.awt.Graphics2D
import java.util.*

interface Drawing {
  fun draw(g: Graphics2D)
}

val selectedSprites = LinkedList<Sprite>()
val sprites = LinkedList<Sprite>()





