package linreg

import breeze.linalg.{DenseMatrix, DenseVector}
import math.floor

class Dataset(val data: DenseMatrix[Double],
              val target: Option[DenseVector[Double]] = None,
              val columns: Option[Array[String]] = None) {

  def this(data: DenseMatrix[Double], target: DenseVector[Double], columns: Option[Array[String]]) {
    this(data, Option(target), columns)
  }

  def this(data: DenseMatrix[Double], target: DenseVector[Double], columns: Array[String]) {
    this(data, Option(target), Option(columns))
  }

  def this(data: DenseMatrix[Double], target: DenseVector[Double]) {
    this(data, Option(target))
  }

  require(target.isEmpty || target.get.length == data.rows,
    "The length of 'target' must match the number of rows in 'data'")

  def trainValSplit(valSize: Int): (Dataset, Dataset) = {
    require(valSize > 0 && valSize < data.rows)
    val trainSize: Int = data.rows - valSize
    val trainData: DenseMatrix[Double] = data(0 until trainSize, ::)
    val valData: DenseMatrix[Double] = data(trainSize to -1, ::)
    target match {
      case None => (new Dataset(trainData, columns = columns), new Dataset(valData, columns = columns))
      case Some(trg) =>
        val trainTarget: DenseVector[Double] = trg(0 until trainSize)
        val valTarget: DenseVector[Double] = trg(trainSize to -1)
        (new Dataset(trainData, trainTarget, columns),
          new Dataset(valData, valTarget, columns))
    }
  }

  def trainValSplit(testSize: Double): (Dataset, Dataset) = {
    require(testSize > 0.0 && testSize < 1.0)
    trainValSplit(floor(testSize * data.rows).toInt)
  }
}

object Dataset {
  def empty(): Dataset = {
    new Dataset(new DenseMatrix[Double](0, 0))
  }
}
