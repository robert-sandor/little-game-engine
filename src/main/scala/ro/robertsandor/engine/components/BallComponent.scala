package ro.robertsandor.engine.components

import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.base.component.{Component, ComponentData}

case class BallComponent(
                          componentType: String = BallComponent.COMPONENT_TYPE
                        ) extends ComponentData

object BallComponent extends Component[BallComponent] {
  val COMPONENT_TYPE = "ball"

  override def update(thisObject: GameObject): GameObject = {
    GameState.getObjectByFullName(thisObject.parentFullName) match {
      case Some(parent) =>
        val newPosition = thisObject.transform.position + thisObject.transform.speed

        val newSpeed = if (newPosition.x < parent.transform.position.x ||
          newPosition.x > parent.transform.position.x + parent.transform.size.x) {
          thisObject.transform.speed.copy(x = -thisObject.transform.speed.x)
        } else {
          thisObject.transform.speed
        }

        thisObject.copy(
          transform = thisObject.transform.copy(position = newPosition, speed = newSpeed)
        )
      case None => thisObject
    }
  }

  override def newComponentData(m: Map[String, _]): BallComponent = BallComponent()
}
