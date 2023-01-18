import mod.Element
import kotlin.random.Random

abstract class Formula: Element {
  abstract fun getInt(): Int
  abstract fun getDouble(): Double
  open fun set(value: Int) {}
  open fun add(increment: Int) {}
  open fun add(increment: Double) {}
  open fun add(increment: String) {}
  override fun toNode(node: Node) {}
}

object zero: Formula() {
  override fun getInt(): Int = 0

  override fun getDouble(): Double = 0.0

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

open class IntValue(var value: Int, private var format: String = ""): Formula() {
  override fun getInt(): Int = value

  override fun getDouble(): Double = value.toDouble()

  override fun toString(): String = when(format) {
    "0" -> String.format("%08d", value)
    "d" -> " ∆".repeat(maxOf(value, 0))
    else -> value.toString()
  }

  override fun set(value: Int) {
    this.value = value
  }

  override fun add(increment: Int) {
    value += increment
  }

  override fun add(increment: Double) {
    value += increment.toInt()
  }

  override fun add(increment: String) {
    TODO()
  }

  override fun toNode(node: Node) {
    node.setInt("value", value)
  }
}

open class DoubleValue(var value: Double): Formula() {
  override fun getInt(): Int = value.toInt()

  override fun getDouble(): Double = value

  override fun toString(): String = "$value"

  override fun add(increment: Int) {
    value += increment
  }

  override fun add(increment: Double) {
    value += increment
  }

  override fun add(increment: String) {
    TODO()
  }

  override fun toNode(node: Node) {
    node.setDouble("value", value)
  }
}

class StringValue(private var value: String): Formula() {
  override fun getInt(): Int = value.toInt()

  override fun getDouble(): Double = value.toDouble()

  override fun toString(): String = value

  override fun add(increment: Int) {
    value += increment
  }

  override fun add(increment: Double) {
    value += increment
  }

  override fun add(increment: String) {
    value += increment
  }

  override fun toNode(node: Node) {
    node.setString("value", value)
  }
}

class RandomDoubleValue(private val begin: Double, private val end:Double): Formula() {
  override fun getInt(): Int = getDouble().toInt()

  override fun getDouble(): Double {
    return begin + (end - begin) * Random.nextDouble()
  }

  override fun toString(): String {
    return "$begin..$end"
  }
}

class RandomDirection(private val formula: Formula) : Formula() {
  override fun getInt(): Int = getDouble().toInt()

  override fun getDouble(): Double {
    return if(Random.nextBoolean()) -formula.getDouble() else formula.getDouble()
  }

  override fun toString(): String {
    return "+-$formula"
  }
}