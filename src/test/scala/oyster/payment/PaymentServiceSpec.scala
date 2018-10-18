package oyster.payment

import org.scalatest.{FlatSpec, Matchers}
import oyster.card.{Card, CardStorage}
import oyster.journey.{Direction, Journey, JourneyStorage, Transport}

class PaymentServiceSpec extends FlatSpec with Matchers {

  val cards = CardStorage()
  val journes = JourneyStorage()
  val test = PaymentService(cards, journes)

  "PaymentService" should "make payment" in {
    val card = cards.addCard(45.7)
    val journey = Journey("code", Transport.Tube, Direction.OUT, 5.1)
    val result = test.payment(journey, card)

    journes.getHistory(1) shouldBe List(Journey("code", Transport.Tube, Direction.OUT, 5.1))
    cards.getCard(1).get shouldBe Card(1, 40.6)
  }
}
