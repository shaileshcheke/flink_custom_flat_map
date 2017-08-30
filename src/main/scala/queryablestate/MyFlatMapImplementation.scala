package queryablestate.functions

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ListStateDescriptor, ValueStateDescriptor}
import org.apache.flink.api.common.typeinfo.{TypeHint, TypeInformation}
import org.apache.flink.configuration.Configuration
import org.apache.flink.util.Collector
import queryablestate.datatypes.WordWithCount


class MyFlatMapImplementation extends RichFlatMapFunction[String,WordWithCount]{
  
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
   }
  
  override def flatMap(value: java.lang.String, out: Collector[WordWithCount]): Unit={
    val splitted: Array[String] = value.split(" ")
    
    for(iter<-splitted){
      out.collect(WordWithCount(iter,1))
    } 
  } 
  
}