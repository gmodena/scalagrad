package scalagrad.dataset

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalagrad.ScalagradSuite

@RunWith(classOf[JUnitRunner])
class DatasetSuite extends ScalagradSuite {
  test("Friedman1 fails when numberOfFeatures < 4") {
    assertThrows[java.lang.IllegalArgumentException] { Friedman1(numberOfFeatures = 4, randomState = randomState) }
  }

  test("Friedman1 fails when numberOfSamples = 0") {
    assertThrows[java.lang.IllegalArgumentException] { Friedman1(numberOfSamples = 0, randomState = randomState) }
  }
}