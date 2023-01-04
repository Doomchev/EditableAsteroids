import kotlin.random.Random

abstract class Formula {
  abstract fun get(): Double
}

object zero: Formula() {
  override fun get(): Double {
    return 0.0
  }

  override fun toString(): String {
    return "0"
  }
}

fun doubleToFormula(string: String): Formula {
  var dir = false
  var s = string
  if(string.startsWith("+-")) {
    s = s.substring(2)
    dir = true
  }
  if(string.contains("..")) {
    val parts = s.split("..")
    val formula = RandomDoubleValue(parts[0].toDouble(), parts[1].toDouble())
    return if(dir) RandomDirection(formula) else formula
  }
  return DoubleValue(string.toDouble())
}

class DoubleValue(private val value: Double): Formula() {
  override fun get(): Double {
    return value
  }

  override fun toString(): String {
    return "$value"
  }
}

class RandomDoubleValue(private val begin: Double, private val end:Double): Formula() {
  override fun get(): Double {
    return begin + (end - begin) * Random.nextDouble()
  }

  override fun toString(): String {
    return "$begin..$end"
  }
}

class RandomDirection(private val formula: Formula) : Formula() {
  override fun get(): Double {
    return if(Random.nextBoolean()) -formula.get() else formula.get()
  }

  override fun toString(): String {
    return "+-$formula"
  }
}