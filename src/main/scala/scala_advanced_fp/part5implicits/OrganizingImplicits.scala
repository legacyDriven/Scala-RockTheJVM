package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import scala_essentials.part4pm.PatternsEverywhere.a

object OrganizingImplicits extends App {

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)  // will take precedence over ordering from .predef
//  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)   // this will confuse compiler as there will be two within the scope

  println(List(1,4,5,3,2).sorted)

  // scala. predef

  /*
    Implicits:
      - val/var
      - object
      - accessor methods = defs with no parentheses
   */

  // Exercise
  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

//  object Person {  // will work as a Person companion object but will not work for person if object name is different
//    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
//  }

  // this will take precedence over the companion object for Person class
//  implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age.compareTo(b.age) < 0)

//  println(persons.sorted)

  /*
    Implicit scope priority order:
      -normal scope = LOCAL scope
      -imported scope
      -companion objects of all types involved in the method signature
        -List
        -Ordering
        -all the types involved = A or any supertype

  //  def sorted[B >: A](implicit ord: Ordering[B]): List[B]=
   */

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age.compareTo(b.age) < 0)
  }

  import AgeOrdering._
  println(persons.sorted)

  /*
  Best practises:
  When defining an implicit val:
  #1
    * if there is a SINGLE possible value for it
    * and you can edit the code for the type

  then define the implicit in the COMPANION

  #2
    * if there are MANY possible values for it
    * but a single GOOD one
    * and you can edit the code for the type

  then define the GOOD implicit in the companion, and other elsewhere,
    preferably in either the local scope or other objects
   */

  /*
    EXERCISE

    add 3 orderings by 3 different criteria:
    - totalPrice = most used (570% usages)
    - by unit count = 20%
    - by unit price = 10%
   */

  case class Purchase(nUnits: Int, unitPrice: Double)
  private def totalPrice(purchase: Purchase): Double = purchase.nUnits * purchase.unitPrice

//  implicit val totalPriceOrdering2: Ordering[Purchase] = Ordering.fromLessThan((a, b) => totalPrice(a) < totalPrice(b)) // same as below

  object Purchase {  // this is default for Purchase scope
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object UnitCountOrdering { // this should be imported
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {  // same as above, or, if not likely to occure can be declared on the spot and used in the local scope
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

}
