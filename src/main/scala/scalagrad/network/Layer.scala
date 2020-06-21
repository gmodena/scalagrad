package scalagrad.network

import scalagrad.Var

class Layer(val numberOfInputs: Int, val numberOfOutputs : Int, randomState: Option[Int] = None) extends Differentiable {
  val neurons: Seq[Neuron] = (0 until numberOfOutputs).toList.map(_ => Neuron(numberOfInputs, randomState))

  def apply(x: Seq[Var]): Seq[Var] = {
    neurons.map(neuron => neuron(x))
  }

  val parameters: Iterable[Var] = {
    for { n <- neurons
          p <- n.parameters }
      yield p
  }
}

object Layer {
  def apply(numberOfInputs: Int, numberOfOutputs: Int, randomState: Option[Int]): Layer = new Layer(numberOfInputs, numberOfOutputs, randomState)
}