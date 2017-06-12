package ro.robertsandor.engine.base.transform

import java.lang.Math.{acos, pow, sqrt, toDegrees}

case class Vec(x: Double, y: Double, z: Double = 0.0) {
  def +(that: Vec): Vec = Vec(this.x + that.x, this.y + that.y, this.z + that.z)

  def *(thatScalar: Double): Vec = Vec(thatScalar * this.x, thatScalar * this.y, thatScalar * this.z)

  def distance(that: Vec): Double = (this - that).length

  def -(that: Vec): Vec = Vec(this.x - that.x, this.y - that.y, this.z - that.z)

  def length: Double = sqrt(pow(this.x, 2) + pow(this.y, 2) + pow(this.z, 2))

  def angleDegrees(that: Vec): Double = toDegrees(this angleRadians that)

  def angleRadians(that: Vec): Double = acos(this.normalize dot that.normalize)

  def normalize: Vec = Vec(this.x / this.length, this.y / this.length, this.z / this.length)

  def dot(that: Vec): Double = this.x * that.x + this.y * that.y + this.z * that.z

  def cross(that: Vec): Vec = Vec(
    this.y * that.z - this.z * that.y,
    this.z * that.x - this.x * that.z,
    this.x * that.y - this.y * that.x
  )

  def :*(that: Vec): Vec = Vec(this.x * that.x, this.y * that.y, this.z * that.z)
}

object Vec {
  def fromList(list: List[_]): Vec = fromVector(list.toVector)

  def fromArray(array: Array[_]): Vec = fromVector(array.toVector)

  def fromVector(vector: Vector[_]): Vec = {
    vector.size match {
      case 2 =>
        val values = (vector(0), vector(1))
        values match {
          case (x: BigInt, y: BigInt) => Vec(x.doubleValue(), y.doubleValue())
          case (x: Int, y: Int) => Vec(x, y)
          case (x: Double, y: Double) => Vec(x, y)
          case _ => zeros
        }
      case 3 =>
        val values = (vector(0), vector(1), vector(2))
        values match {
          case (x: BigInt, y: BigInt, z: BigInt) => Vec(x.doubleValue(), y.doubleValue(), z.doubleValue())
          case (x: Int, y: Int, z: Int) => Vec(x, y, z)
          case (x: Double, y: Double, z: Double) => Vec(x, y, z)
          case _ => zeros
        }
      case _ => zeros
    }
  }

  def zeros: Vec = Vec(0, 0, 0)

  def ones: Vec = Vec(1, 1, 1)
}

