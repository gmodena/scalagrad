package scalagrad.optimizer

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalagrad.{ScalagradSuite, Var}
import scalagrad.dataset.Friedman1
import scalagrad.loss.MSE
import scalagrad.network.Perceptron

@RunWith(classOf[JUnitRunner])
class OptimizerSuite extends ScalagradSuite {
  val dataset = new Friedman1(numberOfSamples=100, randomState = randomState)
  val epochs = 10

  test("We can train a simple regression model with regularized loss") {
    val model = Perceptron(2, List(16, 16, 1), randomState = randomState)
    val optim = GradientDescent(model.parameters.toSeq)

    val X = dataset.X.map(v => v.map(f => Var(f)))
    val y = dataset.y.map(y => Var(y))

    var previousLoss = Double.PositiveInfinity
    (0 until epochs).foreach(_ => {
      val output = X.map(x => model(x))
      val loss = MSE(y, output)
      val regularization = model.parameters.map(p => p**2).reduce(_ + _) * Var(1e-4)
      val lossFunction = loss + regularization

      optim.zeroGrad()
      lossFunction.backward()
      optim.step()

      assert(lossFunction.value < previousLoss)
      previousLoss = lossFunction.value
    })
  }
}
