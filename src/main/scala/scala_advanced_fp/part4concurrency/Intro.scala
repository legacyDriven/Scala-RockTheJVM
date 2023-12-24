package com.eugeniusz.scala
package scala_advanced_fp.part4concurrency

object Intro extends App {

  /*
    interface Runnable {
      public void run()
    }
   */
  // JVM threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })
  val runnable = new Runnable {
    override def run(): Unit = println("Running in other parallel")
  }
  val aThread2 = new Thread(runnable)  // same as: new Thread(() => println("Running in other parallel"))

  aThread.start() // gives the signal to the JVM to start a JVM thread
  // create a JVM thread => OS thread
  aThread.join() // blocks until aThread finishes running
  runnable.run() // doesn't do anything in parallel!

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))  // same as: new Thread(() => println("hello")), supplying a lambda runnable
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  threadHello.start()
  threadGoodbye.start()
  // different runs produce different results!
  threadGoodbye.join()
  threadHello.join()

  // executors
  import java.util.concurrent.Executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
//   pool.execute(() => println("should not appear")) // throws an exception in the calling thread

//  pool.shutdownNow()  // interrupts the sleeping threads
  println(pool.isShutdown)  // true

}
