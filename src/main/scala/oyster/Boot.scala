package oyster

import oyster.card.CardStorage
import oyster.journey.{Direction, Journey, JourneyStorage, Transport}
import oyster.payment.{PaymentService, ValidationService}
import oyster.zone.{CalculationService, StationStorage}


object Boot extends App {

  val cardStorage = CardStorage()
  val journeyStorage = JourneyStorage()

  val test = Service(cardStorage, journeyStorage, CalculationService(StationStorage()), ValidationService(), PaymentService(cardStorage, journeyStorage))


  val card = test.createCard(30)

  val first = Journey("Holborn", Transport.Tube, Direction.IN, 0)
  val result = List(Journey("Earl’s Court", Transport.Tube, Direction.OUT, 0),
    Journey("328bus", Transport.Bus, Direction.IN, 0),
    Journey("Earl’s Court", Transport.Tube, Direction.IN, 0),
    Journey("Hammersmith", Transport.Tube, Direction.OUT, 0)).
    foldLeft(test.doJourney(first, card))((a, b) => a.flatMap(s => test.doJourney(b, s._1)))

  result.right.map(p => println("Balance - " + p._1.balance))

  println("History : ")

  test.journeyHistory(card).foreach(println(_))


}
