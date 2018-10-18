package oyster.card

import org.scalatest.{FlatSpec, Matchers}

class CardStorageSpec extends FlatSpec with Matchers {

  val testCase = CardStorage()


  "CardStorage" should "save card" in {
    val result = testCase.addCard(12.6)

    testCase.getCard(result.number).get shouldBe Card(result.number, 12.6)

    val result2 = testCase.addCard(12.7)

    testCase.getCard(result2.number).get shouldBe Card(result2.number, 12.7)
  }

  "CardStorage" should "update Balance " in {
    testCase.updateBalance(1, 2.2)

    testCase.getCard(1).get shouldBe Card(1, 2.2)
  }

  "CardStorage" should "return Empty " in {
    testCase.getCard(3) shouldBe None
  }
}
