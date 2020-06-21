package scalagrad.network

import scalagrad.Var

trait Differentiable {
  def parameters: Iterable[Var]

  def  zeroGrad(): Unit =
    this.parameters
      .foreach(_.grad = 0)
}
