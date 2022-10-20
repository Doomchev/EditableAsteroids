open class Sprite(
  //var shape: Shape = Shape.Companion.rectangle,
  x: Double = 0.0,
  y: Double = 0.0,
  width: Double = 0.0,
  height: Double = 0.0
): Shape(x, y ,width, height) {
  var index: Int = 0
  //var image: Image = Image()
  var mirrorHorizontally: Boolean = false
  var mirrorVertically: Boolean = false
  open fun getDisplayingAngle(): Double {
    return 0.0
  }
}
