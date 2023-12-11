package com.eugeniusz.scala
package scala_essentials.part3fp

import scala.util.{Failure, Random, Success, Try}

object HandlingFailure extends App {

  // create success and failure
  val aSuccess = Success(3)
  val aFailure = Failure(new RuntimeException("Super Failure"))

  println(aSuccess)
  println(aFailure)

  def unsafeMethod(): String = throw new RuntimeException("No String for you!")

  // Try objects via the apply method
  val potentialFailure = Try(unsafeMethod())
  println(potentialFailure)

  // syntax sugar
  val anotherPotentialFailure = Try {
    // code that might throw
  }

  // utilities
  println(potentialFailure.isSuccess)  // false

  // orElse
  def backupMethod(): String = "A valid result"  // valid result
  val fallbackTry = Try(unsafeMethod()).orElse(Try(backupMethod()))  // Try(backupMethod())
  println(fallbackTry)  // Success(A valid result)

  // If you design the API
  def betterUnsafeMethod(): Try[String] = Failure(new RuntimeException)  // Failure(java.lang.RuntimeException)
  def betterBackupMethod(): Try[String] = Success("A valid result")  // Success(A valid result)
  val betterFallback = betterUnsafeMethod() orElse betterBackupMethod()  // Success(A valid result)
  println(betterFallback)  // Success(A valid result)

  // map, flatMap, filter
  println(aSuccess.map(_ * 2))  // Success(6)
  println(aSuccess.flatMap(x => Success(x * 10)))  // Success(30)
  println(aSuccess.filter(_ > 10))  // Failure(java.util.NoSuchElementException: Predicate does not hold for 3)
  // for-comprehensions

  /*
    Exercise
   */
  val hostname = "localhost"
  val port = "8080"
  def renderHTML(page: String) = println(page)

  class Connection {
    def get(url: String): String = {  // try to establish a connection
      val random = new Random(System.nanoTime())  // random generator
      if (random.nextBoolean()) "<html>...</html>"  // simulate a connection
      else throw new RuntimeException("Connection interrupted")  // throw an exception
    }
    def getSafe(url: String): Try[String] = Try(get(url))  // Try(get(url))
  }

  object HttpService {
    val random = new Random(System.nanoTime())  // random generator
    def getConnection(host: String, port: String): Connection = {
      if (random.nextBoolean()) new Connection  // simulate a connection
      else throw new RuntimeException("Someone else took the port")  // throw an exception
    }
    def getSafeConnection(host: String, port: String): Try[Connection] = Try(getConnection(host, port))  // Try(getConnection(host, port))
  }

  // if you get the html page from the connection, print it to the console i.e. call renderHTML
  val possibleConnection = HttpService.getSafeConnection(hostname, port)  // Try(Connection@6e8dacdf)
  val possibleHTML = possibleConnection.flatMap(connection => connection.getSafe("/home"))  // Try(<html>...</html>)
  possibleHTML.foreach(renderHTML)  // <html>...</html>

  // shorthand version
  HttpService.getSafeConnection(hostname, port)
    .flatMap(connection => connection.getSafe("/home"))
    .foreach(renderHTML)

  // for-comprehension version
  for {
    connection <- HttpService.getSafeConnection(hostname, port)  // Try(Connection@6e8dacdf)
    html <- connection.getSafe("/home")  // Try(<html>...</html>)
  } renderHTML(html)  // <html>...</html>

  // Imperative version
  /*
    try {
      connection = HttpService.getConnection(host, port)
      try {
        page = connection.get("/home")
        renderHTML(page)
      } catch (some other exception) {
      }
    } catch (exception) {
    }
   */
}
