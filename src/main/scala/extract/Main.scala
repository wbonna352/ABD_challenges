package extract

import sttp.model.Uri
import com.typesafe.config.ConfigFactory
import scala.collection.convert.ImplicitConversions._


object Main {

  val config = ConfigFactory.load()

  val baseURI: Uri = Uri.parse(config.getString("baseURI"))
    .getOrElse(
      throw new IllegalArgumentException(s"Invalid URL in configuration")
    )
  val currentParams: List[String] = config
    .getStringList("current_parameters")
    .toList



  val cities: List[City] = City.getAllCities(config)

  def main(args: Array[String]): Unit = {

    val city: City = args.headOption match {
      case None =>
        println("Wybierz miasto z poniższej listy:")
        cities.zipWithIndex.foreach { case (city, index) =>
          println(s"${index + 1}. ${city.name}")
        }
        println("Podaj numer miasta:")
        val selectedNumber: Int = scala.io.StdIn.readInt()
        cities.get(selectedNumber - 1)
      case Some(name) => cities.find(_.name == name) match {
        case Some(city) => city
        case None => throw new IllegalArgumentException(s"Invalid city name: $name")
      }
    }

    val fullURI: Uri = baseURI
      .addPath("forecast")
      .addParam("longitude", city.longitude.toString)
      .addParam("latitude", city.latitude.toString)
      .addParam("current", currentParams.mkString(","))

    val response = requests.get(fullURI.toString, readTimeout = 60*1000)

    println(
      response.text()
    )

  }
}
