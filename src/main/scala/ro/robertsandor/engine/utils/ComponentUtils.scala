package ro.robertsandor.engine.utils

import ro.robertsandor.engine.mapping.Mapping.componentMap
import ro.robertsandor.engine.components.Component
import ro.robertsandor.engine.objects.GameObject

/**
  * Created by robert on 5/22/17.
  */
object ComponentUtils {
  def newComponent(parent: GameObject, data: Map[String, Any]): Component = {
    val componentType = getComponentType(data)
    val componentClass = componentMap.get(componentType)
    val component = componentClass.get
      .getConstructors()(0)
      .newInstance(parent)
      .asInstanceOf[Component]
    component.loadData(data)
    component
  }

  def getComponentType(data: Map[String, Any]): String = data.get("type") match {
    case optionType: Some[String] =>
      componentMap.get(optionType.get) match {
        case _: Some[_] => optionType.get
        case _ => throw new RuntimeException(s"Type ${optionType.get} is not registered in the component mapping!")
      }
    case _ => throw new RuntimeException(s"Invalid type for field 'type' in object $data")
  }
}
