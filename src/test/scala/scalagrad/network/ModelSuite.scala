package scalagrad.network
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner
import scalagrad.{ScalagradSuite, Var}

@RunWith(classOf[JUnitRunner])
class ModelSuite extends ScalagradSuite {
  val numberOfInputs = 5
  val numberOfOutputs = 3
  val numberOfSamples = 100
  val X1d: Vector[Var] = (0 until numberOfSamples).map(_ => Var(random.nextDouble())).toVector
  val X2d: Vector[Vector[Var]] = (0 until numberOfSamples).map(_ => {
      Vector(Var(random.nextDouble()), Var(random.nextDouble()))
  }).toVector

  test("Neuron instantiation") {
    val neuron = new Neuron(numberOfInputs)

    assert(neuron.w.length === numberOfInputs)
    assert(neuron.parameters.toSeq.length === (numberOfInputs + 1) )
  }

  test("Layer instantiation") {
    val layer = new Layer(numberOfInputs, numberOfOutputs)

    assert(layer.neurons.length === numberOfOutputs)
    assert(layer.parameters.toSeq.length === (numberOfOutputs * numberOfInputs) + numberOfOutputs )
    val output = layer(X1d)
    assert(output.length === numberOfOutputs)

  }

  test("MLP model") {
    val numberOfInputs = 2
    val numberOfOutputs = List(16, 16, 1)
    val model = new Perceptron(numberOfInputs, numberOfOutputs) // 2-layer neural network

    val expectedNumberOfParameters = 337
    assert(model.parameters.toSeq.length === expectedNumberOfParameters)

    model.zeroGrad()

    val pred1d = X1d.map(x => model(List(x)))
    assert(pred1d.length === X1d.length)
    assert(pred1d(0).length === 1)

    val pred2d = X2d.map(x => model(x))
    assert(pred2d.length === X2d.length)
    assert(pred2d(0).length === 1)
  }
}
