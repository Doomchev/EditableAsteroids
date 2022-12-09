object parser {
  var text = ""
  private var textPos = 0

  private fun iDSym(sym: Char): Boolean {
    if(sym >= 'a' && sym <= 'z') return true
    if(sym >= 'A' && sym <= 'Z') return true
    if(sym >= '0' && sym <= '9') return true
    return sym == '_' || sym == '-'
  }

  private fun getID(): String {
    var beginning = -1
    while(true) {
      when(val c = text[textPos]) {
        ' ', '\t', '\r', '\n' -> {
          if(beginning >= 0) return text.substring(beginning, textPos)
        }
        else -> {
          if(iDSym(c)) {
            if(beginning < 0) {
              beginning = textPos
            }
          } else {
            if(beginning < 0) {
              beginning = textPos + 1
            } else {
              //println(text.substring(beginning, textPos))
              return text.substring(beginning, textPos)
            }
          }
        }
      }
      textPos++
    }
  }

  fun getValue(): String {
    var quotes: Int = -1
    while(true) {
      when(text[textPos]) {
        '\"' -> {
          if(quotes < 0) {
            quotes = textPos
          } else {
            return text.substring(quotes + 1, textPos)
          }
        } else -> {}
      }
      textPos++
    }
  }

  private fun currentSymbol(): Char {
    while(true) {
      when(val c = text[textPos]) {
        ' ', '\t', '\r', '\n' -> {}
        else -> return c
      }
      textPos++
    }
  }

  private fun expect(symbol: Char) {
    if(currentSymbol() == symbol) {
      textPos++
      return
    }
    throw Error()
  }

  private val idMap = HashMap<Int, Node>()

  fun fromText(parent: Node? = null): Node? {
    while(true) {
      expect('<')

      if(currentSymbol() == '/') {
        textPos++
        val id = getID()
        if(id != parent!!.className) throw Error()
        expect('>')
        return null
      }

      var node = Node(getID())
      parent?.children?.add(node) //println(node.className)
      while(true) {
        when(currentSymbol()) {
          '/' -> {
            textPos++
            expect('>')
            break
          }
          '>' -> {
            textPos++
            fromText(node)
            break
          }
        }
        val id = getID()
        expect('=')
        val value = getValue()
        if(id == "id") {
          if(node.className != "Object") {
            idMap[value.toInt()] = node
          }
        } else if(parent != null && id == "field") {
          parent.fields[value] = node
          parent.children.remove(node)
        } else {
          node.attributes[id] = value
        }
        textPos++
      }

      if(textPos >= text.length) return node
    }
  }
}