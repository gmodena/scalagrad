package scalagrad.dataset

import scala.util.Random

/**
 * From https://www.rdocumentation.org/packages/mlbench/versions/2.1-1/topics/mlbench.friedman1
 *
 * The regression problem Friedman 1 as described in Friedman (1991)
 * and Breiman (1996). Inputs are 10 independent variables uniformly
 * distributed on the interval [0,1], only 5 out of these 10 are actually used.
 * Outputs are created according to the formula
 * y=10sin(πx1x2)+20(x3−0.5)2+10x4+5x5+e
 *
 * @param numberOfSamples number of samples to generate
 * @param numberOfFeatures number of features
 * @param randomState random state
 */
class Friedman1(val numberOfSamples: Int = 100,
                val numberOfFeatures: Int = 5,
                val randomState: Option[Int] = None)  {
  require(numberOfFeatures > 4)
  require(numberOfSamples > 0)

  private val random = {
    if (randomState.isEmpty) {
      new Random()
    } else {
      new Random(seed = randomState.get)
    }
  }

  val X: Seq[Vector[Double]] = (0 until numberOfSamples).map(_ => {
    Vector.fill(numberOfFeatures)(random.nextDouble())
  })

  val y: Seq[Double] = X.map(x => {
      10 * Math.sin(Math.PI * x(0) * x(1)) + 20 * Math.pow(x(2) - 0.5, 2) + 10 * x(3) + 5 * x(4) + random.nextGaussian()
  })
}

object Friedman1 {
  def apply(numberOfSamples: Int = 100, numberOfFeatures: Int = 5, randomState: Option[Int] = None): Friedman1 = new Friedman1(numberOfSamples, numberOfFeatures, randomState)
}