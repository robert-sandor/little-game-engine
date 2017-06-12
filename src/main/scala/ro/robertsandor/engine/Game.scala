package ro.robertsandor.engine

import java.util.{Timer, TimerTask}

import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.GameState.currentGameState
import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.mapping.Mapping

object Game {
  def loop(initialGameState: GameState, fps: Int): Timer = {
    currentGameState = initialGameState

    val timer = new Timer()
    val drawFrame = new TimerTask {
      override def run(): Unit = {
        renderGameState(currentGameState)
        currentGameState = updateGameState(currentGameState)
      }
    }

    timer.schedule(drawFrame, 0, 1000 / fps)
    timer
  }

  def updateGameState(gameState: GameState): GameState = {
    var newGameState = gameState
    Mapping.serviceMap.foreach(serviceEntry => {
      newGameState = serviceEntry._2.update(newGameState)
    })

    val rootGameObjects = gameState.rootGameObjects
      .map(updateGameObject)

    newGameState.copy(rootGameObjects = rootGameObjects)
  }

  def updateGameObject(gameObject: GameObject): GameObject = {
    updateGameObjectChildren(
      updateGameObjectComponents(gameObject)
    )
  }

  def updateGameObjectComponents(gameObject: GameObject): GameObject = {
    var newGameObject = gameObject

    gameObject.components.foreach(component => {
      newGameObject = Mapping.componentMap(component.componentType).update(newGameObject)
    })

    newGameObject
  }

  def updateGameObjectChildren(gameObject: GameObject): GameObject = {
    val newChildren = gameObject.children.map(updateGameObject)
    gameObject.copy(children = newChildren)
  }

  def renderGameState(gameState: GameState): Unit = {
    Mapping.renderService.get.render(gameState)
  }

}
