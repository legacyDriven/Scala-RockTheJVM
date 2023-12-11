package com.eugeniusz.scala
package scala_essentials.part3fp

import scala.util.Random

object Options extends App {

  val myFirstOption: Option[Int] = Some(4)
  val noOption: Option[Int] = None

  println(myFirstOption)

  // WORK with unsafe APIs
  def unsafeMethod(): String = null

  val result = Some(unsafeMethod()) // WRONG, never do this

  val result2 = Option(unsafeMethod()) // Some or None
  println(result2)

  // chained methods
  def backupMethod(): String = "A valid result"
  val chainedResult = Option(unsafeMethod()).orElse(Option(backupMethod()))  // Some or None
  println(chainedResult)

  // DESIGN unsafe APIs
  def betterUnsafeMethod(): Option[String] = None
  def betterBackupMethod(): Option[String] = Some("A valid result")  // Some or None

  val betterChainedResult = betterUnsafeMethod() orElse betterBackupMethod()
  println(betterChainedResult)

  // functions on Options
  println(myFirstOption.isEmpty)  // false
  println(myFirstOption.get)  // UNSAFE - DO NOT USE THIS

  // map, flatMap, filter
  println(myFirstOption.map(_ * 2))  // Some(8)
  println(myFirstOption.filter(_ > 10))  // None
  println(myFirstOption.flatMap(x => Option(x * 10)))  // Some(40)

  // for-comprehensions
  /*
    Exercise.
   */
  val config: Map[String, String] = Map(
    // fetched from elsewhere
    "host" -> "176.45.36.1",
    "port" -> "80"
  )

  class Connection {
    def connect = "Connected"  // connect to some server
  }
  object Connection {
    val random = new Random(System.nanoTime())  // random number generator
    def apply(host: String, port: String): Option[Connection] =
      if (random.nextBoolean()) Some(new Connection)  // Some or None
      else None  // None
  }

  // try to establish a connection, if so - print the connect method
  val host = config.get("host")  // Option[String]
  val port = config.get("port")  // Option[String]
  /*
     if (h != null)
       if (p != null)
         return Connection.apply(h, p)

     return null
    */
  val connection = host.flatMap(h => port.flatMap(p => Connection.apply(h, p)))  // Option[Connection]
  /*
    if (c != null)
      return c.connect

    return null
   */
  val connectionStatus = connection.map(c => c.connect)  // Option[String]
  /*
    if (connectionStatus == null) println(None) else print(Some(connectionStatus.get))
   */
  println(connectionStatus)  // Some(Connected)
  /*
    if (status != null)
      println(status)
   */
  connectionStatus.foreach(println)  // print "Connected"

  // chained calls
  config.get("host")  // Option[String]
    .flatMap(host => config.get("port")  // Option[String]
      .flatMap(port => Connection(host, port))  // Option[Connection]
      .map(connection => connection.connect))  // Option[String]
    .foreach(println)  // print "Connected"

  // for-comprehensions
  val connectionStatusFor = for {
    host <- config.get("host")  // Option[String]
    port <- config.get("port")  // Option[String]
    connection <- Connection(host, port)  // Option[Connection]
  } yield connection.connect  // Option[String]

  connectionStatusFor.foreach(println)  // print "Connected"



}
