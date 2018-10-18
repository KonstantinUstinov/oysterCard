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

  "Service" should "return Error" in {
    val card = test.createCard(1)
    val result = test.doJourney(Journey("Bus", Transport.Bus, Direction.IN, 0), card)

    result.isLeft shouldBe true
    result.left.exists(p => p  == ValidationError("Do not have enough money to travel.")) shouldBe true
  }

}
