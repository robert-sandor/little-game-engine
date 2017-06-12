package ro.robertsandor.engine.base.services

import ro.robertsandor.engine.base.GameState

trait RenderService extends Service {
  final override val serviceType: String = "render"

  final override def update(gameState: GameState): GameState = gameState

  def render(gameState: GameState): Unit
}
