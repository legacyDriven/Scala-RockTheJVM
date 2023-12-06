package com.eugeniusz.scala
package scala_essentials.part2oop

object AbstractDataTypes extends App {

  // abstract
  abstract class Animal {  // can not be instantiated

    val creatureType: String = "wild" // abstract field
    def eat: Unit // abstract method
  }

  class Dog extends Animal {

    override val creatureType: String = "Canine"
    override def eat: Unit = println("crunch crunch")
  }

  // traits
  trait Carnivore {
    def eat(animal: Animal): Unit
    val preferredMeal: String = "fresh meat"

  }

  trait ColdBlooded
  class Crocodile extends Animal with Carnivore with ColdBlooded {

    override val creatureType: String = "croc"
    override def eat: Unit = println("nomnomnom")
    override def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog)

  // traits vs abstract classes

  // 1 - traits do not have constructor parameters
  // 2 - multiple traits may be inherited by the same class
  // 3 - traits = behavior, abstract class = "thing"


}
