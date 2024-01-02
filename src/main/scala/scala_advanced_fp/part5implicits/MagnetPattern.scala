package com.eugeniusz.scala
package scala_advanced_fp.part5implicits

import scala.concurrent.Future
import scala.reflect.ClassManifest
import scala.concurrent.ExecutionContext.Implicits.global

object MagnetPattern extends App {

  // method overloading

  class P2PRequest

  class P2PResponse

  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int

    def receive(request: P2PRequest): Int

    def receive(response: P2PResponse): Int

    def receive[T: Serializer](message: T): Int

    def receive[T: Serializer](message: T, statusCode: Int): Int

    def receive(future: Future[P2PRequest]): Int
    //    def receive(future: Future[P2PResponse]): Int  // <- PROBLEM
  }

  // lots of overloads

  /*
      PROBLEMS
      1 - type erasure
      2 - lifting doesn't work for all overloads
        val receiveFV = receive _ // ?!, statusCode? request?!
      3 - code duplication
      4 - type inference and default args
        actor.receive(?!)  // which default argument to pick?
     */

  // TYPE CLASS
  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet() // <- single method, magnet.apply()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling a P2P request
      println("Handling P2P request")
      42
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling a P2P response
      println("Handling P2P response")
      24
    }
  }

  receive(new P2PRequest) // <- new FromP2PRequest(new P2PRequest).apply()
  receive(new P2PResponse) // <- new FromP2PResponse(new P2PResponse).apply(), implicit conversion from P2PResponse to MessageMagnet[Int]

  // 1 - no more type erasure problems!
  implicit class FromResponseFuture(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = 2
  }

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = 3
  }

  println(receive(Future(new P2PRequest))) // <- new FromRequestFuture(new Future(new P2PRequest)).apply()
  println(receive(Future(new P2PResponse))) // <- new FromResponseFuture(new Future(new P2PResponse)).apply()

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int): Int = x + 1

    def add1(s: String): Int = s.toInt + 1
    // add1 overloads
  }

  // "magnetize"
  trait AddMagnet { // no type parameter, so no type erasure problems and no lifting problems, but we need to define apply method
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(x: String) extends AddMagnet {
    override def apply(): Int = x.toInt + 1
  }

  val addFV = add1 _ // <- AddMagnet
  println(addFV(1)) // <- AddInt(1) => add1(1)
  println(addFV("3")) // <- AddString("3") => add1("3")

  //  val receiveFV = receive _  // <- MessageMagnet
  //  println(receiveFV(new P2PResponse))  // compile error

  /*
    Drawbacks of Magnet Pattern:
    1 - verbose
    2 - harder to read
    3 - you can't name or place default arguments
    4 - call by name doesn't work correctly
    (exercise: prove it!) (hint: side effects)
   */

  class Handler {
    def handle(s: => String) = { // <- call by name
      println(s)
      println(s)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello, Scala!")
    "hahaha"
  }

//  handle(sideEffectMethod())  // <- sideEffectMethod() => StringHandle(sideEffectMethod()) => apply()
  handle {
    println("Hello, Scala!")
    "magnet"  // only this value is converted to StringHandle
  }
  // careful!
}