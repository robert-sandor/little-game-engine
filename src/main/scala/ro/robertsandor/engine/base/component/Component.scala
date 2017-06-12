package ro.robertsandor.engine.base.component

import ro.robertsandor.engine.base.`object`.GameObject

import scala.reflect._
import scala.reflect.runtime.universe._

trait ComponentData {
  val componentType: String
}

abstract class Component[T: TypeTag : ClassTag] {
  def update(gameObject: GameObject): GameObject

  def newComponentData(componentMap: Map[String, _]): T
//  = {
//    val mirror = runtimeMirror(classTag[T].runtimeClass.getClassLoader)
//    val clazz = typeOf[T].typeSymbol.asClass
//    val classMirror = mirror.reflectClass(clazz)
//    val constructor = typeOf[T].decl(termNames.CONSTRUCTOR).asMethod
//    val constructorMirror = classMirror.reflectConstructor(constructor)
//
//    val constructorArgs = constructor.paramLists.flatten.map((param: Symbol) => {
//      val paramName = param.name.toString
//      if (param.typeSignature <:< typeOf[Option[_]])
//        m.get(paramName)
//      else
//        m.getOrElse(paramName,
//          throw new IllegalArgumentException("Map is missing required parameter named " + paramName))
//    })
//
//    constructorMirror(constructorArgs: _*).asInstanceOf[ComponentData].asInstanceOf[T]
//  }
}