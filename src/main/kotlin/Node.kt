import mod.dragging.Element
import java.util.LinkedList
import kotlin.reflect.full.createInstance

//class Attribute(var name:String, var value: String)

val ids = HashMap<Element, Int>()
var lastID = 0

class Node(var className: String) {

  private val attributes = HashMap<String, String>()
  private val fields = HashMap<String, Node>()
  private val children = LinkedList<Node>()

  fun getInt(name: String): Int {
    val value = attributes[name]
    return value?.toInt() ?: 0
  }

  fun setInt(name: String, value: Int) {
    attributes[name] = value.toString()
  }

  fun getDouble(name: String): Double {
    val value = attributes[name]
    return value?.toDouble() ?: 0.0
  }

  fun setDouble(name: String, value: Double) {
    attributes[name] = value.toString()
  }

  fun getString(name: String): String {
    return attributes[name] ?: ""
  }

  fun setString(name: String, value: String) {
    attributes[name] = value
  }

  fun getFormula(name: String): Formula {
    return Class.forName(className).kotlin.createInstance() as Formula
  }

  fun setFormula(name: String, element: Formula) {
    attributes[name] = element.toString()
  }

  fun <T: Element> setField(name: String, list: LinkedList<T>) {
    val node = Node("List")
    node.setChildren(list)
    fields[name] = node
  }

  fun getField(name: String): Element {
    return Class.forName(className).kotlin.createInstance() as Element
  }

  fun setField(name: String, element: Element) {
    val id = ids[element]
    if(id == null) {
      val node = Node(element.getClassName())
      element.store(node)
      fields[name] = node
      lastID++
      ids[element] = lastID
      node.setInt("id", lastID)
    } else {
      fields[name] = objectNode(id)
    }
  }

  /*fun <T: Element> getChildren(): LinkedList<T> {
    val list = LinkedList<T>()
    for(node in children) {
      val element = element() as T
      list.add(node.load())
    }
    return fields[name]!! as
  }*/

  fun <T: Element> setChildren(elements: LinkedList<T>) {
    for(element in elements) {
      val node = Node(element.getClassName())
      element.store(node)
      children.add(node)
    }
  }

  fun getText(attr: String = ""): String {
    var text = "<${className}$attr"
    for(attribute in attributes) {
      text += " ${attribute.key}=\"${attribute.value}\""
    }
    if(fields.isEmpty() && children.isEmpty()) return "$text/>"

    text += ">"
    for(field in fields) {
      text += field.value.getText(" field=\"${field.key}\"")
    }
    for(node in children) {
      text += node.getText()
    }
    text += "</$className>"
    return text
  }
}

fun objectNode(id: Int): Node {
  val node = Node("Object")
  node.setInt("id", id)
  return node
}