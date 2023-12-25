package com.eugeniusz.scala
package scala_advanced_fp.part4concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration.*

// important for Futures
import concurrent.ExecutionContext.Implicits.global  // global thread pool

object FuturesAndPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {  // need implicit ExecutionContext
    calculateMeaningOfLife  // calculates the meaning of life on ANOTHER thread
  }  // (global) which is passed by the compiler

  println(aFuture.value)  // Option[Try[Int]] = None, because the future is not completed yet, Future [Int] is a Try[Int]

  println("Waiting on the future")

  aFuture.onComplete {  // partial function, onComplete returns Unit, so its used for side effects
    case Success(meaningOfLife) => println(s"The meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  }  // SOME thread will call this onComplete method

//  aFuture.onComplete(t => t match {
//    case Success(meaningOfLife) => println(s"The meaning of life is $meaningOfLife")
//    case Failure(e) => println(s"I have failed with $e")
//  })  // SOME thread will call this onComplete method

  Thread.sleep(3000)

  // mini social network
  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile): Unit = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    // "database"
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.3-dummy" -> "Dummy"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    // API
    def fetchProfile(id: String): Future[Profile] = Future {  // Future[Profile] is a Try[Profile]
      // fetching from the DB
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))  // will throw NoSuchElementException if id is not in the map
    }

    def fetchBestFriend(profile: Profile): Future[Profile] = Future {  // Future[Profile] is a Try[Profile]
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)  // will throw NoSuchElementException if id is not in the map
      Profile(bfId, names(bfId))
    }
  }

  // client: mark to poke bill
  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")

//  mark.onComplete {
//    case Success(markProfile) => {
//      val bill = SocialNetwork.fetchBestFriend(markProfile)
//      bill.onComplete {
//        case Success(billProfile) => markProfile.poke(billProfile)
//        case Failure(e) => e.printStackTrace()
//      }
//    }
//    case Failure(ex) => ex.printStackTrace()
//  }

//  Thread.sleep(1000)

  // below is cleaner than above
  // functional composition of futures
  // map, flatMap, filter

  val nameOnTheWall = mark.map(profile => profile.name)  // Future[String]

  val marksBestFriend = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))  // Future[Profile]

  val zucksBestFriendRestricted = marksBestFriend.filter(profile => profile.name.startsWith("Z"))  // Future[Profile]

  // for-comprehensions
  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")  // Future[Profile]
    bill <- SocialNetwork.fetchBestFriend(mark)  // Future[Profile]
  } mark.poke(bill)

  Thread.sleep(1000)

  // fallbacks
  val aProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recover {
    case e: Throwable => Profile("fb.id.0-dummy", "Forever alone")  // fallback value, if the future fails, partial function
  }

  // we use if recoverWith to return result, that with higher probability will be successful
  val aFetchedProfileNoMatterWhat = SocialNetwork.fetchProfile("unknown id").recoverWith {
    case e: Throwable => SocialNetwork.fetchProfile("fb.id.0-dummy")  // fallback future, if the future fails, partial function
  }

  // if the future fails, we can return another future
  val fallbackResult = SocialNetwork.fetchProfile("unknown id").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  // online banking app
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      // simulate fetching from the DB
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double): Future[Transaction] = Future {
      // simulate some processes
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost: Double): String = {
      // fetch the user from the DB
      // create a transaction
      // WAIT for the transaction to finish
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      // block until the transaction is completed
      Await.result(transactionStatusFuture, 2.seconds)  // implicit conversions -> pimp my library
    }  // await will throw TimeoutException if the future is not completed in 2 seconds
  }

  println(BankingApp.purchase("Daniel", "iPhone 12", "rock the jvm store", 3000))

  // promises
  // it separates the concern of reading and handling the futures and of writing to the promises, eliminating concurrency issues almost entirely
  val promise = Promise[Int]()  // "controller" over a future
  val future = promise.future  // future is under the control of the promise

  // thread 1 - "consumer"
  future.onComplete {
    case Success(r) => println("[consumer] I've received " + r)
  }

  // thread 2 - "producer"
  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    // "fulfilling" the promise
    promise.success(42)  // this will call the onComplete method of the future

    // promise.failure(new RuntimeException)  // this will call the onComplete method of the future
    println("[producer] done")
  })

  producer.start()
  Thread.sleep(1000)

  /*
    1) fulfill a future IMMEDIATELY with a value
    2) inSequence(fa, fb)  => new future with the value of fa and fb in sequence
    3) first(fa, fb) => new future with the first value of the two futures
    4) last(fa, fb) => new future with the last value
    5) retryUntil[T](action: () => Future[T], condition: T => Boolean): Future[T]
   */

  // 1 - fulfill immediately
  def fulfillImmediately[T](value: T): Future[T] = Future(value)  // Future.successful(value)

  // 2 - inSequence

  def inSequence[A, B](first: Future[A], second: Future[B]): Future[B] =
    first.flatMap(_ => second)  // first will be completed first, then second

  // 3 - first out of two futures

  def first[A](fa: Future[A], fb: Future[A]): Future[A] = {
    val promise = Promise[A]  // controller over a future of type A

//    def tryComplete(promise: Promise[A], result: Try[A]) = result match {
//      case Success(r) => try {
//        promise.success(r)
//      } catch {
//        case _ =>
//      }
//      case Failure(t) => try {
//        promise.failure(t)
//      } catch {
//        case _ =>
//      }
//    }

//    fa.onComplete(tryComplete(promise, _))
//    fb.onComplete(tryComplete(promise, _))

    // this is better than above
    fa.onComplete(promise.tryComplete)  // if the promise is already completed, it will not complete it again
    fb.onComplete(promise.tryComplete)  // if the promise is already completed, it will not complete it again

    promise.future  // this will be completed by either fa or fb
  }

  // 4 - last out of two futures

  def last[A](fa: Future[A], fb: Future[A]): Future[A] = {
    // 1 promise which both futures will try to complete
    val bothPromise = Promise[A]  // controller over a future of type A
    // 2 promise which the last future will complete
    val lastPromise = Promise[A]  // controller over a future of type A

    // this function will try to complete bothPromise and lastPromise
    val checkAndComplete = (result: Try[A]) =>  // partial function
      if (!bothPromise.tryComplete(result))  // if the bothPromise is already completed, it will not complete it again
        lastPromise.complete(result)  // if the bothPromise is already completed, it will not complete it again

    fa.onComplete(checkAndComplete)  // if the bothPromise is already completed, it will not complete it again
    fb.onComplete(checkAndComplete)

    lastPromise.future  // this will be completed by either fa or fb
  }

  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }

  first(fast, slow).foreach(f => println("FIRST " + f))  // 42

  last(fast, slow).foreach(l => println("LAST " + l))  // 42

  Thread.sleep(1000)

  // 5 - retry until

  // it does not retry in current implementation, debug
  def retryUntil[A](action: () => Future[A], condition: A => Boolean): Future[A] =
    action()  // trigger the action
      .filter(condition)  // if the condition is true, return the future
      .recoverWith {  // if the condition is false, retry
        case _ => retryUntil(action, condition)  // this will return a future
      }

  val random = new Random()

  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(100)
    println("generated " + nextValue)
    nextValue
  }

  retryUntil(action, (x: Int) => x < 10).foreach(result => println("settled at " + result))
  Thread.sleep(10000)

  // why does it not retry on failure?

}
