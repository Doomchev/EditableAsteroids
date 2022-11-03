package mod.actions

import Action
import mod.dragging.selectedShapes
import mod.dragging.shapes

object deleteShapes: Action {
  override fun execute(x: Int, y: Int) {
    shapes.removeAll(selectedShapes)
    selectedShapes.clear()
  }
}