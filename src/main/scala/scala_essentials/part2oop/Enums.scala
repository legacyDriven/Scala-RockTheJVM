package com.eugeniusz.scala
package scala_essentials.part2oop

object Enums {

  enum Permissions { // enum is a new feature in Scala 3
    case READ, WRITE, EXECUTE, NONE // enum values are also objects

    // add fields and methods
    def openDocument(): Unit =
      if (this == READ) println("opening document...")
      else println("you don't have permission to open this document")
  }

    val somePermissions: Permissions = Permissions.READ // READ

    // constructor arguments
  enum PermissionsWithBits(bits: Int) { // enum is a new feature in Scala 3
    case READ extends PermissionsWithBits(4) // 100
    case WRITE extends PermissionsWithBits(2) // 010
    case EXECUTE extends PermissionsWithBits(1) // 001
    case NONE extends PermissionsWithBits(0) // 000
  }

  object PermissionsWithBits {
    // factory method
    def fromBits(bits: Int): PermissionsWithBits = {
      // TODO
      PermissionsWithBits.NONE
    }
  }

  // standard API

  val somePermissionsOrdinal = somePermissions.ordinal // 0
  val allPermissions = PermissionsWithBits.values // Array(READ, WRITE, EXECUTE, NONE)
  val readPermission: Permissions = Permissions.valueOf("READ") // Permissions.READ

    def main(args: Array[String]): Unit = {
      somePermissions.openDocument()
      println(somePermissionsOrdinal)
    }
  }

