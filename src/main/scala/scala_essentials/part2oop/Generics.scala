package com.eugeniusz.scala
package scala_essentials.part2oop

object Generics extends App {

  class MyList[+A] {  // A is a type parameter
    // use the type A
    def add[B >: A](element: B): MyList[B] = ???  // B is a type parameter
    /*
      A = Cat
      B = Dog = Animal
     */
  }

  class MyMap[Key, Value]
  val listOfIntegers = new MyList[Int]  // Int is a type argument
  val listOfStrings = new MyList[String]  // String is a type argument

  // generic methods
  object MyList {
    def empty[A]: MyList[A] = ???
  }
  val emptyListOfIntegers = MyList.empty[Int]

  // variance problem

  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // 1. yes, List[Cat] extends List[Animal] = COVARIANCE
  class CovariantList[+A]  // +A means that we can use subtypes of A
  val animal: Animal = new Cat  // this is ok
  val animalList: CovariantList[Animal] = new CovariantList[Cat] // this is ok

  // animalList.add(new Dog) ??? HARD QUESTION => we return a list of animals

  // 2. NO = INVARIANCE

  class InvariantList[A]  // invariant means that we can't use subtypes
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]  // this is ok

  // 3. Hell, no! CONTRAVARIANCE

  class Trainer[-A]  // -A means that we can use supertypes of A
  val trainer: Trainer[Cat] = new Trainer[Animal]  // this is ok

  // bounded types

  class Cage[A <: Animal](animal: A)  // <: means that we can use only subtypes of Animal
  val cage = new Cage(new Dog)

  class Car
  // generic type needs proper bounded type
  // val newCage = new Cage(new Car)  // this is not ok

  // expand MyList to be generic
}
