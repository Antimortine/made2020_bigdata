package linreg

import breeze.linalg.DenseVector
import breeze.numerics.pow
import breeze.stats.{mean, variance}

object Metrics {
  def meanSquaredError(target: DenseVector[Double], predicted: DenseVector[Double]): Double = {
    require(target.length == predicted.length,
      s"Inconsistent number of samples: [${target.length}, ${predicted.length}]")
    val sum: Double = target.toArray
      .zip(predicted.toArray)
      .map({ case (t, p) => pow(t - p, 2) })
      .sum
    sum / target.length
  }

  def r2(target: DenseVector[Double], predicted: DenseVector[Double]): Double = {
    require(target.length == predicted.length,
      s"Inconsistent number of samples: [${target.length}, ${predicted.length}]")
    val targetVar: Double = variance(target)
    val covariance: Double = mean(pow(target - predicted, 2))
    1 - covariance / targetVar
  }
}
