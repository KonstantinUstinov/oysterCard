package oyster.journey

import scala.collection.mutable.ListBuffer


class JourneyStorage {

  private val listOfJourney = ListBuffer[(Int, Journey)]()

  def addJourney(cardNumber: Int, journey: Journey) : Unit = {
    (cardNumber, journey) +=: listOfJourney
  }

  def getHistory(cardNumber: Int) : List[Journey] = {
    listOfJourney.filter(_._1 == cardNumber).map(_._2).toList.reverse
  }

  def getLastTube(cardNumber: Int) : Option[Journey] = {
    listOfJourney.find(j => j._1 == cardNumber && j._2.transport == Transport.Tube).map(_._2)
  }

}

object JourneyStorage {
  def apply(): JourneyStorage = new JourneyStorage()
}
