package com.eugeniusz.scala
package scala_advanced_fp.part2as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 42 else 65

  // instructions vs expressions
  val aCodeBlock = {
    if(aCondition) 54
    56
  }  // value of codeblock is the last expression

  // Unit = void
  val theUnit = println("Hello, Scala")

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail
  @tailrec
  def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)
  end factorial

  // object-oriented programming

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // method notarion
  val aCroc = new Crocodile
  aCroc.eat(aDog)  // acceptable
  aCroc eat aDog   // acceptable, more like natural language

  // anonymous classes
  val aCarnivore = new Carnivore:
    override def eat(a: Animal): Unit = println("roar")

  // generics
  abstract class MyList[+A]  // variance and variance problems in THIS course

  // singletons and companions
  object MyList  //companion

  // case classes
  case class Person(name: String, age: Int)  //serializable, immutable

  // exceptions and try/catch/finally
//  val throwsException = throw new RuntimeException // return type is Nothing, which can not be instantiated
  val aPotentialFailure = try {
    throw new RuntimeException()
  } catch {
    case e: Exception => "I caught exception"
  } finally {
    println("some logs")
  }

  // packaging and imports
  
  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  val incremented = incrementer(42)  // 43

  val anonymousIncrementer = (x: Int) => x + 1  // equivalent to the above

  val incrementedList = List(1, 2, 3).map(anonymousIncrementer)  // HOF
  println(incrementedList)  // List(2, 3, 4)

  // map, flatMap, filter

  // for-comprehensions
  val pairs = for {
    num <- List(1, 2, 3)  if num % 2 == 0 // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  println(pairs)  // List(1-a, 1-b, 1-c, 2-a, 2-b, 2-c, 3-a, 3-b, 3-c

  // Scala collections: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 555
  )

  // "collections": Options, Try
  val anOption = Some(2)
  println(anOption.isEmpty)  // false
  println(anOption.get)  // 2

  // pattern matching

  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
    case _ => "I don't know my name"
  }
  
  // all the patterns
  
}
