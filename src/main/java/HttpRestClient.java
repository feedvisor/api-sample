import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
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


    public InputStream sendGetReturnStream(String url) throws Exception {
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
        return response.getEntity().getContent();
    }

    public String sendPost(String url, Map<String, String> bodyParam) throws Exception {
        post = new HttpPost(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }

        }
        List<NameValuePair> nvps = new ArrayList<>();
        for (Map.Entry<String, String> entry : bodyParam.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        post.setEntity(new UrlEncodedFormEntity(nvps));

        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new Exception(response.getStatusLine().getReasonPhrase());
        }
        return EntityUtils.toString(response.getEntity());

    }

    public String sendPost(String url, Map<String, String> bodyParam, String filePath) throws Exception {
        post = new HttpPost(url);
        post.addHeader("enctype", "multipart/form-data");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                post.addHeader(entry.getKey(), entry.getValue());
            }

        }

        File file = new File(filePath);
        FileBody fileBody = new FileBody(file);
        MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
        multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntity.addPart("file", fileBody);
        multipartEntity.addTextBody("fileName", file.getName());
        for (Map.Entry<String, String> entry : bodyParam.entrySet()) {
            multipartEntity.addTextBody(entry.getKey(), entry.getValue());
        }
        post.setEntity(multipartEntity.build());
        HttpResponse response = client.execute(post);
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

}
