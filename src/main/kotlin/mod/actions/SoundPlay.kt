package mod.actions

import Node
import Sprite
import SpriteAction
import SpriteFactory
import frame
import mod.Serializer
import mod.actions.sprite.SpriteAcceleration
import mod.actions.sprite.SpriteAccelerationFactory
import sounds
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.swing.JOptionPane

object soundPlaySerializer: Serializer {
  override fun newFactory(): SpriteFactory {
    return SoundPlayFactory(selectSound())
  }

  override fun factoryFromNode(node: Node): SpriteFactory {
    return SoundPlayFactory(File(node.getString("file")))
  }

  override fun actionFromNode(node: Node): SpriteAction {
    return SoundPlay(node.getField("sprite") as Sprite,
      File(node.getString("file")))
  }
}

class SoundPlayFactory(var file: File? = null): SpriteFactory() {
  override fun create(sprite: Sprite): SpriteAction {
    return SoundPlay(sprite, file!!)
  }

  override fun fullText(): String = "Проиграть звук $file"

  override fun toNode(node: Node) {
    node.setString("file", file!!.name)
  }
}

class SoundPlay(sprite: Sprite, var file: File): SpriteAction(sprite) {
  override fun execute() {
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
  }

  override fun toNode(node: Node) {
    node.setString("file", file.name)
  }
}

fun selectSound(): File {
  val options = Array(sounds.size) {sounds[it]}
  return options[JOptionPane.showOptionDialog(frame, "Выберите звук:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])]
}