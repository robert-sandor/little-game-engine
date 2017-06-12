package ro.robertsandor.engine.base.services

import ro.robertsandor.engine.base.GameState

trait Service {
  val serviceType: String

  def init(): Unit = {}

  def update(gameState: GameState): GameState
}
