package com.eugeniusz.scala
package scala_advanced_fp.part2as

import scala.util.Try

object DarkSyntaxSugar extends App {

  // 1. Methods with single parameter
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."
  //  println(singleArgMethod(42))

  val description = singleArgMethod {
    // write some complex code
    42
  }
  println(description)

  val aTryInstance = Try {  // java's try {...}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }

  // syntactic sugar #2: single abstract method
  trait Action {  // SAM
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {  // anonymous class
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 // magic, reduced to lambda

  // example: Runnables
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("Sweet, Scala!"))  // lambda

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("Sweet")  // lambda

  // 3. The :: and #:: methods are special
  val prependedList = 2 :: List(3, 4)
  // 2.::(List(3, 4))
  // List(3, 4).::(2)
  // ?!

  // scala spec: last char decides associativity of method
  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1) // equivalent

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]  // streams are right associative

  // Syntax sugar #4: multi-word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")  // `and then said` is a method name
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"  // infix notation

  // syntax sugar #5: infix types
  class Composite[A, B]
  val composite: Composite[Int, String] = null
  val composite2: Int Composite String = null  // equivalent

  class -->[A, B]  // same as Composite[A, B]
  val towards: Int --> String = null  // same as Int --> String

  // syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7  // rewritten to anArray.update(2, 7)
  // used in mutable collections
  // remember apply() AND update()!

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0  // private for OO encapsulation
    def member: Int = internalMember  // getter
    def member_=(value: Int): Unit = internalMember = value  // setter
  }

  val aMutableContainer = new Mutable
  val aMutableContainer2 = Mutable()  // equivalent to new Mutable
  aMutableContainer.member = 42  // rewritten as aMutableContainer.member_=(42)

}
