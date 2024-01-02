package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import java.util.Date

object JSONSerialization extends App {

  /*
  Users, posts, feeds
  Serialize to JSON
   */

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: Date)
  case class Feed(user: User, posts: List[Post])

  /*
  1 - intermediate data types: Int, String, List, Date
  2 - type classes for conversion to intermediate data types
  3 - serialize to JSON
   */

  sealed trait JSONValue { // intermediate data type, sealed trait is a good practice, works like enum
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    def stringify: String = "\"" + value + "\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    /*
    {
      name: "John",
      age: 22,
      friends: [...],
      latestPost: {
        content: "Scala rocks",
        date: ...
      }
    }
     */
    def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(get: Map[String, JSONValue]) extends JSONValue {
    /*
    {
      name: "John",
      age: 22,
      friends: [...],
      latestPost: {
        content: "Scala rocks",
        date: ...
      }
    }
     */
    def stringify: String = get.map { case (key, value) => "\"" + key + "\":" + value.stringify
    }
      .mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" -> JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala rocks"),
      JSONNumber(453)
    ))
  ))

  println(data.stringify)  // {"user":"Daniel","posts":["Scala rocks",453]}

  // type class
  /*
  1 - type class
  2 - type class instances (implicit)
  3 - pimp library to use type class instances
   */

  // 2.1
  trait JSONConverter[T] {  // type class
    def convert(value: T): JSONValue
  }

  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue = converter.convert(value)
  }

// 2.2

  //  existing data types
  implicit object StringConverter extends JSONConverter[String] {
    def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types

  implicit object UserConverter extends JSONConverter[User] {
    def convert(user: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(user.name),
      "age" -> JSONNumber(user.age),
      "email" -> JSONString(user.email)
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    def convert(post: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(post.content),
      "created:" -> JSONString(post.createdAt.toString)
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    def convert(feed: Feed): JSONValue = JSONObject(Map(
      "user" -> feed.user.toJSON,  //UserConverter.convert(feed.user),  // TODO
      "posts" -> JSONArray(feed.posts.map(_.toJSON))  //(PostConverter.convert)) // TODO
    ))
  }

  // call stringify on result

  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@rockthejvm.com")
  val feed = Feed(john, List(
    Post("Hello", now),
    Post("Look at this cute puppy", now)
  ))

  println(feed.toJSON.stringify)



}
