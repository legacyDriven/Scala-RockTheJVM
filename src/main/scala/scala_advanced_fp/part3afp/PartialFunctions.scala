package com.eugeniusz.scala
package scala_advanced_fp.part3afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  // partial function from Int to Int / a proper function
  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1, 2, 5} => Int

  // Partial function notation
  val aPartialFunction: PartialFunction[Int, Int] = {   // partial function value (literal)
    case 1 => 42  // partial function value
    case 2 => 56
    case 5 => 999
  } // partial function value (literal)

  println(aPartialFunction(2))
  // println(aPartialFunction.apply(57273)) // will crash with MatchError

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))  // false
  println(aPartialFunction.isDefinedAt(2))  // true

  // lift
  val lifted = aPartialFunction.lift // Int => Option[Int], turns partial function into total function
  println(lifted(2))  // Some(56)
  println(lifted(98)) // None, will not crash if the value is not defined

  // orElse
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2)) // 56
  println(pfChain(45)) // 67

  // PF extends normal functions, PF is subtype of total function
  val aTotalFunction: Int => Int = {  // partial function value (literal)
    case 1 => 99
  }

  // HOFs accept partial functions as well
  val aMappedList = List(1, 2, 3).map {  // will crash if partial function is not defined for all elements
    case 1 => 42  // partial function value
    case 2 => 56
    case 3 => 999
  } // partial function value (literal)

  println(aMappedList) // List(42, 56, 999)

  /*
    Note: PF can only have ONE parameter type
   */

  /**
   * Exercises
   *
   * 1 - construct a PF instance yourself (anonymous class)
   * 2 - dumb chatbot as a PF
   *
   */

  // 1
  val anonymousPartialFunction: PartialFunction[Int, Int] = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 || x == 5  // partial function value, and for value 44 it will return false

    override def apply(arg: Int): Int = arg match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
      case 44 => 1000
    }
  }

  println(anonymousPartialFunction(2)) // 56
  println(anonymousPartialFunction(44)) // 1000
//  println(anonymousPartialFunction(57273)) // MatchError

  // 2
  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "Once you start talking to me, there is no return, human!"
    case "call mom" => "Unable to find your phone without your credit card"
  }

//  scala.io.Source.stdin.getLines().foreach(line => println("You said: " + line))

  scala.io.Source.stdin.getLines().foreach(line => println("You said: " + chatbot(line)))  // will crash if the value is not defined
  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)  // same as above, will crash if the value is not defined

  // PFs take as argument only a subset of the given domain


}
