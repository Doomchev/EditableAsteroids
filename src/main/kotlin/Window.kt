import mod.currentEntry
import mod.project
import mod.sprite1Entry
import mod.sprite2Entry
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class Window: JPanel() {
  override fun paintComponent(g: Graphics) {
    val oldCanvas = currentCanvas
    val g2d = g as Graphics2D

    for(spriteClass1 in project.classes) {
      for(entry in spriteClass1.onCollision) {
        val spriteClass2 = entry.spriteClass
        for(sprite1 in spriteClass1.sprites) {
          if(!sprite1.active) continue
          for(sprite2 in spriteClass2.sprites) {
            if(!sprite2.active) continue
            if(sprite1.collidesWidth(sprite2)) {
              sprite1Entry.sprite = sprite1
              sprite2Entry.sprite = sprite2
              for(factory in entry.factories) {
                factory.create().execute()
              }
            }
          }
        }
      }
    }

    for(action in actions) {
      //if(!action.sprite.active) continue
      currentEntry.sprite = (action as SpriteAction).sprite
      action.execute()
    }

    for(entry in newSprites) {
      entry.spriteClass.sprites.add(entry.sprite)
    }
    newSprites.clear()

    for(action in newActions) {
      actions.add(action)
      action.execute()
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

    val it = delayedActions.iterator()
    while(it.hasNext()) {
      val action = it.next()
      if(action.check()) it.remove()
    }

    for(cnv in canvases) {
      cnv.draw(g2d)
    }

    currentCanvas = oldCanvas
  }
}