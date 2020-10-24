package linreg

import java.io.{BufferedWriter, File, FileWriter}

import breeze.linalg.{Axis, DenseMatrix, DenseVector}

import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try, Using}

object DatasetLoader {
  def readCsv(path: String, hasHeader: Boolean): Try[(DenseMatrix[Double], Option[Array[String]])] = {
    Using(io.Source.fromFile(path)) { source => {
      var columns: Option[Array[String]] = None
      val iter: Iterator[String] = source.getLines
      if (hasHeader) {
        columns = Some(iter.next.split(','))
      }
      val values: Array[Array[Double]] = iter
        .map(line => line.split(',').map(str => str.toDouble))
        .toArray
      // Проверяем, что количество чисел во всех строках одинаковое
      val isCorrect: Boolean = values
        .map(elements => elements.length)
        .distinct
        .length == 1
      // А если есть header, то и с его длинной сравниваем
      if (!isCorrect || columns.isDefined && values.head.length != columns.get.length)
        throw new CsvFormatException
      val matrix = DenseMatrix(values: _*)
      return Success((matrix, columns))
    }
    }
  }

  def loadDataset(path: String, hasHeader: Boolean, targetName: String = "target"): Try[Dataset] = {
    val dataset = readCsv(path, hasHeader = hasHeader)
    dataset match {
      case Success(pair) =>
        var (data, columns): (DenseMatrix[Double], Option[Array[String]]) = pair
        if (columns.isDefined) {
          // Если в CSV был заголовок, то ищем там targetName
          val targetIndex: Int = columns.get.indexOf(targetName)
          // Если не нашли targetName, то считаем, что таргета нет
          if (targetIndex < 0)
            return Success(new Dataset(data, columns = columns))
          // Иначе выносим target в отдельный вектор, удаляем его из матрицы, а также targetName из columns
          val target: DenseVector[Double] = data(::, targetIndex)
          data = data.delete(targetIndex, Axis._1)
          val featureNames: ArrayBuffer[String] = ArrayBuffer(columns.get: _*)
          featureNames.remove(targetIndex)
          return Success(new Dataset(data, target, featureNames.toArray))
        }
        Success(new Dataset(data))
      case Failure(s) =>
        Console.err.println(s"Can't load dataset from '$path': $s")
        Failure(s)
    }
  }

  def saveVector(vector: DenseVector[Double], path: String): Unit = {
    val file: File = new File(path)
    val bw: BufferedWriter = new BufferedWriter(new FileWriter(file))
    for (value <- vector) {
      bw.write(value.toString + "\n")
    }
    bw.close()
  }
}
