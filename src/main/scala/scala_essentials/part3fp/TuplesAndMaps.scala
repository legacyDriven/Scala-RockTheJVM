package com.eugeniusz.scala
package scala_essentials.part3fp

object TuplesAndMaps extends App {

  // tuples = finite ordered "lists"
  val aTuple = new Tuple2(2, "Hello, Scala") // Tuple2[Int, String] = (Int, String)

  println(aTuple._1)
  println(aTuple._2)
  println(aTuple.copy(_2 = "goodbye Java"))
  println(aTuple.swap) // ("Hello, Scala", 2)

  //  Maps - keys -> values
  val aMap: Map[String, Int] = Map()

  val phoneBook = Map(("Jim", 555), "Daniel" -> 789).withDefaultValue(-1)
  // a -> b is sugar for (a,b)
  println(phoneBook)

  // map ops
  println(phoneBook.contains("Jim"))
  println(phoneBook.contains("Jim"))
  println(phoneBook("Jim"))
  println(phoneBook("Mary")) // will throw NoSuchElementException when no defaultValue

  // add a pairing
  val newPairing = "Mary" -> 678
  val newPhoneBook = phoneBook + newPairing
  println(newPhoneBook)

  // functionals on maps
  // map, flatMap, filter

  println(phoneBook.map(pair => pair._1.toLowerCase -> pair._2))

  //  filterKeys
  println(phoneBook.filterKeys(x => x.startsWith("J"))) // deprecated
  println(phoneBook.view.filterKeys(x => x.startsWith("J")).toMap) // current syntax

  //  mapValues
  println(phoneBook.mapValues(number => number * 10)) // deprecated MapView(<not computed>)
  println(phoneBook.view.mapValues(number => number * 10).toMap)
  println(phoneBook.view.mapValues(number => "0245-" + number).toMap)

  // conversions to other collections
  println(phoneBook.toList)
  println(List(("Daniel", 555)).toMap)

  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))

  /*
    1. What would happen if I had two original entries "Jim" -> 555 and "JIM" -> 900
        !!! careful with mapping keys.
    2. Overly simplified social network based on maps
       Person = String
        - add a person to the network
        - remove
        - friend (mutual)
        - unfriend

        - number of friends of a person
        - person with most friends
        - how many people have NO friends
        - if there is a social connection between two people (direct or not)
  */

  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] =  // add a person to the network
    network + (person -> Set())  // add a person to the network

  def friend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {  // add b to a's friends and a to b's friends
    val friendsA: Set[String] = network(a)  // network(a) is a Set
    val friendsB: Set[String] = network(b)  // network(b) is a Set

    network + (a -> (friendsA + b)) + (b -> (friendsB + a))  // add b to a's friends and a to b's friends
  }

  def unfriend(network: Map[String, Set[String]], a: String, b: String): Map[String, Set[String]] = {  // remove b from a's friends and a from b's friends
    val friendsA: Set[String] = network(a)  // network(a) is a Set
    val friendsB: Set[String] = network(b)  // network(b) is a Set

    network + (a -> (friendsA - b)) + (b -> (friendsB - a))  // remove b from a's friends and a from b's friends
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] = {  // remove a person from the network
    def removeAux(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] =  // remove a person from the network
      if (friends.isEmpty) networkAcc
      else removeAux(friends.tail, unfriend(networkAcc, person, friends.head))

    val unfriended = removeAux(network(person), network)  // remove a person from the network

    unfriended - person  // remove a person from the network
  }

  val empty: Map[String, Set[String]] = Map()
  val network = add(add(empty, "Bob"), "Mary")  // add a person to the network
  println(network)
  println(friend(network, "Bob", "Mary"))  // add b to a's friends and a to b's friends
  println(unfriend(friend(network, "Bob", "Mary"), "Bob", "Mary"))  // remove b from a's friends and a from b's friends
  println(remove(friend(network, "Bob", "Mary"), "Bob"))  // remove a person from the network

  // Jim, Bob, Mary
  val people = add(add(add(empty, "Bob"), "Mary"), "Jim")  // add a person to the network
  val jimBob = friend(people, "Bob", "Jim")  // add b to a's friends and a to b's friends
  val testNet = friend(jimBob, "Bob", "Mary")  // add b to a's friends and a to b's friends

  println(testNet)

  def nFriends(network: Map[String, Set[String]], person: String): Int =  // number of friends of a person
    if (!network.contains(person)) 0  // number of friends of a person
    else network(person).size  // number of friends of a person

  println(nFriends(testNet, "Bob"))  // number of friends of a person, prints 2

  def mostFriends(network: Map[String, Set[String]]): String =  // person with most friends
    network.maxBy(pair => pair._2.size)._1  // person with most friends

  println(mostFriends(testNet))  // person with most friends, prints Bob

  def nPeopleWithNoFriends(network: Map[String, Set[String]]): Int =  // how many people have NO friends
    network.count(pair => pair._2.isEmpty)  // how many people have NO friends

  println(nPeopleWithNoFriends(testNet))  // how many people have NO friends, prints 0

  def socialConnection(network: Map[String, Set[String]], a: String, b: String): Boolean = {  // if there is a social connection between two people (direct or not)
    // breadth-first search is used to find the shortest path between two nodes in a graph
    def bfs(target: String, consideredPeople: Set[String], discoveredPeople: Set[String]): Boolean =  // if there is a social connection between two people (direct or not)
      if (discoveredPeople.isEmpty) false  // if there is a social connection between two people (direct or not)
      else {  // if there is a social connection between two people (direct or not)
        val person = discoveredPeople.head  // if there is a social connection between two people (direct or not)
        if (person == target) true  // if there is a social connection between two people (direct or not)
        else if (consideredPeople.contains(person)) bfs(target, consideredPeople, discoveredPeople.tail)  // if there is a social connection between two people (direct or not)
        else bfs(target, consideredPeople + person, discoveredPeople.tail ++ network(person))  // if there is a social connection between two people (direct or not)
      }  // if there is a social connection between two people (direct or not)

    bfs(b, Set(), network(a) + a)  // if there is a social connection between two people (direct or not)
  }

  println(socialConnection(testNet, "Mary", "Jim"))  // if there is a social connection between two people (direct or not), prints true
  println(socialConnection(network, "Mary", "Bob"))  // if there is a social connection between two people (direct or not), prints false

}
