package ro.robertsandor.engine.mapping


import ro.robertsandor.engine.components.Component
import ro.robertsandor.engine.services.Service

/**
  * Created by robert on 5/22/17.
  */
object Mapping {
  var componentMap: Map[String, Class[_ <: Component]] = Map()

  var serviceMap: Map[String, Class[_ <: Service]] = Map()
}
