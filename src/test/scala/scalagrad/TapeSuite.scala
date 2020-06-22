package scalagrad

import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class TapeSuite extends ScalagradSuite {
  test("Basic derivation via chain rule") {
    // Unit tests from https://github.com/Rufflewind/revad/blob/83a86f458e7d72d45253ef805675f80e3700eab0/src/tape.rs
    val t = new Tape()
    val x = t.Var(0.5)
    val y = t.Var(4.2)
    val z = x * y + x.sin()

    val grad = z.grad()

    assert(Math.abs(z.value - 2.579425538604203) <= tol)
    assert(Math.abs(grad.wrt(x) - (y.value + Math.cos(x.value)) ) <= tol)
    assert(Math.abs(grad.wrt(y) - x.value) <= tol)
  }
}