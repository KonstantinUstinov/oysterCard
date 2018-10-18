package oyster.journey

import org.scalatest.{FlatSpec, Matchers}

class JourneyStorageSpec extends FlatSpec with Matchers {

  val test = JourneyStorage()

  "JourneyStorage" should " add Journey" in {
    test.addJourney(1234, Journey("code", Transport.Bus, Direction.IN, 3))
    test.addJourney(1234, Journey("code", Transport.Tube, Direction.OUT, 3))

    test.getHistory(1234) shouldBe List(Journey("code", Transport.Bus, Direction.IN, 3), Journey("code", Transport.Tube, Direction.OUT, 3))
  }

  "JourneyStorage" should " return Tube Journey" in {
    test.addJourney(1234, Journey("coder", Transport.Tube, Direction.OUT, 3))

    test.getLastTube(1234).get shouldBe Journey("coder", Transport.Tube, Direction.OUT, 3)
  }

}
