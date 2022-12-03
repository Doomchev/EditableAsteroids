import mod.Element

abstract class Formula {
  abstract fun get(): Double
}

abstract class SpriteFactory: Element {
  abstract fun copy(): SpriteFactory
  abstract fun create(sprite: Sprite): SpriteAction
  override fun toString(): String = fullText()
  open fun fullText(): String = fullText()
}

object zero: Formula() {
  override fun get(): Double {
    return 0.0
  }

  override fun toString(): String {
    return "0"
  }
}