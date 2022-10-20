import java.awt.EventQueue
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import javax.swing.JFrame
import javax.swing.JPanel

class Window(obj: Element) : JPanel() {
	var block: Block = obj.toBlock()

	override fun paintComponent(g: Graphics) {
		block.draw(g as Graphics2D, 0, 0)
	}
}

fun createWindow(obj: Element) {
	val frame = JFrame("Elasmotherium")
	frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
	val panel = Window(obj)
	panel.addKeyListener(Listener())
	frame.add(panel)
	frame.setSize(1600, 900)
	frame.isVisible = true
}

var containerStyle = Style()
val buttonStyle = Style(drawRectangle, 4, 4, 4)
val menuCaptionStyle = Style(drawNothing, 4, 0, 4)

val menu = Container(
	ContainerType.horizontalList,
	HorizontalAlign.left,
	VerticalAlign.center,
	containerStyle,
)

val buttons = Container(
	ContainerType.horizontalList,
	HorizontalAlign.left,
	VerticalAlign.center,
	containerStyle,
)

val tree = CanvasBlock(HorizontalAlign.center, VerticalAlign.justify, containerStyle)
val canvas = CanvasBlock(HorizontalAlign.justify, VerticalAlign.justify, containerStyle)
val properties = Container(
	ContainerType.verticalList,
	HorizontalAlign.left,
	VerticalAlign.justify,
	containerStyle
)

fun gui() {
	tree.width = 250
	properties.width = 250
	createWindow(
		Container(
			ContainerType.verticalList,
			HorizontalAlign.justify,
			VerticalAlign.justify,
			containerStyle,
			menu,
			buttons,
			Container(
				ContainerType.horizontalList,
				HorizontalAlign.justify,
				VerticalAlign.justify,
				containerStyle,
				tree,
				canvas,
				properties,
			)
		)
	)
}

fun main() {
	EventQueue.invokeLater(::gui)
}
