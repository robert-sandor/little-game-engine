package ro.robertsandor.engine.services

import org.apache.commons.lang3.StringUtils.EMPTY
import ro.robertsandor.engine.objects.GameObject

/**
  * Created by robert on 5/22/17.
  */
trait EventManager {
  def addEventListener(gameObject: GameObject, event: String): Unit

  def removeEventListener(gameObject: GameObject, event: String): Unit

  def notifyListeners(event: String, objectName: String = EMPTY)
}
