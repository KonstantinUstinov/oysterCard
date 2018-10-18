package oyster.zone

case class Station(name: String, zones: List[Int])

class StationStorage {
  private val stations = List(
    Station("Holborn", List(1)),
    Station("Earl’s Court", List(1, 2)),
    Station("Hammersmith", List(2)),
    Station("Wimbledon", List(3)),
    Station("Edgware", List(5))
  )

  def getZonesBStation(name: String): List[Int] = {
    stations.find(_.name == name).get.zones
  }

  def crossedZone(from: List[Int], to: List[Int]): Boolean =
    (from.lengthCompare(1) == 0 && from.contains(1)) || (to.lengthCompare(1) == 0 && to.contains(1))


  def findMinZoneVisit(from: List[Int], to: List[Int]): Int = {
    var minZonesVisited: Int = Int.MaxValue
    for (fromZone <- from) {
      for (toZone <- to) {
        val zonesVisited: Int = Math.abs(fromZone - toZone) + 1
        if (zonesVisited < minZonesVisited) minZonesVisited = zonesVisited
      }
    }
    minZonesVisited
  }

  def getCostByZoneVisit(minZonesVisit: Int, crossedZone: Boolean) : Double = {
    if(minZonesVisit == 3)
      StationStorage.MAX_COST
    else if (minZonesVisit == 2 && !crossedZone)
      2.25
    else if (minZonesVisit == 2 && crossedZone)
      3.0
    else if(minZonesVisit == 1 && crossedZone)
      2.5
    else if(minZonesVisit == 1 && !crossedZone)
      2.0
    else
      StationStorage.MAX_COST
  }
}

object StationStorage {

  val MAX_COST = 3.2
  val BUS_COST = 1.8

  def apply(): StationStorage = new StationStorage()
}