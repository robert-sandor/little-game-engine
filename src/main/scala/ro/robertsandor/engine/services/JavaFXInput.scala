package ro.robertsandor.engine.services

import javafx.scene.Scene
import javafx.scene.input._

import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.events.Event
import ro.robertsandor.engine.base.services.Service
import ro.robertsandor.engine.mapping.Mapping

object JavaFXInput extends Service {
//  var scene: Scene = _
  override val serviceType: String = "input"

  def initializeInputEvents(scene: Scene) : Scene = {
    scene.setOnKeyPressed((event: KeyEvent) => {
      println("key pressed")
      event.getCode match {
        case KeyCode.UP => Mapping.eventService.get.dispatchEvent(Event("upKey"))
        case KeyCode.DOWN => Mapping.eventService.get.dispatchEvent(Event("downKey"))
        case _ =>
      }
    })
    scene
  }

  override def init(): Unit = {

  }

  override def update(gameState: GameState): GameState = gameState
}
