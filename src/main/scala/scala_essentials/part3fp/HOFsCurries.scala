package com.eugeniusz.scala
package scala_essentials.part3fp

import scala.annotation.tailrec

object HOFsCurries extends App {

  val superFunction: (Int, (String, (Int => Boolean)) => Int) => (Int => Int) = null  // Higher Order Function (HOF)

  // map, flatMap, filter in MyList

  // function that applies a function n times over a value x
  
  // nTimes(f, n, x)
  // nTimes(f, 3, x) = f(f(f(x))) = nTimes(f, 2, f(x))
  // nTimes(f, n, x) = f(f(...f(x))) = nTimes(f, n-1, f(x))

  @tailrec
  def nTimes(f: Int => Int, n: Int, x: Int): Int = {
    if (n <= 0) x
    else nTimes(f, n-1, f(x))  // tail recursion
  }

  val plusOne = (x: Int) => x + 1  // anonymous function (lambda)

  println(nTimes(plusOne, 10, 1))  // plusOne(plusOne(...plusOne(1)))

  // ntb(f, n) = x => f(f(f...(x)))
  // increment10 = ntb(plusOne, 10) = x => plusOne(plusOne...(x))
  // val y = increment10(1)
  def nTimesBetter(f: Int => Int, n: Int): (Int => Int) = {
    if (n <= 0) (x: Int) => x
    else (x: Int) => nTimesBetter(f, n-1)(f(x))  // nTimesBetter(f, n-1) returns a function
  }

  val plus10 = nTimesBetter(plusOne, 10)
  println(plus10(1))

  // curried functions
  val superAdder: Int => (Int => Int) = (x: Int) => (y: Int) => x + y
  val add3 = superAdder(3)  // y => 3 + y
  println(add3(10))
  println(superAdder(5)(10))  // curried function

  // functions with multiple parameter lists
  def curriedFormatter(c: String)(x: Double): String = c.format(x)

  val standardFormat: (Double => String) = curriedFormatter("%4.2f")  // lift
  val preciseFormat: (Double => String) = curriedFormatter("%10.8f")  // lift

  println(standardFormat(Math.PI))  // curriedFormatter("%4.2f")(Math.PI)
  println(preciseFormat(Math.PI))  // curriedFormatter("%10.8f")(Math.PI)
  
  /*
    1. Expand MyList
      - foreach method A => Unit
        [1,2,3].foreach(x => println(x))
      
      - sort function ((A, A) => Int) => MyList
        [1,2,3].sort((x, y) => y - x) => [3,2,1]
      
      - zipWith (list, (A, A) => B) => MyList[B]
        [1,2,3].zipWith([4,5,6], x * y) => [1 * 4, 2 * 5, 3 * 6] = [4, 10, 18]
      
      - fold(start)(function) => a value
        [1,2,3].fold(0)(x + y) = 6
    
    2. toCurry(f: (Int, Int) => Int) => (Int => Int => Int)
       fromCurry(f: (Int => Int => Int)) => (Int, Int) => Int
   
    3. compose(f,g) => x => f(g(x))
       andThen(f,g) => x => g(f(x))
   */

  def toCurry(f: (Int, Int) => Int): (Int => Int => Int) = {
    x => y => f(x, y)  // curried function
  }

  def fromCurry(f: (Int => Int => Int)): (Int, Int) => Int = {
    (x, y) => f(x)(y)  // uncurried function
  }

  // FunctionX

  def compose[A, B, T](f: A => B, g: T => A): T => B = {
    x => f(g(x))
  }

  def andThen[A, B, C](f: A => B, g: B => C): A => C = {
    x => g(f(x))
  }

  def superAdder2: (Int => Int => Int) = toCurry(_ + _)  // x => y => x + y

  def add4 = superAdder2(4)   // y => 4 + y
  println(add4(17))  // 21 = 4 + 17

  val simpleAdder = fromCurry(superAdder)  // (x, y) => x + y
  println(simpleAdder(4, 17))  // 21

  val add2 = (x: Int) => x + 2
  val times3 = (x: Int) => x * 3

  val composed = compose(add2, times3)  // x => add2(times3(x))
  val ordered = andThen(add2, times3)  // x => times3(add2(x))

  println(composed(4))  // 14
  println(ordered(4))  // 18


}
