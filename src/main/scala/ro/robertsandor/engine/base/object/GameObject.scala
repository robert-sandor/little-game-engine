package ro.robertsandor.engine.base.`object`

import org.apache.commons.lang3.StringUtils.EMPTY
import ro.robertsandor.engine.base.component.{Component, ComponentData}
import ro.robertsandor.engine.base.transform.{Transform, Vec}
import ro.robertsandor.engine.mapping.Mapping

case class GameObject(parentFullName: String,
                      name: String,
                      fullName: String,
                      components: List[ComponentData],
                      children: List[GameObject],
                      transform: Transform = Transform()) {
}

object GameObject {
  def getComponentData[T <: ComponentData](gameObject: GameObject): Option[T] = {
    try {
      Some(gameObject.components.filter(component => component.isInstanceOf[T]).head.asInstanceOf[T])
    } catch {
      case _: NoSuchElementException => None
    }
  }

  def getParentName(gameObject: GameObject): String = {
    if (gameObject.parentFullName.isEmpty) {
      gameObject.parentFullName
    } else {
      gameObject.parentFullName.split("/").last
    }
  }

  def newComponent(componentData: Map[String, _], parentName: String): ComponentData = {
    componentData.get("type") match {
      case Some(componentType: String) => Mapping.componentMap.get(componentType) match {
        case Some(component: Component[_]) =>
          component.newComponentData(componentData)
        case _ => throw new IllegalArgumentException(s"Component type $componentType has not been declared in the mapping")
      }
      case _ => throw new IllegalArgumentException(s"Invalid data for component $componentData")
    }
  }

  def newGameObject(data: Map[String, _], parent: Option[String]): GameObject = {
    println(data)
    val parentName = parent.getOrElse(EMPTY)

    val name = data.getOrElse("name", throw new IllegalArgumentException("GameObject must have a name!"))
      .asInstanceOf[String]

    val fullName = s"$parentName/$name"

    val children = data.get("children") match {
      case Some(childrenList: List[Map[String, _]]) => childrenList
        .map(childData => newGameObject(childData, Some(fullName)))
      case _ =>
        throw new IllegalArgumentException("GameObject children data should be a list of maps!")
    }

    val components = data.get("components") match {
      case Some(componentList: List[Map[String, _]]) => componentList.map(componentData => newComponent(componentData, fullName))
      case _ => throw new IllegalArgumentException("GameObject component data should be a list of maps!")
    }

    val transform = data.get("transform") match {
      case Some(trans: Map[String, _]) => Transform.newTransform(trans)
      case _ => Transform()
    }

    GameObject(parentName, name, fullName, components, children, transform)
  }
}