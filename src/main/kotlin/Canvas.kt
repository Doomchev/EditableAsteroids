class Canvas(
  centerX: Double,
  centerY: Double,
  halfWidth: Double,
  halfHeight: Double
) : Shape(centerX, centerY, halfWidth, halfHeight) {
  var vdx: Double = 1.0
  var vdy: Double = 1.0
  var k: Double = 1.0

  val viewport: Shape = Shape(600.0, 337.5,600.0, 337.5)

  fun update() {
    k = viewport.width / width
    height = viewport.height / k
    vdx = viewport.centerX / k - centerX
    vdy = viewport.centerY / k - centerY
  }
}

fun xToScreen(fieldX: Double): Double = fieldX * canvas.k + canvas.vdx
fun yToScreen(fieldY: Double): Double = fieldY * canvas.k + canvas.vdy
fun distToScreen(fieldDist: Double): Double = fieldDist * canvas.k

fun xFromScreen(screenX: Int): Double = (screenX - canvas.vdx) / canvas.k
fun yFromScreen(screenY: Int): Double = (screenY - canvas.vdy) / canvas.k

var canvas = Canvas(0.0, 0.0, 16.0, 9.0)