package linreg

final case class CsvFormatException(private val message: String = "Invalid CSV format: inconsistent number of columns")
  extends Exception(message)
