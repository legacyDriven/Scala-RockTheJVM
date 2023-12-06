package com.eugeniusz.scala
package scala_essentials.part2oop

object InheritanceAndTraits extends App {

  // single class inheritance
  sealed class Animal { //sealed - only extend in THIS FILE

    val creatureType = "wild"
    def eat = println("nomnom")
    private def woof = println("woof-woof") //private members not accessible from outside
    protected def drink = println("slurp") //protected members accessible from subclasses
  }

  class Cat extends Animal {
    def crunch = {
      drink
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.crunch

  //constructors
  class Person(name: String, age: Int) { //primary constructor
    def this(name: String) = this(name, 0)  //auxiliary constructor
  }

  class Adult(name: String, age: Int, idCard: String) extends Person(name) //primary constructor of Person is called

  //overriding
  class Dog(override val creatureType: String) extends Animal {
    //override val creatureType: String = "domestic"
    override def eat = {
      super.eat
      println("crunch, crunch")
    }
  }

  val dog = new Dog("domestic")
  dog.eat
  println(dog.creatureType)

  //type substitution (broad: polymorphism)

  val unknownAnimal: Animal = new Dog("K9")
  unknownAnimal.eat

  //overRIDING vs overLOADING

  //super

  //preventing overrides
  //1 - use final on member
  //2 - use final on class - no inheritance
  //3 - seal the class = extend classes in THIS FILE, prevent extension in other files

}
