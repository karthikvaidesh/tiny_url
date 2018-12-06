import com.datastax.driver.core.Row;
import com.datastax.spark.connector.CassandraRowMetadata;
import com.datastax.spark.connector.ColumnRef;
import com.datastax.spark.connector.cql.TableDef;
import com.datastax.spark.connector.rdd.reader.RowReader;
import com.datastax.spark.connector.rdd.reader.RowReaderFactory;
import scala.collection.IndexedSeq;

import java.io.Serializable;

public class UrlRowReader extends GenericRowReader<UrlLog> {
    private static RowReader<UrlLog> reader = new UrlRowReader();

    public UrlLog read(Row row, CassandraRowMetadata rowMetaData) {
        UrlLog urlLog = new UrlLog();
        urlLog.setId(row.getString(0));
        urlLog.setTime(row.getTimestamp(2));
        urlLog.setTinyUrl(row.getString(3));
	urlLog.setLongUrl(row.getString(1));
        return urlLog;
    }

    public static class UrlRowReaderFactory implements RowReaderFactory<UrlLog>, Serializable {

        public RowReader<UrlLog> rowReader(TableDef table, IndexedSeq<ColumnRef> selectedColumns) {
            return reader;
        }

        public Class<UrlLog> targetClass() {
            return UrlLog.class;
        }
    }
}
