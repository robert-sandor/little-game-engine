package ro.robertsandor.engine.components

import org.apache.commons.lang3.StringUtils.EMPTY
import ro.robertsandor.engine.utils.ComponentUtils.getComponentType
import ro.robertsandor.engine.objects.GameObject

/**
  * Created by robert on 5/22/17.
  */
abstract class Component(parent: GameObject) {
  protected var _type: String = EMPTY

  def getParent: GameObject = parent

  def getType: String = _type

  def loadData(data: Map[String, Any]): Unit = {
    _type = getComponentType(data)
  }

  def update()
}
