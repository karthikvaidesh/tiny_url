import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UrlLog implements Serializable {
    String id;
    String tinyUrl;
    String longUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static List<String> columns() {
        List<String> columns = new ArrayList<String>();
        columns.add("id");
        columns.add("tiny_url");
        columns.add("long_url");
        return columns;
    }
}
