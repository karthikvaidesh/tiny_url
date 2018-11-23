import com.datastax.spark.connector.ColumnRef;
import com.datastax.spark.connector.cql.TableDef;
import com.datastax.spark.connector.writer.RowWriter;
import com.datastax.spark.connector.writer.RowWriterFactory;
import scala.Tuple2;
import scala.collection.IndexedSeq;
import scala.collection.Seq;

import java.io.Serializable;

public class UrlRowWriter implements RowWriter<Tuple2<String, Url>> {

    private static RowWriter<Tuple2<String, Url>> writer = new UrlRowWriter();

    public Seq<String> columnNames() {
        return scala.collection.JavaConversions.asScalaBuffer(Url.columns()).toList();
    }

    public void readColumnValues(Tuple2<String, Url> data, Object[] buffer) {
        buffer[0] = data._2.getTinyUrl();
        buffer[1] = data._2.getLongUrl();
        buffer[2] = data._2.getCount();
    }

    public static class UrlRowWriterFactory implements RowWriterFactory<Tuple2<String, Url>>, Serializable {

        public RowWriter<Tuple2<String, Url>> rowWriter(TableDef table, IndexedSeq<ColumnRef> selectedColumns) {
            return writer;
        }
    }
}
