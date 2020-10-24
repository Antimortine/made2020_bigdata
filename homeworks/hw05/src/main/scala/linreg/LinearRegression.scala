package linreg

import breeze.linalg.{DenseMatrix, DenseVector}
import breeze.stats.regression.{LeastSquaresRegressionResult, leastSquares}
import linreg.DatasetLoader.saveVector

class LinearRegression {
  private var model: Option[LeastSquaresRegressionResult] = None
  private var hasIntercept: Boolean = true

  def fit(dataset: Dataset, fitIntercept: Boolean = true): Unit = {
    require(dataset.target.isDefined, "target must not be None")
    hasIntercept = fitIntercept
    var features: DenseMatrix[Double] = dataset.data
    // Если требуется оценить свободный член, то нужно добавить к фичам столбец из единиц
    if (hasIntercept)
      features = addOnes(features)
    model = Some(leastSquares(features, dataset.target.get))
  }

  def predict(dataset: Dataset): DenseVector[Double] = {
    require(model.isDefined,
      "This LinearRegression instance is not fitted yet. Call 'fit' with appropriate arguments before using this estimator.")
    var features: DenseMatrix[Double] = dataset.data
    if (hasIntercept)
      features = addOnes(features)
    model.get.apply(features)
  }

  def saveWeights(path: String): Unit = {
    require(model.isDefined,
      "This LinearRegression instance is not fitted yet. Call 'fit' with appropriate arguments before using this estimator.")
    val weights: DenseVector[Double] = model.get.coefficients
    saveVector(weights, path)
  }

  private def addOnes(features: DenseMatrix[Double]): DenseMatrix[Double] = {
    val ones: DenseMatrix[Double] = DenseMatrix.ones[Double](features.rows, 1)
    DenseMatrix.horzcat(features, ones)
  }
}
