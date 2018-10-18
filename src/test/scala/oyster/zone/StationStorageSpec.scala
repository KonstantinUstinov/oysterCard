package oyster.zone

import org.scalatest.{FlatSpec, Matchers}

class StationStorageSpec extends FlatSpec with Matchers {

  val test = StationStorage()

  "StationStorage" should "return max cost" in {
    test.getCostByZoneVisit(test.findMinZoneVisit(List(3), List(5)), test.crossedZone(List(3), List(5))) shouldBe  3.2
  }

  "StationStorage" should "return 2.0" in {
    test.getCostByZoneVisit(test.findMinZoneVisit(List(1,2), List(2)), test.crossedZone(List(1,2), List(2))) shouldBe  2.0
  }

  "StationStorage" should "return 2.5" in {
    test.getCostByZoneVisit(test.findMinZoneVisit(List(1), List(1)), test.crossedZone(List(1), List(1))) shouldBe  2.5
  }

  "StationStorage" should "return 3.0" in {
    test.getCostByZoneVisit(test.findMinZoneVisit(List(1), List(2)), test.crossedZone(List(1), List(2))) shouldBe  3.0
  }

  "StationStorage" should "return 2.25" in {
    test.getCostByZoneVisit(test.findMinZoneVisit(List(2), List(3)), test.crossedZone(List(2), List(3))) shouldBe  2.25
  }

}
