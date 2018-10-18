package oyster.zone

import oyster.journey.{Direction, Journey}

class CalculationService(station: StationStorage) {

  def updateBusCost(journey: Journey) : Journey = journey.copy(cost = StationStorage.BUS_COST)

  def updateTubeCost(journey: Journey, previousJourney: Option[Journey]) : Journey = {
    if (journey.direction == Direction.IN) journey.copy(cost = StationStorage.MAX_COST)
    else {
      previousJourney match {
        case Some(previous) =>
          val previousZones = station.getZonesBStation(previous.stationName)
          val currentZones = station.getZonesBStation(journey.stationName)
          val cost = station.getCostByZoneVisit(station.findMinZoneVisit(previousZones, currentZones), station.crossedZone(previousZones, currentZones))
          journey.copy(cost = cost)
        case None => journey.copy(cost = StationStorage.MAX_COST)
      }
    }
  }

}

object CalculationService {
  def apply(station: StationStorage): CalculationService = new CalculationService(station)
}