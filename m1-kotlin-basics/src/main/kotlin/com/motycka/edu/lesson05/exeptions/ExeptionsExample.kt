package com.motycka.edu.lesson05.exeptions

/*
Create a class OutOfFuelException that extends Throwable and sets the message to "Car is out of fuel."

Create a class Car with the following properties and methods:
- var fuelKm: Int
- fun drive(distance: Int) that will check if car has enough fuel to drive the distance and reduce the fuelKm by the distance

Create an instance of the Car class and test the drive method with a distance that is greater than the fuelKm.

Use a try-catch-finally block to catch the OutOfFuelException and print the message.

Add another catch block to catch any other Exception and print the message.
 */
fun main() {

    val car = Car(fuelKm = 100)

    try {
        car.drive(101)
    } catch (e: OutOfFuelException) {
        println(e.message)
    } catch (e: Exception) {
        println(e.message)
    } finally {
        println("Exiting the car.")
    }
}

/*
 */
class OutOfFuelException() : Throwable("Car is out of fuel.")

class Car(var fuelKm: Int) {
    fun drive(km: Int) {
        if (fuelKm - km <= 0) {
            fuelKm = 0
            throw OutOfFuelException()
        } else {
            fuelKm -= km
        }
    }
}
