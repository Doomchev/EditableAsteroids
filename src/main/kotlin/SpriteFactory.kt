abstract class Formula {
  abstract fun get(): Double
}

abstract class SpriteFactory {
  abstract fun copy(): SpriteFactory
  abstract fun create(sprite: Sprite): SpriteAction
}

object zero: Formula() {
  override fun get(): Double {
    return 0.0
  }

  override fun toString(): String {
    return "0"
  }
}