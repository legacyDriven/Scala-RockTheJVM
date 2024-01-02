package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import scala_essentials.part1basics.StringOps.{age, name}

import com.eugeniusz.scala.scala_advanced_fp.exercises.EqualityPlayground.Equal

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

  // PART 3

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(john.toHTML(UserSerializer))  // println(new HTMLEnrichment[User](john).toHtml(UserSerializer))
  println(john.toHTML)  // same as above

  /*
  COOL!
    - extend to new types
    - choose implementation
    - super expressive!
   */

  println(2.toHTML)  // println(new HTMLEnrichment[Int](2).toHtml(IntSerializer))
  println(john.toHTML(PartialUserSerializer)) // println(new HTMLEnrichment[User](john).toHtml(PartialUserSerializer))

  /*
  - type class itself --- HTMLSerializer[T] { .. }
  - type class instances (some of which are implicit) --- UserSerializer, IntSerializer
  - conversion with implicit classes --- HTMLEnrichment
   */

  // context bounds
  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String =   // context bound, tell the compiler to inject the serializer
    val serializer = implicitly[HTMLSerializer[T]]
    // use serializer
    s"<html><body>${content.toHTML(serializer)}</body></html>"  // compiler will inject the serializer for us

  // implicitly

  case class Permissions(mask: String)
  implicit val defaultPermissions: Permissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]  // compiler will inject the defaultPermissions, implicitly[Permissions] == defaultPermissions

  // implicit arguments and parameters



}
