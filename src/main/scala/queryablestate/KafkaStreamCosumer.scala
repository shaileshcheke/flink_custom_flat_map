package queryablestate.functions

import org.apache.flink.streaming.api.scala._
import java.util.Properties
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.windowing.time.Time

object KafkaStreamCosumer{
  
  def main(args: Array[String]) : Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

    val prop = new Properties()
    prop.setProperty("bootstrap.servers", "localhost:9092");
    prop.setProperty("zookeeper.connect", "localhost:2181");
    prop.setProperty("group.id", "test");
    val dataStreamSource = env.
      addSource(new FlinkKafkaConsumer010[String]("test", new SimpleStringSchema(), prop))
    val keyStream = dataStreamSource.map(_.toString)
      .flatMap(new MyFlatMapImplementation())
      .keyBy(0)
      .timeWindow(Time.seconds(20))
      .sum(1)
    keyStream.map(_.toString).addSink(new FlinkKafkaProducer010[String]("localhost:9092", "output", new SimpleStringSchema()))
    keyStream.map(_.toString).print()
    env.execute("KafkaStreamCosumer")
  }

}