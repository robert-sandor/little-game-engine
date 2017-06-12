package ro.robertsandor.engine.mapping


import ro.robertsandor.engine.base.component.{Component, ComponentData}
import ro.robertsandor.engine.base.services.{EventService, RenderService, Service}

import scala.collection.mutable

/**
  * Created by robert on 5/22/17.
  */
object Mapping {
  var componentMap: mutable.Map[String, Component[ComponentData]] = mutable.ListMap()

  var serviceMap: mutable.Map[String, Service] = mutable.ListMap()

  def renderService: Option[RenderService] = getServiceByClass(classOf[RenderService])
    .asInstanceOf[Option[RenderService]]

  def getServiceByClass(clazz: Class[_]): Option[Service] = {
    try {
      Some(serviceMap.values.filter(serviceEntry => clazz.isInstance(serviceEntry)).head)
    } catch {
      case _: Throwable => None
    }
  }

  def getServiceByName(name: String): Option[Service] = serviceMap.get(name)

  def eventService: Option[EventService] = getServiceByClass(classOf[EventService])
    .asInstanceOf[Option[EventService]]
}
