package com.eugeniusz.scala
package scala_essentials.part1basics

import scala.annotation.tailrec

object Functions extends App{

  def aFunction(a: String, b: Int) = {
    a + " " + b
  }

  println(aFunction("Hello", 3))

  def aParameterlessFunction(): Int = 42 //can not be called without parentheses
  def otherParameterLessFunction: Int = 88 //can be called without parentheses
  println(aParameterlessFunction())
  println(otherParameterLessFunction)

  //RECURSION
  def aRepeatedFunction(aString: String, n: Int): String = {
    if (n == 1) aString
    else aString + aRepeatedFunction(aString, n - 1)
  }

  println(aRepeatedFunction("Hello", 3))

  //WHEN YOU NEED LOOPS, USE RECURSION

  def aFunctionWithSideEffects(aString: String): Unit = println(aString)
  aFunctionWithSideEffects("Hello")

  def aBigFunction(n: Int): Int = {
    def aSmallerFunction(a: Int, b: Int): Int = a + b

    aSmallerFunction(n, n - 1)
  }

  /*
  1. A greeting function (name, age) => "Hi, my name is $name and I am $age years old"
  2. Factorial function 1 * 2 * 3 * .. * n
  3. A Fibonacci function
    f(1) = 1
    f(2) = 1
    f(n) = f(n - 1) + f(n - 2)
  4. Tests if a number is prime
   */

  def greetingFunction(name: String, age: Int): String = s"Hi, my name is $name and I am $age years old"
  println(greetingFunction("Eugeniusz", 25))

  def factorial(n: Int): Int = {
    if (n <=0) 1
    else n * factorial(n - 1)
  }

  println(factorial(5))  // 1 * 2 * 3 * 4 * 5 = 120

  def fibonacci(n: Int): Int = {
    if (n <= 1) 1
    else fibonacci(n - 1) + fibonacci(n - 2)
  }

  println(fibonacci(1))
  println(fibonacci(2))
  println(fibonacci(3))
  println(fibonacci(4))

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeUntil(t: Int): Boolean = {
      if (t <= 1) true
      else n % t != 0 && isPrimeUntil(t - 1)
    }

    isPrimeUntil(n / 2)
  }

  println(isPrime(37)) // true
  println(isPrime(2003)) // true
  println(isPrime(37 * 17)) // false
  
  
}
