import java.awt.Color
import java.awt.Graphics2D
import java.util.MutableList

enum class HorizontalAlign {
  left,
  center,
  right,
  justify,
}

enum class VerticalAlign {
  top,
  center,
  bottom,
  justify,
}

enum class ContainerType {
  horizontalList,
  verticalList,
}

class Style(
  val background: Drawable = drawNothing,
  val horizontalMargin: Int = 0,
  val verticalMargin: Int = 0,
  val separator: Int = 0,
) {
  fun draw(g: Graphics2D, x: Int, y: Int, width: Int, height: Int) {
    background.draw(g, x, y, width, height)
    g.color = Color(0, 0, 0)
  }
}

abstract class Element(
  var horizontalAlign: HorizontalAlign,
  var verticalAlign: VerticalAlign,
  var style: Style
) {
  var width = -1
  var height = -1
  abstract fun toBlock(): Block
  abstract fun arrange(block: Block)
}

class Container(
  var type: ContainerType,
  horizontalAlign: HorizontalAlign,
  verticalAlign: VerticalAlign,
  style: Style,
  vararg elements: Element,
): Element(horizontalAlign, verticalAlign, style) {
  var justified = 0
  val elements = mutableListOf<Element>()

  init {
    for(element in elements) this.elements.add(element)
  }

  override fun toBlock(): Block {
    val block = Block(this)
    var x0 = style.horizontalMargin
    var y0 = style.verticalMargin
    justified = 0
    when(type) {
      ContainerType.horizontalList -> {
        var maxHeight = 0
        for(element in elements) {
          val childBlock = element.toBlock()
          childBlock.x = x0
          childBlock.y = y0
          x0 += block.width + style.separator
          maxHeight = maxOf(maxHeight, childBlock.height)
          block.children.add(childBlock)
          if(element.horizontalAlign == HorizontalAlign.justify) {
            justified++
          }
        }
        block.width = x0 + style.horizontalMargin - style.separator
        block.height = maxHeight + 2 * style.verticalMargin
      }
      ContainerType.verticalList -> {
        var maxWidth = 0
        for(element in elements) {
          val childBlock = element.toBlock()
          childBlock.x = x0
          childBlock.y = y0
          y0 += block.height + style.separator
          maxWidth = maxOf(maxWidth, childBlock.width)
          block.children.add(childBlock)
          if(element.verticalAlign == VerticalAlign.justify) {
            justified++
          }
        }
        block.width = if(width >= 0) width
          else maxWidth + 2 * style.horizontalMargin
        block.height = if(height >= 0) height
          else y0 + style.verticalMargin - style.separator
      }
    }
    return block
  }

  override fun arrange(block: Block) {
    TODO("Not yet implemented")
  }

  /*override fun arrange(block: Block) {
    when(type) {
      ContainerType.horizontalList -> {
        if(width >= 0) block.width = width
        if(height >= 0) block.height = height
        val contentHeight = height - style.verticalMargin * 2
        var dWidth = block.width - contentWidth
        val dx = when(horizontalAlign) {
          HorizontalAlign.center -> dWidth / 2
          HorizontalAlign.right -> dWidth
          else -> 0
        }
        for(childBlock in block.children) {
          childBlock.x += dx
          if(childBlock.element.horizontalAlign == HorizontalAlign.justify) {
            childBlock.width += dWidth / justified
          }
          val verticalAlign = childBlock.element.verticalAlign
          if(verticalAlign == VerticalAlign.center) {
            childBlock.y += (childBlock.height - contentHeight) / 2
          } else if(verticalAlign == VerticalAlign.bottom) {
            childBlock.y += (childBlock.height - contentHeight)
          }
        }
      }

      ContainerType.verticalList -> TODO()
    }
  }*/
}

class CanvasBlock(
  horizontalAlign: HorizontalAlign,
  verticalAlign: VerticalAlign,
  style: Style
): Element(horizontalAlign, verticalAlign, style)  {

  override fun toBlock(): Block {
    return Block(this)
  }

  override fun arrange(block: Block) {
  }
}