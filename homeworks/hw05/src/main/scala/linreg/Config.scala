package linreg

import org.sellmerfud.optparse._

case class Config(
                   verbose: Boolean = false,
                   hasHeader: Boolean = true,
                   fitIntercept: Boolean = true,
                   validationSize: Int = 100,
                   trainPath: String = "./data/train.csv",
                   testPath: String = "./data/test.csv",
                   predictionsPath: String = "./data/predictions.csv",
                   weightsPath: String = "./data/weights.csv",
                   metricsPath: String = "./data/metrics.txt"
                 )

object Config {
  def parse(args: Array[String]): Config = {
    val defaults = Config()
    try {
      new OptionParser[Config] {
        banner = "linreg [options]"
        separator("")
        separator("Options:")
        flag("-v", "--verbose", "Write additional info to stdout (disabled by default).")
        { (cfg) => cfg.copy(verbose = true) }

        bool("", "--header", s"Whether the first line of csv files contains column names (default: ${defaults.hasHeader}).")
        { (v, cfg) => cfg.copy(hasHeader = v) }

        bool("-i", "--intercept", s"Whether to calculate the intercept for LR model (default: ${defaults.fitIntercept}).")
        { (v, cfg) => cfg.copy(fitIntercept = v) }

        optl[Int]("-s", "--validationSize <count>", s"The number of samples to be used for validation (default: ${defaults.validationSize}).")
          { (v, c) => c.copy(validationSize = v getOrElse defaults.validationSize) }

        optl[String]("", "--train <path>", s"File path to train dataset (default: ${defaults.trainPath}).")
          { (v, c) => c.copy(trainPath = v getOrElse defaults.trainPath) }

        optl[String]("", "--test <path>", s"File path to test dataset (default: ${defaults.testPath}).")
          { (v, c) => c.copy(testPath = v getOrElse defaults.testPath) }

        optl[String]("-p", "--predictions <path>", s"File path to save model predictions (default: ${defaults.predictionsPath}).")
          { (v, c) => c.copy(predictionsPath = v getOrElse defaults.predictionsPath) }

        optl[String]("-m", "--metrics <path>", s"File path to save model metrics (default: ${defaults.metricsPath}).")
          { (v, c) => c.copy(metricsPath = v getOrElse defaults.metricsPath) }

        optl[String]("-w", "--weights <path>", s"File path to save model weights (default: ${defaults.weightsPath}).")
          { (v, c) => c.copy(weightsPath = v getOrElse defaults.weightsPath) }
      }.parse(args, Config())
    } catch {
      case e: OptionParserException =>
        println(e.getMessage)
        sys.exit(1)
    }
  }
}