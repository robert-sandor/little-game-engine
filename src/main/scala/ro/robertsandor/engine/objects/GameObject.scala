package ro.robertsandor.engine.objects

import org.apache.commons.lang3.StringUtils.EMPTY
import ro.robertsandor.engine.components.Component
import ro.robertsandor.engine.utils.GameObjectUtils.generateFullName
import ro.robertsandor.engine.utils.{ComponentUtils, GameObjectUtils}

import scala.collection.JavaConverters._

/**
  * Created by robert on 5/22/17.
  */
class GameObject(parent: Option[GameObject]) {
  private var name: String = EMPTY
  private var fullName: String = EMPTY
  private var components: Map[String, Component] = Map()
  private var children: Map[String, GameObject] = Map()

  def getComponents: Map[String, Component] = components

  def getChildren: Map[String, GameObject] = children

  def getName: String = name

  def getFullName: String = fullName

  def getParent: Option[GameObject] = parent

  def loadName(data: Map[String, Any]): Unit = {
    data.get("name") match {
      case nameData: Some[String] => {
        name = nameData.get
        fullName = generateFullName(this)
      }
      case _ => throw new RuntimeException(s"The gameobject $data does not have a name field!");
    }
  }

  def loadData(data: Map[String, Any]): Unit = {
    loadName(data)
    loadComponents(data)
    loadChildren(data)
  }

  def loadChildren(data: Map[String, Any]): Unit = {
    data.get("children") match {
      case childrenList: Some[java.util.List[java.util.Map[String, Any]]] =>
        childrenList.get.asScala.foreach(childData => {
          val gameObject: GameObject = new GameObject(Some(this))
          gameObject.loadData(childData.asScala.toMap)
          children += (gameObject.name -> gameObject)
        })
      case _ =>
    }
  }

  def loadComponents(data: Map[String, Any]): Unit = {
    data.get("components") match {
      case componentsList: Some[java.util.List[java.util.Map[String, Any]]] =>
        componentsList.get.asScala.foreach(componentData => {
          val component = ComponentUtils.newComponent(this, componentData.asScala.toMap)
          components += (component.getType -> component)
        })
      case _ =>
    }
  }
}
