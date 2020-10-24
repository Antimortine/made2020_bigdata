package linreg

import linreg.DatasetLoader.{loadDataset, saveVector}
import linreg.Helpers.{describeDataset, evalModel}

object Main extends App {
  val config = Config.parse(args)
  println("Run 'linreg -h' or 'linreg --help' to get more information about options")

  if (config.verbose)
    println(config)

  var trainVal: Dataset = Dataset.empty()
  var test: Dataset = Dataset.empty()

  try {
    trainVal = loadDataset(config.trainPath, hasHeader = config.hasHeader).get
    test = loadDataset(config.testPath, hasHeader = config.hasHeader).get
  } catch {
    case ex: Exception =>
      ex.printStackTrace()
      sys.exit(1)
  }

  if (config.verbose) {
    describeDataset(trainVal, "TrainVal")
    describeDataset(test, "Test")
  }

  val (train, validation) = trainVal.trainValSplit(config.validationSize)

  if (config.verbose) {
    println("\nAfter Train/Val split:\n")
    describeDataset(train, "Train")
    describeDataset(validation, "Validation")
  }

  var model: LinearRegression = new LinearRegression()
  model.fit(train, config.fitIntercept)

  evalModel(model, train, validation, config.metricsPath)

  val testPrediction = model.predict(test)
  saveVector(testPrediction, config.predictionsPath)
  model.saveWeights(config.weightsPath)
}
