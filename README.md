![](https://github.com/gmodena/scalagrad/workflows/build/badge.svg)
# scalagrad
A tiny, scalar-valued, reverse mode automatic differentiation library inspired by 
[PyTorch](https://pytorch.org/) and Andrej Karpathy's [micrograd](https://github.com/karpathy/micrograd/).

`scalagrad` has been designed to educate myself, and to serve as a building block for
further experimentation (e.g. source code transformation, tape-based autodiff, ...). 

There's no performance optimizations,
and the current code won't scale past toy datasets. The construction of a dynamic graph of derivatives is heavy on heap allocations,
and the backward adjoint gradients calculation step requires a linear ordering of the nodes.The latter is achieved by a modified depth-first search
on the graph (e.g. a topological sort). A good overview of this approach can be found in UWashington [CSE599W](https://dlsys.cs.washington.edu/) lecture notes 
[https://dlsys.cs.washington.edu/pdf/lecture4.pdf](https://dlsys.cs.washington.edu/pdf/lecture4.pdf)

# Build

Build with
`./gradlew build`

# Examples
This sections contains two basic examples of `scalagrad` in action. More can be found under `tests`.

## Differentiation

The code below shows how to compute the derivative of the expression `z = x * y + sin(x)`.

```
import scalagrad.Var

val x = Var(0.5)
val y = Var(4.2)
val z = x * y + sin(x)

z.backward()
println(z.grad)
```

## Training a neural network

The code below shows how to train a 2-layers neural network to solve the
[Friedman1](https://projecteuclid.org/euclid.aos/1176347963) regression problem.
```
import scalagrad.Var
import scalagrad.dataset.Friedman1
import scalagrad.loss.MSE
import scalagrad.network.Perceptron

val randomState = Some(12345)
val dataset = new Friedman1(numberOfSamples=100, randomState = randomState)
val model = Perceptron(2, List(16, 16, 1), randomState = randomState) // A 2-layers neural network
val optim = GradientDescent(model.parameters.toSeq)

val X = dataset.X.map(v => v.map(f => Var(f)))
val y = dataset.y.map(y => Var(y))

(0 until epochs).foreach(_ => {
  val output = X.map(x => model(x))
  val loss = MSE(y, output)
  val regularization = model.parameters.map(p => p**2).reduce(_ + _) * Var(1e-4)
  val lossFunction = loss + regularization

  optim.zeroGrad()
  lossFunction.backward()
  optim.step()
})
```

# References
- https://rufflewind.com/2016-12-30/reverse-mode-automatic-differentiation
- Automatic differentiation in machine learning: a survey, Bayding et. al. 2015.https://arxiv.org/abs/1502.05767
- The Stan Math Library: Reverse-Mode Automatic Differentiation in C++, Carpenter et. al. 2015. https://arxiv.org/abs/1509.07164
