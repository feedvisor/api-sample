import com.google.gson.Gson;
import model.ListingUpdateRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;


class HttpRestClient {
    private HttpClient client = HttpClientBuilder.create().build();
    private HttpPost post;
    private HttpGet get;
    private Map<String, String> headers;
    private HttpPut put;
    Gson gson = new Gson();
    RestTemplate restTemplate = new Re


    public HttpRestClient() {
        this.client = HttpClientBuilder.create().build();
    }

    public String sendGet(String url) throws Exception {
        get = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                get.addHeader(entry.getKey(), entry.getValue());
            }

        }
        HttpResponse response = client.execute(get);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception(response.getStatusLine().getReasonPhrase());
        }
        return EntityUtils.toString(response.getEntity());
    }


    public void addHeaders(String key, String value) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
    }

    public String sendPut(String url, List<ListingUpdateRequest> listingUpdateRequestList) throws Exception {
        put = new HttpPut(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                put.addHeader(entry.getKey(), entry.getValue());
            }

        }

        put.setEntity(new StringEntity(gson.toJson(listingUpdateRequestList)));

        HttpResponse response = client.execute(put);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception(response.getStatusLine().getReasonPhrase());
        }
        return EntityUtils.toString(response.getEntity());
    }

    public String sendPut(String url, List<ListingUpdateRequest> listingUpdateRequestList) throws Exception {
        put = new HttpPut(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                put.addHeader(entry.getKey(), entry.getValue());
            }

        }

        put.setEntity(new StringEntity(gson.toJson(listingUpdateRequestList)));

        HttpResponse response = client.execute(put);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception(response.getStatusLine().getReasonPhrase());
        }
        return EntityUtils.toString(response.getEntity());
    }
}
