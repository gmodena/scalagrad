package scalagrad.network

import scalagrad.Var

class Perceptron(val numberOfInputs : Int, val numberOfOutputs: List[Int], randomState: Option[Int] = None) extends Differentiable {
  //noinspection ZeroIndexToHead
  val network: Iterable[Layer] = (numberOfInputs :: numberOfOutputs).sliding(2)
    .map(lst => Layer(lst(0), lst(1), randomState))
    .to(Iterable)

  def apply(x: Seq[Var]): Seq[Var] = {
    network.foldLeft(x)( (input, layer) => layer(input) )
  }

  val parameters: Iterable[Var] = {
    for { l <- network
          u <- l.parameters }
      yield u
  }
}

object Perceptron {
  def apply(numberOfInputs: Int, numberOfOutputs: List[Int], randomState: Option[Int] = None): Perceptron = new Perceptron(numberOfInputs, numberOfOutputs, randomState)
}