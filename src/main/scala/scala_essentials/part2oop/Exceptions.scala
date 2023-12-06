package com.eugeniusz.scala
package scala_essentials.part2oop

object Exceptions extends App {

  val x: String = null

  //  println(x.length)
  // this ^^ will crash with a NPE

  // 1. throwing exceptions

  //  val weirdValue: Nothing = throw new NullPointerException  // Nothing is a subtype of every other type
  //  val otherWeirdValue: String = throw new NullPointerException

  // throwable classes extend the Throwable class.
  // Exception and Error are the major Throwable subtypes

  // 2. how to catch exceptions
  def getInt(withExceptions: Boolean): Int =
    if (withExceptions) throw new RuntimeException("No int for you!")  // throw an exception
    else 42

  val potentialFail = try {
    // code that might throw
    getInt(false)
  } catch {
    case e: RuntimeException => 43 //println("caught a Runtime exception")
  } finally {
    // code that will get executed NO MATTER WHAT
    // optional
    // does not influence the return type of this expression
    // use finally only for side effects
    println("finally")
  }

  println(potentialFail)

  // 3. how to define your own exceptions

  class MyException extends Exception
  val exception = new MyException

  //  throw exception

  /*
    1. Crash your program with an OutOfMemoryError
    2. Crash with SOError
    3. PocketCalculator
      - add(x,y)
      - subtract(x,y)
      - multiply(x,y)
      - divide(x,y)

      Throw
        - OverflowException if add(x,y) exceeds Int.MAX_VALUE
        - UnderflowException if subtract(x,y) exceeds Int.MIN_VALUE
        - MathCalculationException for division by 0
   */

  // OOM

  //  val array = Array.ofDim[Int](Int.MaxValue) // OutOfMemoryError - heap space, needed to add [Int] to specify the type

  // SO

  def infinite: Int = 1 + infinite  // SOError
  //  val noLimit = infinite  // SOError

  class OverflowException extends RuntimeException
  class UnderflowException extends RuntimeException
  class MathCalculationException extends RuntimeException("Division by 0")

  object PocketCalculator {
    def add(x: Int, y: Int): Int = {
      val result = x + y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException  // if the result is negative, then we have an overflow
      else if (x < 0 && y < 0 && result > 0) throw new UnderflowException  // if the result is positive, then we have an underflow
      else result
    }

    def subtract(x: Int, y: Int): Int = {
      val result = x - y
      if (x > 0 && y < 0 && result < 0) throw new OverflowException  // if the result is negative, then we have an overflow
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException  // if the result is positive, then we have an underflow
      else result
    }

    def multiply(x: Int, y: Int): Int = {
      val result = x * y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException  // if the result is negative, then we have an overflow
      else if (x < 0 && y < 0 && result < 0) throw new OverflowException  // if the result is negative, then we have an overflow
      else if (x > 0 && y < 0 && result > 0) throw new UnderflowException  // if the result is positive, then we have an underflow
      else if (x < 0 && y > 0 && result > 0) throw new UnderflowException  // if the result is positive, then we have an underflow
      else result
    }

    def divide(x: Int, y: Int): Int = {
      if (y == 0) throw new MathCalculationException
      else x / y
    }
  }
  
  //  println(PocketCalculator.add(Int.MaxValue, 10))
  //  println(PocketCalculator.divide(2, 0))
}
