package scalagrad.network

import scalagrad.Var

import scala.util.Random


class Neuron(val numberOfInputs: Int, val randomState: Option[Int] = None) extends Differentiable {
  protected val random : Random= {
    if (randomState.isEmpty) {
      new Random()
    } else {
      new Random(seed = randomState.get)
    }
  }

  val b: Var =  Var(0.0)
  val w: Seq[Var] = (0 until numberOfInputs).toList.map(_ => Var(random.nextDouble()))

  val parameters: Iterable[Var] = {
    for (elem <- w :+ b) yield elem
  }
  def apply(x: Seq[Var]): Var = {
    this.w.lazyZip(x).map(_ * _).reduce(_ + _) + this.b
  }
}

object Neuron {
  def apply(numberOfInputs: Int, randomState: Option[Int]): Neuron = new Neuron(numberOfInputs, randomState)
}
