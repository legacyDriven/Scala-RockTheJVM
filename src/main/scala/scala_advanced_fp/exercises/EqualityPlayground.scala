package com.eugeniusz.scala
package scala_advanced_fp.exercises

import scala_advanced_fp.part5implicits.TypeClasses.User

object EqualityPlayground extends App {
  /*
      Equality exercise
    type class to test equality, and instances that compare users by name, and by name and email
     */

  // TYPE CLASS
  trait Equal[T] {
    def apply(value: T, anotherValue: T): Boolean
  }

  // TYPE CLASS INSTANCE
  implicit object NameEquality extends Equal[User] {
    override def apply(user: User, anotherUser: User): Boolean = user.name == anotherUser.name
  }

  // TYPE CLASS INSTANCE
  object FullEquality extends Equal[User] {
    override def apply(user: User, anotherUser: User): Boolean = user.name == anotherUser.name && user.email == anotherUser.email
  }

  object Equal {
    def apply[T](value: T, anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, anotherValue)
  }

  val john: User = User("John", 32, "john@rtjvm.com")
  val anotherJohn: User = User("John", 45, "anotherjohn@rtjvm.com")
  println(Equal.apply(john, anotherJohn))

  // AD-HOC polymorphism

  /*
    Exercise - improve the Equal TC with an implicit conversion class
    ===(anotherValue: T)
    !==(anotherValue: T)
   */

  implicit class TypeSafeEqual[T](value: T){
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(value, anotherValue)

    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean =
      ! equalizer.apply(value, anotherValue)

  }

  println(john === anotherJohn)
  /*
    john.===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)
    new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)
   */

  /*
    TYPE SAFE
   */
//  println(john == 43)  // scala 3 added multiversal equality, and john == 42 will not compile
//  println(john === 43) // TYPE SAFE, compiler will not compile this code


}
