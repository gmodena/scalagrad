package scalagrad

class Var(v: Double, val children: Set[Var] = Set()) {
  var value: Double = v
  var grad: Double = 0
  var callback: () => Unit = () => ()

  def +(that: Var): Var = {
    val z = Var(this.value + that.value, Set(this, that))
    z.callback = () => {
      this.grad += z.grad
      that.grad += z.grad
    }
    z
  }

  def *(that: Var): Var = {
    val z = Var(this.value * that.value, Set(this, that))
    z.callback = () => {
      this.grad += that.value * z.grad
      that.grad += this.value * z.grad
    }
    z
  }

  def /(that: Var): Var = {
    this * that**(-1)
  }

  def -(that: Var): Var = {
    this + (Var(-1) * that)
  }

  def **(that: Double): Var = {
    val z = Var(math.pow(this.value, that), Set(this))
    z.callback = () => {
      this.grad += that * scala.math.pow(this.value, that-1) * z.grad
    }
    z
  }

  def backward(): Unit = {
    this.grad = 1.0
    sort(this).reverse.foreach(v => v.callback())
  }
}

object Var {
  def apply(v: Double, children: Set[Var] = Set()): Var = new Var(v, children)
}