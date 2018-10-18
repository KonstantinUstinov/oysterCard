package oyster

import org.scalatest.{FlatSpec, Matchers}
import oyster.card.{Card, CardStorage}
import oyster.journey.{Direction, Journey, JourneyStorage, Transport}
import oyster.payment.{PaymentService, ValidationError, ValidationService}
import oyster.zone.{CalculationService, StationStorage}

class ServiceSpec extends FlatSpec with Matchers {

  val cardStorage = CardStorage()
  val journeyStorage = JourneyStorage()

  val test = Service(cardStorage, journeyStorage, CalculationService(StationStorage()), ValidationService(), PaymentService(cardStorage, journeyStorage))

  "Service" should "do Bus journey" in {
    val card = test.createCard(30)

    val result = test.doJourney(Journey("Bus", Transport.Bus, Direction.IN, 0), card)

    result.isRight shouldBe true
    result.right.exists(p => p._1 == Card(1, 28.2)) shouldBe true
    result.right.exists(p => p._2 == Journey("Bus", Transport.Bus, Direction.IN, 1.8)) shouldBe true

    test.journeyHistory(card) shouldBe List(Journey("Bus", Transport.Bus, Direction.IN, 1.8))
  }

  "Service" should "do Bus journey  - return Error" in {
    val card = test.createCard(1)
    val result = test.doJourney(Journey("Bus", Transport.Bus, Direction.IN, 0), card)

    result.isLeft shouldBe true
    result.left.exists(p => p  == ValidationError("Do not have enough money to travel.")) shouldBe true
  }

  "Service" should "validate" in {
    val card = test.createCard(1)
    val result = test.validateJourney(Journey("Bus", Transport.Bus, Direction.IN, 0), Journey("Bus", Transport.Bus, Direction.IN, 0), card)
    result.isLeft shouldBe true

    test.validateJourney(Journey("Bus", Transport.Bus, Direction.IN, 0), Journey("Bus", Transport.Bus, Direction.OUT, 0), card)
    .isLeft shouldBe true

    val card2 = test.createCard(5)
    test.validateJourney(Journey("Bus", Transport.Bus, Direction.IN, 0), Journey("Bus", Transport.Bus, Direction.OUT, 0), card2)
        .isRight shouldBe true
  }

  "Service" should "do Tube journey" in {
    val card = test.createCard(30)

    val result = test.doJourney(Journey("Earl’s Court", Transport.Tube, Direction.IN, 0), card)

    result.isRight shouldBe true
    result.right.exists(p => p._1 == Card(5, 26.8)) shouldBe true
    result.right.exists(p => p._2 == Journey("Earl’s Court", Transport.Tube, Direction.IN, 3.2)) shouldBe true

    test.journeyHistory(card) shouldBe List(Journey("Earl’s Court", Transport.Tube, Direction.IN, 3.2))

  }

  val plannedJourney =
    """
      -Tube Holborn to Earl’s Court
      -328 bus from Earl’s Court to Chelsea
      -Tube Earl’s court to Hammersmith"""

  "Service" should s"complete Journey $plannedJourney in £6.3 " in {
    val card = test.createCard(30)

    val first = Journey("Holborn", Transport.Tube, Direction.IN, 0)
    val result = List(Journey("Earl’s Court", Transport.Tube, Direction.OUT, 0),
         Journey("328bus", Transport.Bus, Direction.IN, 0),
         Journey("Earl’s Court", Transport.Tube, Direction.IN, 0),
         Journey("Hammersmith", Transport.Tube, Direction.OUT, 0)).
      foldLeft(test.doJourney(first, card))((a, b) => a.flatMap(s => test.doJourney(b, s._1)))

    result should be('right)
    result.right.exists(p => p._1.balance == 17.3)  shouldBe true
  }
}
