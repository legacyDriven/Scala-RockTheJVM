package com.eugeniusz.scala
package scala_advanced_fp.part2afp

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
  println(lifted(98)) // None

  // orElse
  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

}
