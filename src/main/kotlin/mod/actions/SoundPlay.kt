package mod.actions

import Sprite
import SpriteAction
import frame
import soundOptions
import sounds
import java.io.File
import javax.sound.sampled.AudioSystem
import javax.swing.JOptionPane

class SoundPlay: SpriteAction() {
  private val file: File? = null

  override fun create(sprite: Sprite?): SpriteAction {
    return SoundPlay()
  }

  override fun settings() {
    JOptionPane.showOptionDialog(frame, "", "Выберите звук:",
      JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, soundOptions, soundOptions!![0])
  }

  override fun execute() {
    val file = sounds[0]
    val audioInputStream = AudioSystem.getAudioInputStream(file.absoluteFile)
    val clip = AudioSystem.getClip()
    clip.open(audioInputStream)
    clip.start()
  }

  override fun toString(): String = "Проиграть звук"
}