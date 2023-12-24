package com.eugeniusz.scala
package scala_advanced_fp.part4concurrency

object JVMConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)  // race condition
  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    /*
      involves 3 steps:
      - read the value of the account
      - do some computation based on the value
      - write the value back to the account
     */
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {  // thread safe
    bankAccount.synchronized {  // do not allow two threads to evaluate this at the same time
      bankAccount.amount -= price  // critical section
      // no other thread can read or write to this bank account until this thread is done with it
    }
  }

  /*
    Example race condition:
    thread1 (shoes): 50000
      - reads amount 50000
      - decrease amount by 3000 = 50000 - 3000 = 47000
    thread2 (iphone): 50000
      - reads amount 50000
      - decrease amount by 4000 = 50000 - 4000 = 46000
    thread1 (shoes): 47000
      - write amount to 47000
    thread2 (iphone): 46000
      - write amount to 46000
   */
  def demoBankingProblem(): Unit = {
    (1 to 100000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iPhone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"AHA! I've just broken the bank: ${account.amount}")
    }
  }

  /*
  Exercises:
  1 - create "inception threads"
    thread1
      -> thread2
        -> thread3
          -> ...
    each thread prints "hello from thread $i"
    print all the messages in reverse order

  2 - what's the biggest value possible for x?

  3 - "sleep fallacy"
    - what's the value of message?
    - is it guaranteed?
   */

  // 1 - create "inception threads"

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
    }
    println(s"Hello from thread $i")
  })

  // 2 - what's the biggest value possible for x?
  /*
    - what's the biggest value possible for x? 100
    - what's the smallest value possible for x? 1
      min value = 1
      all threads read the same value of x = 0 at the same time
      all threads (in parallel) increment the value of x by 1
      all threads write the value of x = 1
   */
  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
    threads.foreach(_.join())
    println(x)
  }

  // 3 - "sleep fallacy"
  /*
    - what's the value of message? "Scala is awesome"
    - is it guaranteed? NO!
    - why? why not?

    (main thread)
      message = "Scala sucks"
      awesomeThread.start()
      sleep() - relieves execution - may yield the CPU to some other thread
    (awesome thread)
      sleep() - relieves execution
    (OS gives the CPU to some important thread - takes CPU for more than 2 seconds)
    (OS gives the CPU back to the MAIN thread)
      println("Scala sucks")
    (OS gives the CPU to awesome thread)
      message = "Scala is awesome"
   */
  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    // solution 1 - wait for the awesome thread to join
    awesomeThread.join()  // wait for the awesome thread to join
    println(message)
  }

  def main(args: Array[String]): Unit = {
//    runInParallel()
//    demoBankingProblem()
//    minMaxX()
    demoSleepFallacy()
//    inceptionThreads(50).start()
  }

}
