package linreg

import java.io.{BufferedWriter, File, FileWriter}

import linreg.Metrics.{meanSquaredError, r2}

object Helpers {
  def describeDataset(dataset: Dataset, name: String): Unit = {
    println(s"$name features shape: ${dataset.data.rows}*${dataset.data.cols}")
    println(s"$name features count: ${dataset.data.cols}")
    println(s"$name has feature names: ${dataset.columns.isDefined}")
    if (dataset.columns.isDefined)
      println(s"$name feature names: ${dataset.columns.get.mkString(", ")}")
    println(s"$name has target: ${dataset.target.isDefined}")
    if (dataset.target.isDefined)
      println(s"$name target length: ${dataset.target.get.length}")
  }

  def evalModel(model: LinearRegression,
                train: Dataset,
                validation: Dataset,
                metricsPath: String): Unit = {
    val trainPrediction = model.predict(train)
    val valPrediction = model.predict(validation)

    val trainMSE = meanSquaredError(train.target.get, trainPrediction)
    val trainR2 = r2(train.target.get, trainPrediction)
    println(s"Train MSE: $trainMSE")
    println(s"Train R^2: $trainR2")

    val valMSE = meanSquaredError(validation.target.get, valPrediction)
    val valR2 = r2(validation.target.get, valPrediction)
    println(s"Validation MSE: $valMSE")
    println(s"Validation R^2: $valR2")

    val file: File = new File(metricsPath)
    val bw: BufferedWriter = new BufferedWriter(new FileWriter(file))
    bw.write(s"Train MSE: $trainMSE\n")
    bw.write(s"Train R^2: $trainR2\n")
    bw.write(s"Validation MSE: $valMSE\n")
    bw.write(s"Validation R^2: $valR2\n")
    bw.close()
  }
}
