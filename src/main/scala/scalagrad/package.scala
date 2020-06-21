import scala.collection.mutable

package object scalagrad {
  def sort(v: Var): Array[Var] = {
    var nodes: Array[Var] = Array()
    val visited: mutable.Set[Var] = mutable.Set()

    def aux(v: Var): Unit = {
      if (!visited.contains(v)) {
        visited += v
        v.children.foreach(child => aux(child))
        nodes :+= v
      }
    }
    aux(v)
    nodes
  }

  def sin(x: Var): Var = {
    val z = new Var(Math.sin(x.value), Set(x))
    z.callback = () => {
      x.grad += Math.cos(x.value)
    }
    z
  }
}
