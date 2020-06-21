package scalagrad.optimizer

import scalagrad.Var
import scalagrad.network.Differentiable

class GradientDescent(val params: Seq[Var], val lr: Double) extends Differentiable {
  def step(): Unit = {
    for (p <- params) {
      p.value -= p.grad * lr
    }
  }

  override def parameters: Iterable[Var] = params
}

object GradientDescent {
  def apply(params: Seq[Var], lr: Double = 1e-2): GradientDescent = new GradientDescent(params, lr)
}
