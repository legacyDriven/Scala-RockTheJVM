package com.eugeniusz.scala
package scala_essentials.part1basics

object Expressions extends App{

  val x: Int = 1 + 2 // Expression
  println(x)

  println(2 + 3 * 4)
  // + - * / & | ^ << >> >>> (right shift with zero extension)

  println(1 == x)
  // == != > >= < <=

  println(!(1 == x))

  var aVariable = 2
  aVariable += 3 // also works with -= *= /= ... side effects
  println(aVariable)

  // Instructions (DO) vs Expressions (VALUE)

  // IF expression
  val aCondition: Boolean = true
  val aConditionedValue: Int = if (aCondition) 5 else 3 // IF expression
  println(aConditionedValue)
  println(if (aCondition) 5 else 3)
  println(1 + 3)

  var i = 0
  val aWhile: Unit = while (i < 10) {
    println(i)
    i += 1
  }

  // NEVER WRITE THIS AGAIN^^ - DO NOT WRITE IMPERATIVE CODE IN SCALA

  // EVERYTHING in Scala is an Expression!

  val aWeirdValue: Unit = (aVariable = 3) // Unit === void
  println(aWeirdValue)  // () - value of type Unit
  
  // side effects: println(), whiles, reassigning
  
  // Code blocks
  
  val aCodeBlock: String = {
    val y: Int = 2
    val z: Int = y + 1

    if (z > 2) "hello" else "goodbye"  // value of the block is the value of the last expression
  }
  
  // 1. difference between "hello world" vs println("hello world")? String and Unit(Side effect)
  // 2.
  val someValue: Boolean = {
    2 < 3
  } // true
  
  val someOtherValue: Int = {
    if (someValue) 239 else 986
    42
  } // 42 - last expression is 42
  
  println(someValue)
  println(someOtherValue)
  
}
