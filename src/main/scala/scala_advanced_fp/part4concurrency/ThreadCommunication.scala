package com.eugeniusz.scala
package scala_advanced_fp.part4concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {

  /*
    the producer-consumer problem

    producer -> [ x ] -> consumer
    producer and consumer run in parallel and don't know about each other state
   */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def get: Int = {
      val result = value
      value = 0
      result
    }

    def set(newValue: Int): Unit = value = newValue
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => { // consumer busy waiting
      println("[consumer] waiting...")
      while (container.isEmpty) {
        println("[consumer] actively waiting...")
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced, after long work, the value " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  //  naiveProdCons()

  /*
    wait and notify
    waiting will suspend the thread indefinitely until another thread notifies
   */
  /*
    General principles:
    make no assumptions about who gets the lock first
    keep locking to a minimum
    maintain thread safety at ALL times
   */

  def smartProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => { // consumer busy waiting
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }

      // container must have some value
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  //  smartProdCons()

  /*
    buffer
    producer -> [ ? ? ? ] -> consumer

   */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue() // dequeue removes the first element in the queue
          println("[consumer] consumed " + x)

          // hey producer, there's empty space available, are you lazy?!
          buffer.notify()
        }

        Thread.sleep(random.nextInt(250)) // try with 100, 250, 500
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // hey consumer, new food for you!
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(250)) // try with 100, 250, 500
      }
    })

    consumer.start()
    producer.start()
  }

//  prodConsLargeBuffer()

  /*
    prod-cons, level 3
    limited capacity buffer, multiple producers and consumers

    producer1 -> [ ? ? ? ] -> consumer1
    producer2 -----^    ^---- consumer2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          /*
            producer produces value, two Cons are waiting
            notifies ONE consumer, notifies on buffer
            notifies the other consumer
           */
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue() // dequeue removes the first element in the queue
          println(s"[consumer $id] consumed " + x)

          // hey producer, there's empty space available, are you lazy?!
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500)) // try with 100, 250, 500
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          /*
            producer produces value, two Cons are waiting
            notifies ONE consumer, notifies on buffer
            notifies the other consumer
           */
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer $id] producing " + i)
          buffer.enqueue(i)

          // hey consumer, new food for you!
          buffer.notify()  // notifyAll would wake up all the threads, but only one can consume, will prevent potential deadlock

          i += 1
        }

        Thread.sleep(random.nextInt(250)) // try with 100, 250, 500
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    (1 to nConsumers).foreach(id => new Consumer(id, buffer).start())
    (1 to nProducers).foreach(id => new Producer(id, buffer, capacity).start())
  }

//  multiProdCons(3, 6)  // play with different combinations of consumers and producers and capacity

  /*
    Exercises.
    1) think of an example where notifyAll acts in a different way than notify?
    2) create a deadlock - two threads block each other, waiting for the other one to release a lock
    3) create a livelock - threads are not blocked, but they are busy with a useless task
   */

  // 1 - notifyAll

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock'n roll!")
      bell.synchronized {
        bell.notifyAll() // notifyAll wakes up all the waiting threads
      }
    }).start()
  }

//  testNotifyAll()

  // 2 - deadlock

  case class Friend(name: String) {
    def bow(other: Friend): Unit = {
      this.synchronized {
        println(s"$this: I'm bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend): Unit = {
      this.synchronized {
        println(s"$this: I'm rising to my friend $other")
      }
    }

    // exercise 3 - livelock
    var side = "right"

    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {  // this is a livelock, where both threads are active, but they are not progressing
      while (this.side == other.side) {
        println(s"$this: Oh, but please, $other, feel free to pass...")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  def testDeadlock(): Unit = {
    val sam = Friend("Sam")
    val pierre = Friend("Pierre")

    // no thread can progress because both are waiting for each other to release the lock
//    new Thread(() => sam.bow(pierre)).start() // sam's lock, then pierre's lock
//    new Thread(() => pierre.bow(sam)).start() // pierre's lock, then sam's lock
  }

//  testDeadlock()

  // 3 - livelock

  def testLivelock(): Unit = {
    val sam = Friend("Sam")
    val pierre = Friend("Pierre")

    new Thread(() => sam.pass(pierre)).start()
    new Thread(() => pierre.pass(sam)).start()
  }

  testLivelock()

}
