package com.eugeniusz.scala
package scala_essentials.part2oop

object CaseClasses extends App {

  /*
   * equals, hashCode, toString
   */

  case class Person(name: String, age: Int)  // case classes are immutable by default

  // 1. class parameters are fields
  val jim = new Person("Jim", 34)
  println(jim.name)  // Jim - name param is a field

  // 2. sensible toString
  // println(instance) = println(instance.toString)  // syntactic sugar
  println(jim.toString)  // Person(Jim,34)
  println(jim)  // Person(Jim,34), same as above

  // 3. equals and hashCode implemented OOTB (out of the box)
  val jim2 = new Person("Jim", 34)
  println(jim == jim2)  // true

  // 4. CCs have handy copy method
  val jim3 = jim.copy(age = 45)  // copy method takes named params
  println(jim3)  // Person(Jim,45)

  // 5. CCs have companion objects
  val thePerson = Person  // companion object
  val mary = Person("Mary", 23)  // apply method is called on the companion object
  // we usually dont use new keyword when creating instances of CCs, we use the apply method instead
  
  // 6. CCs are serializable
  // Akka framework - distributed computing framework, uses CCs a lot
  
  // 7. CCs have extractor patterns = CCs can be used in PATTERN MATCHING
  
  case object UnitedKingdom {  // case objects are also CCs but they dont have companion objects because they are their own companion objects
    def name: String = "The UK of GB and NI"
  }
  
  /*
   * Expand MyList - use case classes and case objects
   */

}
