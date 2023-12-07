package com.eugeniusz.scala
package scala_essentials.part3fp

object AnonymousFunctions extends App {

  // anonymous class
  val doubler = new Function[Int, Int] {
    override def apply(x: Int) = x * 2
  }

  // anonymous function (LAMBDA)
  val doubler2 = (x: Int) => x * 2

  // multiple params in a lambda
  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b

  // no params
  val justDoSomething: () => Int = () => 3

  println(justDoSomething) // function itself
  println(justDoSomething()) // call

  // curly braces with lambdas

  val stringToInt = { (str: String) =>
    str.toInt
  }

  // MOAR syntactic sugar

  val niceIncrementer: Int => Int = _ + 1 // equivalent to x => x + 1

  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a, b) => a + b

  /*
   1. MyList: replace all FunctionX calls with lambdas
   2. Rewrite the "special" adder as an anonymous function
   */

  val superAdd = (x: Int) => (y: Int) => x + y  // Function1[Int, Function1[Int, Int]]
  println(superAdd(3)(4))


}
