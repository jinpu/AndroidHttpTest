package Http2Test;

import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;

import DataStore.ResultStore;

/**
 * Created by jinpu on 11/25/15.
 */
public class OKhttp2ClientTest extends Thread{
        private OkHttpClient client_h2;
        private Response response;
        private Request request;

        private Gson gson = new Gson();
        private long total_time;

        private String url;
        private int times;

        public OKhttp2ClientTest(String url,int times) {
            this.url = url;
            this.times = times;

        }

        public void  ConNet(){
            //创建一个okhttpclient
            client_h2 = new OkHttpClient();
            //为创建好的OKhttpClient设置使用的协议列表（个数大于一个时，会自动与服务器协商要使用的协议）
            client_h2.setProtocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1));

            for (int i = 0; i <times; ++i) {
            //构建request
            request = new Request.Builder().url(url).build();
            try{
                   //向服务器发送request;
                    response=client_h2.newCall(request).execute();
                   //创建一个用于保存response的结构
                    Hashtable<String , String>  hashtable = new Hashtable<String, String>() ;

                    hashtable.put( "url" , url );
                    //hashtable.put( "data" , response.body().string() );
                    //获取response头部信息
                    Headers headers = response.headers();
                    //根据response头部信息中的OkHttp-Sent-Millis和OkHttp-Received-Millis两个字段计算请求时间
                    String OkHttp_Sent_Millis = headers.get("OkHttp-Sent-Millis");
                    String send_time = OkHttp_Sent_Millis.substring(4, OkHttp_Sent_Millis.length());
                    int send = Integer.parseInt(send_time);

                    String OkHttp_Received_Millis = headers.get("OkHttp-Received-Millis");
                    String receive_time = OkHttp_Received_Millis.substring(4, OkHttp_Sent_Millis.length());
                    int receive = Integer.parseInt(receive_time);
                    int request_time = receive - send;
                    Log.i("jinputest", "第" + i + "次http/2.0请求花费" + request_time + " ms");

                    //计算总时间
                    total_time+=request_time;
                    //将头部信息一次放入已经创建好的数据结构中
                    for (int j = 0; j < headers.size(); j++) {
                        hashtable.put( headers.name(j) , headers.value(j));
                    }

                    //Log.i("jinputest", gson.toJson(hashtable));

            }catch (IOException e){
                    e.printStackTrace();
            }
        }
        Log.i("jinputest","发送 "+ times + "次http/2.0请求花费总时间："+total_time+"ms");
        Log.i("jinputest","发送 "+ times + "次http/2.0请求平均时间："+(total_time/times)+"ms");
        total_time = 0;//total = 0;
        }
    @Override
    public void run() {
        ConNet();
    }
}


