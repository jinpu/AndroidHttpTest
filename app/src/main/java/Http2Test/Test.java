package Http2Test;

import android.app.DownloadManager;
import android.support.annotation.StringDef;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;

import DataStore.ResultStore;
import okio.Buffer;

/**
 * Created by jinpu on 11/16/15.
 */
public class Test {
    private OkHttpClient client_h2;
    private OkHttpClient client_h1;
    private Response response;
    private Request request;
    private long request_time_start;
    private long request_time_finish;
    private Gson gson = new Gson();
    private long total_time;
    private long total;

    public Response ConNet(String url,String port,int request_times) throws IOException {

        client_h2 = new OkHttpClient();
        client_h2.setProtocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1));

        client_h1 = new OkHttpClient();
        client_h1.setProtocols(Arrays.asList(Protocol.HTTP_1_1)).setConnectionPool(new ConnectionPool(100,10000));

        if(port!=null){
            url+=":"+port;
        }
        //Log.i("jinputest", url);

        for (int i = 0; i < request_times; ++i) {

            String Url = url;
            request = new Request.Builder().url(Url)
                     .addHeader("Connection","keep-alivex")
                    .build();
            Log.i("jinputest",request.toString());


            //request_time_start = System.currentTimeMillis();
            response = client_h1.newCall(request).execute();
            //request_time_finish = System.currentTimeMillis();
            //Log.i("first way","第" + i + "次http/1.1请求花费" + (request_time_finish - request_time_start) + " ms");

            //total += (request_time_finish - request_time_start);

            Hashtable<String, String> hashtable = new Hashtable<String, String>();

            hashtable.put("url", url);
            //hashtable.put("data", response.body().string());

            Headers headers = response.headers();

            String OkHttp_Sent_Millis = headers.get("OkHttp-Sent-Millis");
            String send_time = OkHttp_Sent_Millis.substring(4, OkHttp_Sent_Millis.length());
            int send = Integer.parseInt(send_time);

            String OkHttp_Received_Millis = headers.get("OkHttp-Received-Millis");
            String receive_time = OkHttp_Received_Millis.substring(4, OkHttp_Sent_Millis.length());
            int receive = Integer.parseInt(receive_time);
            int request_time = receive - send;
            Log.i("second way", "第" + i + "次http/1.1请求花费" + request_time + " ms");

            total_time+=request_time;


            for (int j = 0; j < headers.size(); j++) {
                hashtable.put(headers.name(j), headers.value(j));
            }


           Log.i("jinputest",gson.toJson(hashtable).toString());
        }

        Log.i("second way", "发送 " + request_times + "次http/1.1请求花费总时间：" + total_time + "ms");
        Log.i("second way","发送 "+ request_times + "次http/1.1请求平均时间："+(total_time/request_times)+"ms");

        //Log.i("first way", "发送 " + request_times + "次http/1.1请求花费总时间：" + total + "ms");
        //Log.i("first way","发送 "+ request_times + "次http/1.1请求平均时间："+(total/request_times)+"ms");
        total_time = 0;//total = 0;


        /*
        for (int i = 0; i < request_times; ++i) {
            //ResultStore resultStore = new ResultStore();
           // List<Headers> headersList = new ArrayList<Headers>();

            String Url = url + "?test="+i;
            request = new Request.Builder().url(Url).build();
            Log.i("jinputest",request.toString());

            //request_time_start = System.currentTimeMillis();
            response=client_h2.newCall(request).execute();
            //request_time_finish = System.currentTimeMillis();

            //total += (request_time_finish - request_time_start);

            //Log.i("first way", "第" + i + "次http/2.0请求花费" + (request_time_finish - request_time_start) + " ms");

            Hashtable<String , String>  hashtable = new Hashtable<String, String>() ;

            hashtable.put( "url" , url );
            //hashtable.put( "data" , response.body().string() );

            Headers headers = response.headers();
            String OkHttp_Sent_Millis = headers.get("OkHttp-Sent-Millis");
            String send_time = OkHttp_Sent_Millis.substring(4, OkHttp_Sent_Millis.length());
            int send = Integer.parseInt(send_time);

            String OkHttp_Received_Millis = headers.get("OkHttp-Received-Millis");
            String receive_time = OkHttp_Received_Millis.substring(4, OkHttp_Sent_Millis.length());
            int receive = Integer.parseInt(receive_time);
            int request_time = receive - send;
            Log.i("second way", "第" + i + "次http/2.0请求花费" + request_time + " ms");

            total_time+=request_time;
            for (int j = 0; j < headers.size(); j++) {
                hashtable.put( headers.name(j) , headers.value(j));
            }

              Log.i("TAG", gson.toJson(hashtable)) ;
//            resultStore.setRequest_start_time(request_time_start);
//            resultStore.setRequest_end_time(request_time_finish);
//            resultStore.setData(response_h2.body().string());
//            resultStore.setUrl(url);
//            headersList.add(headers);
//            resultStore.setHeadersList(headersList);

//            list_h2.add(resultStore);
//            Print_json(resultStore);

        }

        Log.i("second way","发送 "+ request_times + "次http/2.0请求花费总时间："+total_time+"ms");
        Log.i("second way","发送 "+ request_times + "次http/2.0请求平均时间："+(total_time/request_times)+"ms");

        //Log.i("first way","发送 "+ request_times + "次http/2.0请求花费总时间："+total+"ms");
        //Log.i("first way","发送 "+ request_times + "次http/2.0请求平均时间："+(total/request_times)+"ms");
        total_time = 0;//total = 0;
        */
        return response;
    }

    public void Print_json(ResultStore resultStore){
        System.out.println(gson.toJson(resultStore));
    }
}

