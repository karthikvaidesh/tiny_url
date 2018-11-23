import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Url implements Serializable {
    String tinyUrl;
    String longUrl;
    int count;

    public Url(String tinyUrl, String longUrl) {
        this.tinyUrl = tinyUrl;
        this.longUrl = longUrl;
        this.count = 1;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static List<String> columns() {
        List<String> columns = new ArrayList<String>();
        columns.add("tiny_url");
        columns.add("long_url");
        columns.add("count");
        return columns;
    }
}
