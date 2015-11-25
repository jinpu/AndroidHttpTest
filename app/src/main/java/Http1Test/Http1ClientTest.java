package Http1Test;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by jinpu on 11/25/15.
 */
public class Http1ClientTest extends Thread{

    private String url;
    private HttpClient client;
    private HttpResponse response;
    private int requestTimes;

    private long request_start_time;
    private long request_end_time;
    private long request_total_time;

    private Gson gson = new Gson();

    public Http1ClientTest(String url,int times){
        this.url = url;
        this.requestTimes = times;
    }

    public void DoHttpClientGet(){
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Connection","keep-alive");
        client = new DefaultHttpClient();
        try {
            for(int i=0;i<requestTimes;++i) {
            //记录发送请求的时间
            request_start_time = System.currentTimeMillis();
            //发送请求
            response = client.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    //记录响应的时间
                    request_end_time = System.currentTimeMillis();
                    //获取响应消息实体
                    String content = EntityUtils.toString(response.getEntity());
                    //获取响应头
                    Header[] headers = response.getAllHeaders();
                    Hashtable<String, String> hashtable = new Hashtable<String, String>();
                    //保存请求的url
                    hashtable.put("request-url", url);
                    //保存响应的状态行信息
                    hashtable.put("Statusline", response.getStatusLine().toString());
                    //将响应头信息进行保存
                    for (Header h : headers) {
                        //String str = h.getName()+" : "+h.getValue();
                        //Log.i("jinputest",str);
                        hashtable.put(h.getName(), h.getValue());
                    }
                    Log.i("jinputest", "第" + i + "次http/1.1请求花费" + (request_end_time - request_start_time) + " ms");
                    //Log.i("jinputest",gson.toJson(hashtable).toString());
                    //Log.i("jinputest", content);
                }
                    request_total_time += (request_end_time - request_start_time);
                }
                Log.i("jinputest", "发送 " + requestTimes + "次http/1.1请求花费总时间：" + request_total_time + "ms");
                Log.i("jinputest","发送 "+ requestTimes + "次http/1.1请求平均时间："+(request_total_time/requestTimes)+"ms");
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }finally {
               client.getConnectionManager().shutdown();
            }
        }

    @Override
    public void run() {
        DoHttpClientGet();
    }
}
