package com.eugeniusz.scala
package scala_essentials.part1basics

import scala.annotation.tailrec

object Recursion extends App{

  // StackOverflowError
//  @tailrec
  def factorial(n: Int): Int = {
    if (n <= 1) 1
    else {
      println("Computing factorial of " + n + " - I first need factorial of " + (n-1))
      val result = n * factorial(n-1)  // recursive call
      println("Computed factorial of " + n)  // this line is executed after the recursive call
      result
    }
  }

  println(factorial(10))
  println(factorial(5000))

  def anotherFactorial(n: Int): BigInt = {
    @tailrec
    def factHelper(x: Int, accumulator: BigInt): BigInt = {
      if (x <= 1) accumulator
      else factHelper(x-1, x * accumulator) // TAIL RECURSION = use recursive call as the LAST expression
    }
    factHelper(n, 1)
  }
  /*

  anotherFactorial(10) = factHelper(10, 1)
  = factHelper(9, 10 * 1)
  = factHelper(8, 9 * 10 * 1)
  = factHelper(7, 8 * 9 * 10 * 1)
  = ...
  = factHelper(2, 3 * 4 * ... * 10 * 1)
  = factHelper(1, 2 * 3 * 4 * ... * 10 * 1)
  = 1 * 2 * 3 * 4 * ... * 10  // accumulator = 1
   */

  println(anotherFactorial(10))
  println(anotherFactorial(20000))

  // WHEN YOU NEED LOOPS, USE _TAIL_ RECURSION.

  /*
  1. Concatenate a string n times
  2. IsPrime function tail recursive
  3. Fibonacci function, tail recursive
   */

  // tailrec

  @tailrec
  def concatenateTailrec(aString: String, n: Int, accumulator: String): String = {
    if (n <= 0) accumulator
    else concatenateTailrec(aString, n-1, aString + accumulator)  // accumulator is a String, so it's immutable
  }

  println(concatenateTailrec("hello", 3, ""))
  println(concatenateTailrec("hello", 3, "SATAN"))

  def isPrimeTailrec(n: Int): Boolean = {
    @tailrec
    def isPrimeTailrecHelper(t: Int, isStillPrime: Boolean): Boolean = {
      if (!isStillPrime) false // if isStillPrime is false, then it's not prime
      else if (t <= 1) true // if t is 1 or less, then it's prime
      else isPrimeTailrecHelper(t-1, n % t != 0 && isStillPrime)  // if n % t == 0, then it's not prime
    }
    isPrimeTailrecHelper(n/2, true) // we only need to check up to n/2
  }

  println(isPrimeTailrec(2003))
  println(isPrimeTailrec(629))

  def fibonacci(n: Int): Int = {
    @tailrec
    def fibonacciTailrec(i: Int, last: Int, nextToLast: Int): Int = {
      if (i >= n) last  // if i is greater than or equal to n, then return last
      else fibonacciTailrec(i+1, last + nextToLast, last)  // if i is less than n, then call fibonacciTailrec again
    }
    if (n <= 2) 1  // if n is less than or equal to 2, then return 1
    else fibonacciTailrec(2, 1, 1)  // if n is greater than 2, then call fibonacciTailrec
  }

  println(fibonacci(8))
  println(fibonacci(100))

}
