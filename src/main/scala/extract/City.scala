package extract

import com.typesafe.config.Config
import scala.jdk.CollectionConverters.CollectionHasAsScala

case class City(name: String, latitude: Double, longitude: Double)

object City {
  def getAllCities(config: Config): List[City] = {
    val citiesConfig = config.getConfigList("cities").asScala
    citiesConfig.map { cityConfig =>
      val name = cityConfig.getString("name")
      val latitude = cityConfig.getDouble("latitude")
      val longitude = cityConfig.getDouble("longitude")
      City(name, latitude, longitude)
    }.toList
  }
}