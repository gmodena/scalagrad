package scalagrad.loss

import scalagrad.Var

object MSE {
  // FIXME(gmodena): must not use yHat_.head
  def apply(y: Seq[Var], yHat: Seq[Seq[Var]]): Var = y.lazyZip(yHat).map((y_, yHat_) =>{
    (y_ - yHat_.head) ** 2
  }).reduce(_ + _) / Var(y.length.toDouble)
}

