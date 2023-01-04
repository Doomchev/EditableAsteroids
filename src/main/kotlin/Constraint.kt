import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Constraint(sprite: Sprite, private val parent: Sprite): SpriteAction(sprite) {
  private val dAngle: Double
  private val dAngle2: Double
  private val length: Double

  init {
    dAngle = sprite.angle - parent.angle
    val dx = sprite.centerX - parent.centerX
    val dy = sprite.centerY - parent.centerY
    length = sqrt(dx * dx + dy * dy)
    dAngle2 = atan2(dy, dx) - parent.angle
  }

  override fun execute() {
    sprite.angle = parent.angle + dAngle
    val angle = parent.angle + dAngle2
    sprite.centerX = parent.centerX + length * cos(angle)
    sprite.centerY = parent.centerY + length * sin(angle)
  }
}

fun addConstraint(sprite: Sprite, parent: Sprite) {
  actions.add(Constraint(sprite, parent))
}