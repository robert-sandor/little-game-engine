package ro.robertsandor.engine.components

import ro.robertsandor.engine.base.GameState
import ro.robertsandor.engine.base.`object`.GameObject
import ro.robertsandor.engine.base.component.{Component, ComponentData}
import ro.robertsandor.engine.base.events.BaseEvent
import ro.robertsandor.engine.mapping.Mapping

case class BallComponent(
                          componentType: String = BallComponent.COMPONENT_TYPE
                        ) extends ComponentData

object BallComponent extends Component[BallComponent] {
  val COMPONENT_TYPE = "ball"

  override def update(thisObject: GameObject): GameObject = {
    val newPosition = thisObject.transform.position + thisObject.transform.speed

    val newSpeed = if (newPosition.x < 0 || newPosition.x > 800) {
      if (newPosition.y < 0 || newPosition.y > 600) {
        thisObject.transform.speed.copy(x = -thisObject.transform.speed.x, y = -thisObject.transform.speed.y)
      } else {
        thisObject.transform.speed.copy(x = -thisObject.transform.speed.x)
      }
    } else {
      if (newPosition.y < 0 || newPosition.y > 600) {
        thisObject.transform.speed.copy(y = -thisObject.transform.speed.y)
      } else {
        thisObject.transform.speed
      }
    }

    thisObject.copy(
      transform = thisObject.transform.copy(position = newPosition, speed = newSpeed)
    )
  }

  override def init(gameObject: GameObject, componentData: ComponentData): ComponentData = {
    println("initializing BallComponent")

    Mapping.eventService.get.addEventListener(gameObject.fullName, "upKey", (event: BaseEvent) => {
      GameState.getObjectByFullName(gameObject.fullName).get
        .components
        .find(componentData => componentData.componentType == JavaFXRenderComponent.COMPONENT_TYPE)
        .get
        .asInstanceOf[JavaFXRenderComponent]
        .color
    })

    componentData
  }

  override def loadComponentData(componentMap: Map[String, _]): BallComponent = BallComponent()
}
