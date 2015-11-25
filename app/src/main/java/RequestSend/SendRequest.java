package RequestSend;

import Http1Test.Http1ClientTest;
import Http2Test.OKhttp2ClientTest;

/**
 * Created by jinpu on 11/25/15.
 */
public class SendRequest {

    private String url;
    private int times;

    public SendRequest(String url,int times){
        this.url = url;
        this.times = times;
    }

    public void SendHttp1Request(){
        new Http1ClientTest(url,times).start();
    }

    public void SendHttp2Request(){
        new OKhttp2ClientTest(url,times).start();
    }

    public void SendBothRquest(){
        new Http1ClientTest(url,times).start();
        new OKhttp2ClientTest(url,times).start();
    }
}
