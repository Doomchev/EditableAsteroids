package mod.actions

import Sprite
import SpriteAction
import SpriteFactory
import frame
import mod.actions.sprite.currentImageArray
import sounds
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.swing.JOptionPane

class SoundPlayFactory(var file: File? = null): SpriteFactory() {
  override fun copy(): SpriteFactory {
    return SoundPlayFactory(file)
  }

  override fun create(sprite: Sprite): SpriteAction {
    return SoundPlay(sprite, selectSound())
  }

  override fun toString(): String = "Проиграть звук"
}

class SoundPlay(sprite: Sprite, var file: File): SpriteAction(sprite) {
  override fun execute() {
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
  }
}

fun selectSound(): File {
  val soundOptions = Array(sounds.size) {sounds[it]}
  return soundOptions[JOptionPane.showOptionDialog(frame, "Выберите звук:", "", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, soundOptions, soundOptions[0])]
}