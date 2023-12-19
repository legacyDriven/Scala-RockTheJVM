package com.eugeniusz.scala
package scala_advanced_fp.exercises

import scala.annotation.tailrec

trait MySet [A] extends (A => Boolean){

  /*
    EXERCISE - implement a functional set
   */

  def apply(elem: A): Boolean =
    contains(elem)

  def contains(elem: A): Boolean
  def +(elem: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A] // union
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit

  // part 2

  /*
    EXERCISE #2
    - removing an element
    - intersection with another set
    - difference with another set
   */

  def -(elem: A): MySet[A] // remove an element

  def &(anotherSet: MySet[A]): MySet[A] // intersection

  def --(anotherSet: MySet[A]): MySet[A] // difference

  // EXERCISE #3  - implement a unary_! = NEGATION of a set
  // set[1,2,3] => unary_! would return everything but [1,2,3]
  def unary_! : MySet[A]

}

class EmptySet[A] extends MySet[A]{
  override def contains(elem: A): Boolean = false
  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)  // add element to the set
  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet  // union
  override def map[B](f: A => B): MySet[B] = new EmptySet[B]
  override def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  override def filter(predicate: A => Boolean): MySet[A] = this
  override def foreach(f: A => Unit): Unit = ()
  override def apply(elem: A): Boolean = false

  // part 2
  override def -(elem: A): MySet[A] = this // remove an element

  override def &(anotherSet: MySet[A]): MySet[A] = this // intersection

  override def --(anotherSet: MySet[A]): MySet[A] = this // difference

  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)  // negation of a set

}

// all elements of type A which satisfy the property
// { x in A | property(x)}
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A]{

  override def contains(elem: A): Boolean = property(elem)

  // { x in A | property(x)} + element = { x in A | property(x) || x == element
  override def +(elem: A): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || x == elem)  // add element to the set

  // { x in A | property(x)} ++ set => { x in A | property(x) || set contains x}
  override def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || anotherSet(x))  // union

  // all integers => (_ % 3) => [0 1 2] - all integers which are divisible by 3
  override def map[B](f: A => B): MySet[B] = politelyFail

  override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

  override def filter(predicate: A => Boolean): MySet[A] =
    new PropertyBasedSet[A](x => property(x) && predicate(x))  // intersection

  override def foreach(f: A => Unit): Unit = politelyFail

  override def -(elem: A): MySet[A] = filter(x => x != elem)  // removing

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)  // intersection

  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)  // difference

  override def unary_! : MySet[A] =
    new PropertyBasedSet[A](x => !property(x))  // negation of a set

  private def politelyFail = throw new IllegalArgumentException("Really deep rabbit hole!")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  def contains(elem: A): Boolean =
    elem == head || tail.contains(elem)

  /*
  [1 2 3] + 4 = [1 2 3 4]
  [1 2 3] + 3 = [1 2 3]
 */
  def +(elem: A): MySet[A] =
    if (this contains elem) this
    else new NonEmptySet[A](elem, this)

  /*
  [1 2 3] ++ [4 5] =
  [2 3] ++ [4 5] + 1 =
  [3] ++ [4 5] + 1 + 2 =
  [] ++ [4 5] + 1 + 2 + 3 =
  [4 5] + 1 + 2 + 3 = [4 5 1 2 3]
   */
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] =
    (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] = // f returns a set
    (tail flatMap f) ++ f(head) // f(head) returns a set, so we need to union it with the tail set

  def filter(predicate: A => Boolean): MySet[A] = { // predicate returns a boolean
    val filteredTail = tail filter predicate // filter the tail
    if (predicate(head)) filteredTail + head // if the head satisfies the predicate, add it to the filtered tail
    else filteredTail // otherwise return the filtered tail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  // part 2
  override def -(elem: A): MySet[A] =   // removing
    if (head == elem) tail
    else tail - elem + head


  override def --(anotherSet: MySet[A]): MySet[A] =
//    filter(x => !anotherSet.contains(x))
//    filter(x => !anotherSet(x))
      filter(!anotherSet)
  override def &(anotherSet: MySet[A]): MySet[A] =  // intersecting == filtering
//    filter(x => anotherSet.contains(x))
//    filter(x => anotherSet(x))  // same as above
      filter(!anotherSet)  // with unary operator

  // new operator

  def unary_! : MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))
}

object MySet {
  /*
    val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
    = buildSet(seq(2,3), [] + 1)
    = buildSet(seq(3), [1] + 2)
    = buildSet(seq(), [1, 2] + 3)
    = [1, 2, 3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1,2,3,4)
  s + 5 ++ MySet(-1, -2) + 3 flatMap(x => MySet(x, x * 10)) filter (_ % 2 == 0) foreach println

  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2))  // false
  println(negative(5))  // true

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))  // false

  val negativeEven5 = negativeEven + 5  // all the even numbers > 4 + 5
  println(negativeEven5(5))  // true
}