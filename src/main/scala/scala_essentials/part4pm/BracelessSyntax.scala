package com.eugeniusz.scala
package scala_essentials.part4pm

object BracelessSyntax {

  // if - expressions

  val anIfExpression = if (2 > 3) "bigger" else "smaller"

  // java style
  val anIfExpression2 =
    if (2 > 3) {
      "bigger"
    } else {
      "smaller"
    }

  // compact  - scala 2 compatible, same as above
  val anIfExpression3 =
    if (2 > 3) "bigger"
    else "smaller"

  // scala 3
  val anIfExpression4 =
    if 2 > 3 then
      "bigger"  // no need for curly braces, higher indentation is required, imaginary code block
    else
      "smaller"

  val anIfExpression5 =
    if 2 > 3 then
      val result = "bigger"
      result
    else
      val result = "smaller"
      result

  // scala 3 one liner
  val anIfExpression6 = if 2 > 3 then "bigger" else "smaller"

  // for - comprehensions
  val aForComprehension = for {
    n <- List(1,2,3)
    s <- List("white", "black")
  } yield s"$n-$s"

  // scala 3 no need for curly braces
  val aForComprehension2 = for
    n <- List(1,2,3)
    s <- List("white", "black")
  yield s"$n-$s"

  // pattern matching
  val meaningOfLife = 42
  val aPatternMatching = meaningOfLife match {
    case 42 => "the meaning of life"
    case 2 => "double or nothing"
    case _ => "something else"
  }

  // scala 3
  val aPatternMatching2 = meaningOfLife match
    case 42 => "the meaning of life"
    case 2 => "double or nothing"
    case _ => "something else"

  // methods without braces
  def computeMeaningOfLife(arg: Int): Int = {
    val partialResult = 40



    partialResult + 2
  }  // is still valid if we remove curly braces

  // class definition with significant indentation (same for objects, traits, case classes, enums)
  class Animal:  // compiler expects the body of the class to be indented
    def eat(): Unit = println("nomnom")

    def sleep(): Unit = println("zzzzzz")

    // 3000 lines of code
  end Animal  // end keyword is used to end the class definition, same for objects, traits, case classes, enums

  // anonymous classes
  val someAnimal = new Animal:
    override def eat(): Unit = println("chomp chomp")

  // indentation = strictly larger indentation
  // 3 spaces vs 2 tabs > 2 spaces and 2 tabs
  // 3 spaces vs 2 tabs > 3 spaces and 1 tab
  // 3 tabs + 2 spaces ??? 2 tabs + 3 spaces - compiler will get confused - do not mix tabs and spaces

  def main(args: Array[String]): Unit = {
    println(anIfExpression)
    println(anIfExpression2)
    println(anIfExpression3)
    println(anIfExpression4)
    println(anIfExpression5)
    println(anIfExpression6)
    println(aForComprehension)
    println(aForComprehension2)
    println(aPatternMatching)
    println(aPatternMatching2)
  }




}
