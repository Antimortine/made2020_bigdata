import scala.collection.mutable.ArrayBuffer
import scala.util.Random

val xByte: Byte = 5
val xLong = 1000L
val xLongStr = xLong.toString

val name = "Mark"
var isCold = true

def greet(name: String, isCold: Boolean): Unit = {
  if (isCold) {
    println(s"Hi $name! It's cold")
  } else {
    println(s"Hi $name! It's warm")
  }
}

greet(name, isCold)

def celsiusToFahrenheit(celsius: Double): Double = {
  celsius * (9.0 / 5.0) + 32.0
}

def fahrenheitToCelsius(fahrenheit: Double): Double = {
  (fahrenheit - 32.0) * (5.0 / 9.0)
}

celsiusToFahrenheit(100)

fahrenheitToCelsius(celsiusToFahrenheit(100))

fahrenheitToCelsius(451)

val celsiusArray: Array[Double] = Array(5.0, 6.0, 232.78)

for (temp <- celsiusArray) {
  println(celsiusToFahrenheit(temp))
}

val fhtArray: Array[Double] = for (temp <- celsiusArray)
  yield celsiusToFahrenheit(temp)

val fhtArray2: Array[Double] = celsiusArray.map(celsiusToFahrenheit)

val fhtArray3: Array[Double] = celsiusArray.map(t => celsiusToFahrenheit(t))

// Reference Equal
fhtArray2 == fhtArray3

// Elementwise equal
fhtArray2.sameElements(fhtArray3)

// Call method as an operator
fhtArray2 sameElements fhtArray3

// Удобны для описания данных
// Не требует оператора new
case class Person(firstName: String, secondName: String, age: Int = 54)

val richard: Person = Person("Richard", secondName = "Kruspe")

val paul: Person = Person("Paul", "Landers")

val till: Person = Person("Till", "Lindemann")

var band: ArrayBuffer[Person] = ArrayBuffer(richard)

//band.addOne(paul)
band.appendAll(Array[Person](paul, till))

val daysOfWeek: Array[String] = Array("Monday", "Tuesday",
  "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

val temps: Array[Double] = Array.fill(7)(9.0)

val map: Map[String, Double] = daysOfWeek.zip(temps).toMap

val check: Double = map("Monday")

// java.util.NoSuchElementException
//map("UNK")

map.getOrElse("UNK", "Idk")

val randomTemps: Array[Double] = temps.map(t => (t + Random.nextDouble * 8).round)

val randomMap: Map[String, Double] = daysOfWeek.zip(randomTemps).toMap

def presentResult(person: Person, dayOfWeek: String): String = {
  val celsius: Double = randomMap.getOrElse(dayOfWeek, 23.4)
  val fhtTemp: String = celsiusToFahrenheit(celsius).toString
  if (person.firstName == "Till" && person.secondName == "Lindemann") {
    celsius match {
      case 23.4 => "Left"
      case 1 | 2 | 4 => "Sun"
      case _ => fhtTemp
    }
  } else {
    fhtTemp
  }
}

presentResult(till, "First")

presentResult(richard, "First")

presentResult(richard, "Sunday")

