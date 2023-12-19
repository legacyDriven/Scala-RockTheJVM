package com.eugeniusz.scala
package scala_advanced_fp.part2afp

object CurriesPAF extends App {

  // curried function
  val superAdder: Int => Int => Int =
    x => y => x + y
  val add3 = superAdder(3)  // Int => Int = y => 3 + y
  println(add3(5))  // partially applied function
  println(superAdder(3)(5))  // curried function

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y
  val add4: Int => Int = curriedAdder(4)  // won't work without the type annotation, because compiler can't infer the type

  println(add4(5))  // partially applied function
  println(curriedAdder(4)(5))  // curried method

  // lifting = ETA-EXPANSION is the process of turning a method into a function value
  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1  // method
  List(1, 2, 3).map(inc) // ETA-expansion done by compiler, because map expects a function, not a method

  // Partial function applications
  val add5 = curriedAdder(5) _ // Int => Int, underscore is needed to tell the compiler to do ETA-expansion, so no type annotation is needed
  println(add5(6))

  // Exercise
  val simpleAddFunction = (x: Int, y: Int) => x + y  // Int => Int => Int
  def simpleAddMethod(x: Int, y: Int) = x + y  // Int => Int => Int
  def curriedAddMethod(x: Int)(y: Int) = x + y  // Int => Int => Int

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!
  val add7 = (x: Int) => simpleAddFunction(7, x)  // simplest
  val add7_2 = simpleAddFunction.curried(7)  // works as well, curried method is defined in Function2 trait and it's a syntactic sugar
  val add7_6 = simpleAddFunction(7, _: Int)  // alternative syntax for turning methods into function values, PAF = partial applied function
  val add7_3 = curriedAddMethod(7) _  // PAF
  val add7_4 = curriedAddMethod(7)(_)  // PAF = alternative syntax
  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values
                                          // y => simpleAddMethod(7, y)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c
  val insertName = concatenator("Hello, I'm ", _: String, ", how are you?")  // x: String => concatenator("Hello, I'm ", x, ", how are you?")
  println(insertName("Eugeniusz"))  // Hello, I'm Eugeniusz, how are you?

  val fillInTheBlanks = concatenator("Hello, ", _: String, _: String)  // (x, y) => concatenator("Hello, ", x, y)
  println(fillInTheBlanks("Eugeniusz", " Scala is awesome!"))  // Hello, Eugeniusz Scala is awesome!

  // Exercises
  /*
    1. Process a list of numbers and return their string representations with different formats
       Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
   */
  // %8.6f 8 chars total, 6 decimal precision
  // %14.12f 14 chars total, 12 decimal precision

  println("%4.2f".format(Math.PI))  // 3.14
  println("%8.6f".format(Math.PI))  // 3.141593
  println("%14.12f".format(Math.PI))  // 3.141592653590

  def curriedFormatter(s: String)(number: Double): String = s.format(number)
  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
  val simpleFormat = curriedFormatter("%4.2f") _  // lift method to function value
  val seriousFormat = curriedFormatter("%8.6f") _  // lift
  val preciseFormat = curriedFormatter("%14.12f") _  // lift

  println(numbers.map(simpleFormat))  // List(3.14, 2.72, 1.00, 9.80, 0.00)
  println(numbers.map(seriousFormat))  // List(3.141593, 2.718282, 1.000000, 9.800000, 0.000000)
  println(numbers.map(preciseFormat))  // List(3.141592653590, 2.718281828459, 1.000000000000, 9.800000000000, 0.000000000001)

  println(numbers.map(curriedFormatter("%14.12f")))  // alternative syntax, compiler does ETA-expansion for us

  /*
    2. Difference between
       - functions vs methods
       - parameters: by-name vs 0-lambda - common source of confusion
   */

  def byName(n: => Int): Int = n + 1  // n is evaluated every time is used
  def byFunction(f: () => Int): Int = f() + 1  // f is evaluated only once

  def method: Int = 42  // method
  def parenthesesMethod(): Int = 42

  /*
    calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
   */

  byName(23)  // ok
  byName(method)  // ok, because method is evaluated to 42
  byName(parenthesesMethod())  // will not work without the parentheses, because compiler will try to pass the method itself, not the result of the method
//  byName(parenthesesMethod)  // ok, but beware ==> byName(parenMethod())
  
  // byName(() => 42)  // not ok
  byName((() => 42)())  // ok, because we are passing the result of the lambda
  // byName(parenMethod _)  // not ok, because compiler will try to pass the method itself, not the result of the method
  
  // byFunction(45)  // not ok
  // byFunction(method)  // not ok, because compiler will try to pass the method itself, not the result of the method
  byFunction(parenthesesMethod)  // compiler does ETA-expansion for us
  byFunction(() => 46)  // ok
  byFunction(parenthesesMethod _)  // also works, but warning - unnecessary underscore

}
