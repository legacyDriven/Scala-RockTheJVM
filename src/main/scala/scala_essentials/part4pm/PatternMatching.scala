package com.eugeniusz.scala
package scala_essentials.part4pm

import scala.util.Random

object PatternMatching extends App {

  // switch on steroids
  val random = new Random
  val x = random.nextInt(10)

  val description = x match {
    case 1 => "the ONE"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else" // _ = WILDCARD
  }

  println(x)
  println(description)

  // 1. Decompose values
  case class Person(name: String, age: Int)
  val bob = Person("Bob", 20)

  val greeting = bob match {
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I can't drink in the US"  // guards
    case Person(n, a) => s"Hi, my name is $n and I am $a years old"  // guards
    case _ => "I don't know who I am"  // guards, wildcard
  }

  println(greeting)

  /*
    1. cases are matched in order
    2. what if no cases match? MatchError
    3. type of the PM expression? unified type of all the types in all the cases
    4. PM works really well with case classes
   */

  // PM on sealed hierarchies

}
