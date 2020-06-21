package scalagrad

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

trait ScalagradSuite extends AnyFunSuite{
 val tol = 1e-15
 val seed = 12345
 val randomState: Option[Int] = Some(seed)
 val random = new Random(seed = seed)

}
