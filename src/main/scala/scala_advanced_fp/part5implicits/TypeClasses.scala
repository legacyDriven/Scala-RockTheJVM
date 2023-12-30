package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import scala_essentials.part1basics.StringOps.{age, name}

import java.util.Date

object TypeClasses extends App {

  // type class is a trait that takes a type and describes what operations can be applied to that

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age) yo <a href=$email/> </div>"
  }

  User("John", 32, "john@rockthejvm.com")
  /*
    Disadvantages:
    1 - only works for the types WE write
    2 - ONE implementation out of quite a number
   */

  // option 2 - pattern matching
  object HTMLSerializerPM {
    def serializeToHtml(value: Any): Unit = value match{
      case User(n,a,e) =>
//      case java.util.Date =>
      case _ =>
    }
  }
  /*
    1 - lost type safety
    2 - need to modify the code every time
    3 - still ONE implementation
   */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User]{
    def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val john = User("John", 32, "john@rockthejvm.com")

  println(UserSerializer.serialize(john))

  // 1 - we can define serializers for other types
  import java.util.Date
  object DateSerializer extends HTMLSerializer[Date]{ // type class instance
    override def serialize(date: Date): String = s"<div>${date.toString()} </div>"
  }

  // 2 - we can define MULTIPLE serializers
  object PartialUserSerializer extends HTMLSerializer[User]{  // type class instance
    override def serialize(user: User) = s"<div>${user.name} </div>"
  }

  // HtmlSerializer is called - TYPE CLASS
  // which specifies a set of operations (serialize()) that can be applied to a given type

  // so, anyone, who extends HTMLSerializer needs to provide this functionality
  // and all the implementers of a type class are called TYPE CLASS INSTANCES

  // there is no reason to implement them many times, that's why we use singleton object


  // TYPE CLASS
  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]): MyTypeClassTemplate[T] = instance
  }

  /*
    Equality exercise
  type class to test equality, and instances that compare users by name, and by name and email
   */

  // TYPE CLASS
  trait Equal[T] {
    def apply(value: T, anotherValue: T): Boolean
  }

  // TYPE CLASS INSTANCE
  implicit object NameEquality extends Equal[User]{
    override def apply(user: User, anotherUser: User): Boolean = user.name == anotherUser.name
  }
  // TYPE CLASS INSTANCE
  object FullEquality extends Equal[User]{
    override def apply(user: User, anotherUser: User): Boolean = user.name == anotherUser.name && user.email == anotherUser.email
  }

  // PART 2
  // we can just call method from parent object (serializer.serialize) and once we have defined some implicit objects
  // the compiler will fetch them out for us, like with IntSerializer

  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"div style: color=blue>$value</div"
  }

  // access to the entire type class interface
  println(HTMLSerializer.serialize(42))
  println(HTMLSerializer.serialize(john))  // call implicit from line 45

  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(john))  // same as above

  /*
  Exercise: implement the TC pattern for the Equality tc.
   */

  object Equal {
    def apply[T](value: T, anotherValue: T)(implicit equalizer: Equal[T] ): Boolean = equalizer.apply(value, anotherValue)
  }

  val anotherJohn = User("John", 45, "anotherjohn@rtjvm.com")
  println(Equal.apply(john, anotherJohn))
  
  // AD-HOC polymorphism

}
