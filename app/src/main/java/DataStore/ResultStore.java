package DataStore;

import com.google.gson.Gson;
import com.squareup.okhttp.Headers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jinpu on 11/16/15.
 */
public class ResultStore {
    private String url;
    private long request_start_time;
    private long request_end_time;
    private String data;
    List<Headers> headersList;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setRequest_start_time(long request_start_time) {
        this.request_start_time = request_start_time;
    }

    public long getRequest_start_time() {
        return request_start_time;
    }

    public void setRequest_end_time(long request_end_time) {
        this.request_end_time = request_end_time;
    }

    public long getRequest_end_time() {
        return request_end_time;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setHeadersList(List<Headers> headersList) {
        this.headersList = headersList;
    }

    public List<Headers> getHeadersList() {
        return headersList;
    }
}
