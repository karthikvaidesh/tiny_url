import com.datastax.spark.connector.japi.CassandraJavaUtil;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

import java.io.Serializable;
import java.util.List;

public class TinyUrlSparkAggregator implements Serializable {
    private transient SparkConf conf;

    JavaPairRDD<String, Url> urlLogRDD;

    private TinyUrlSparkAggregator(SparkConf conf) {
        this.conf = conf;
    }

    private void run() {
        JavaSparkContext sc = new JavaSparkContext(conf);
        compute(sc);
        showResults(sc);
        sc.stop();
    }

    private void compute(JavaSparkContext sc) {
        System.out.println("In compute");
        urlLogRDD = CassandraJavaUtil
                .javaFunctions(sc)
                .cassandraTable("url_logs", "urls", new UrlRowReader.UrlRowReaderFactory())
                .keyBy(new Function<UrlLog, String>() {
                    public String call(UrlLog urlLog) throws Exception {
                        System.out.println(urlLog.getTinyUrl());
                        return urlLog.getTinyUrl();
                    }
                })
                .mapToPair(new PairFunction<Tuple2<String, UrlLog>, String, Url>() {
                    public Tuple2<String, Url> call(Tuple2<String, UrlLog> stringUrlLogTuple2) throws Exception {
                        return new Tuple2<String, Url>(stringUrlLogTuple2._1, new Url(stringUrlLogTuple2._2.getTinyUrl(), stringUrlLogTuple2._2.getLongUrl()));
                    }
                })
                .reduceByKey(new Function2<Url, Url, Url>() {
                    public Url call(Url url, Url url2) throws Exception {
                        url.setCount(url.getCount() + url2.getCount());
                        return url;
                    }
                });
    }

    private void showResults(JavaSparkContext sc) {
        System.out.println("Showing results");
        CassandraJavaUtil
                .javaFunctions(urlLogRDD)
                .writerBuilder("url_logs", "summary", new UrlRowWriter.UrlRowWriterFactory())
                .saveToCassandra();

        List<Tuple2<String, Url>> list = urlLogRDD.collect();
        System.out.println("------Contents of wordsRDD------");
        for (Tuple2<String, Url> tuple : list) {
            System.out.println("(" + tuple._1 + " : " + tuple._2.getCount() + ")");
        }
        System.out.println("--------------------------------");
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf();
        conf.setAppName("Url aggregator");
        conf.setMaster("local[*]");
        conf.set("spark.cassandra.connection.host", "127.0.0.1");

        TinyUrlSparkAggregator app = new TinyUrlSparkAggregator(conf);
        app.run();
    }
}
