package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit): Unit = {
      def timesAux(n: Int): Unit =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def contatenate(n: Int): List[T] =
        if (n <= 0) List()
        else contatenate((n - 1)) ++ list

      contatenate(value)
    }
  }

  implicit class RicherInt(richInt: RichInt){
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  new RichInt(42).sqrt

  42.isEven  // new RichInt(42).isEven - that's how it works under the hood

  // type enrichment = pimping

  1 to 10

  import scala.concurrent.duration.*
  3.seconds

  // compiler does not to multiple implicit searches
  //  42.isOdd  // will not work, compiler would wrap previous call into RichInt, but will not wrap into another object in another step

  /*
  Enrich the String class
  - asInt
  - encrypt
    "John" -> Lqjp

  Keep enriching the Int class
  - times(function)
    3.times(() => ...)
  - *
    3 * List(1,2) => List(1,2,1,2,1,2)
   */
  implicit class RichString(string: String){
    def asInt: Int = Integer.valueOf(string)  // java.lang.Integer -> Int
    def encrypt(cypherDistance: Int): String = string.map(c => (c + cypherDistance).asInstanceOf[Char])
  }

  println("3".asInt + 4)
  println("John".encrypt(2))

  3.times(() => println("Scala Rocks!"))
  println(4 * List(1,2))

  // "3" / 4
  implicit def stringToInt(string: String): Int = Integer.valueOf(string)

  println("6" / 2)  // stringToInt("6") / 2

  // equivalent: implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)  // implicit conversion method

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  /*
  if (n) do something
  else do something else
   */

  val aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)

  /*
  Tips:
  - keep type enrichment to implicit classes and type classes
  - avoid implicit defs as much as possible
  - package implicits clearly, bring into scope only what you need
  - if you need conversions, make them specific
   */


}
