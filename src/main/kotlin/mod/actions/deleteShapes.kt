package mod.actions

import Action
import mod.dragging.selectedShapes
import mod.dragging.shapes

object deleteShapes: Action {
  override fun execute(x: Double, y: Double) {
    shapes.removeAll(selectedShapes)
    selectedShapes.clear()
  }
}