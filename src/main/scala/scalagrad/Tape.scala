package scalagrad

case class TapeNode(weights: Seq[Double], deps: Seq[Short])

class TapeVar(val tape: Tape, val index: Short, val value: Double) {
  def grad(): Grad = {
    val nodes = tape.nodes
    val len = nodes.length
    val derivs = Array.fill(len)(0.0)
    derivs(this.index) = 1.0

    (len-1 until 0 by -1).foreach(i => {
      val node = nodes(i)
      val deriv = derivs(i)
      (0 until 2).foreach(j => {
        derivs(node.deps(j)) += node.weights(j) * deriv
      })
    })

    new Grad(derivs)
  }

  def +(that: TapeVar): TapeVar = {
    val index = this.tape.push2(1.0, this.index, 1.0, that.index)
    new TapeVar(this.tape, index, this.value + that.value )
  }

  def +(that: Double): TapeVar = {
    val index = this.tape.push1(1.0, this.index)
    new TapeVar(this.tape, index, this.value + that )
  }

  def *(that: TapeVar): TapeVar = {
    val index = this.tape.push2(that.value, this.index, this.value, that.index)
    new TapeVar(this.tape, index, this.value * that.value )
  }

  def sin(): TapeVar = {
    val index = this.tape.push1(Math.cos(this.value), this.index)
    new TapeVar(this.tape, index, Math.sin(this.value))
  }

}

class Grad(val dervis: Array[Double]) {
  def wrt(v: TapeVar): Double = {
    this.dervis(v.index)
  }
}

class Tape {
  var nodes: Array[TapeNode] = Array()

  def Var(value: Double): TapeVar = {
    new TapeVar(this, this.push0(), value)
  }

  def push0(): Short = {
    val index = nodes.length.toShort
    nodes :+= TapeNode(Seq(0.0, 0.0), Seq(index, index) )
    index
  }

  def push1(weight: Double, dep: Short): Short  = {
    val index = nodes.length.toShort
    nodes :+= TapeNode(Seq(weight, 0.0), Seq(dep, index) )
    index
  }
  def push2(weight: Double, dep: Short,
            otherWeight: Double, otherDep: Short): Short = {
    val index = nodes.length.toShort
    nodes :+= TapeNode(Seq(weight, otherWeight), Seq(dep, otherDep) )
    index
  }
}
