import org.apache.spark.{SparkConf, SparkContext}

object CountWord {
  def main(args: Array[String]) {

    System.setProperty("hadoop.home.dir", "C:\\dev\\winutils")

    val conf = new SparkConf()
      //.setMaster("local[*]")
      .setMaster("spark://192.168.99.100:7077")
      .setAppName("Test")

    val sc = new SparkContext(conf)
    /*
        val logFile = "C:\\source\\CAS-BDA\\bitcoinextractor\\src\\main\\resources\\bitcoin-historical-data\\coinbase.csv" // Should be some file on your system
        val logData = sc.textFile(logFile, 2).cache()
        val numAs = logData.filter(line => line.contains("a")).count()
        println("Lines with a: %s".format(numAs))
    */

    val lines = sc.parallelize(
      Seq("Spark Intellij Idea Scala test one",
        "Spark Intellij Idea Scala test two",
        "Spark Intellij Idea Scala test three"))


    val counts = lines
      .flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    counts.foreach(println)

    sc.stop()

  }
}