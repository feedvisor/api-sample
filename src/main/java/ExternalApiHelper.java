import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;
import static java.util.Base64.getEncoder;

class ExternalApiHelper {
    private static final String HOST = "https://api.feedvisor.com/external";

    private static final String GET_TOKEN = "/oauth/token/";
    private static final String FEED = "/{accountId}/feed";
    private static final String FEED_RESULT_FILE = "/{accountId}/feed/{requestId}/result_file";
    private static final String FEED_STATUS = "/{accountId}/feed/{requestId}";
    private static final String FEED_HISTORY = "/{accountId}/feed";
    private static final String REPORT = "/{accountId}/report";
    private static final String REPORT_FILE = "/{accountId}/report/{requestId}/file";
    private static final String REPORT_STATUS = "/{accountId}/report/{requestId}";
    private static final String REPORT_HISTORY = "/{accountId}/report";
    private static final String CUSTOMER_INFO = "/info";
    public static final String FINISHED = "Finished";
    public static final String FAILED = "Failed";
    public static final int WAIT_TIME_IN_MILL = 5000;

    public static String createAccessToken(String clientId, String clientSecret) throws Exception {
        HttpRestClient client = new HttpRestClient();
        String value = clientId + ":" + clientSecret;
        client.addHeaders("Content-Type", "application/x-www-form-urlencoded");
        client.addHeaders("Authorization", "Basic " + getEncoder().encodeToString(value.getBytes("utf-8")));
        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "client_credentials");
        String url = HOST + GET_TOKEN;
        String response = client.sendPost(url, body);
        return getFieldFromJson("access_token", response);
    }

    public static LinkedTreeMap<String, String> getAccountId(String accessToken) throws Exception {
        HttpRestClient client = new HttpRestClient();
        client.addHeaders("Authorization", "Bearer " + accessToken);
        String url = HOST + CUSTOMER_INFO;
        String response = client.sendGet(url);
        Gson gson = new GsonBuilder().create();
        Map jsonMap = gson.fromJson(response, Map.class);
        return (LinkedTreeMap<String, String>) jsonMap.get("accounts");
    }

    public static File importFeeds(String accountId, String accessToken, String fileSource) throws Exception {
        HttpRestClient client = new HttpRestClient();
        client.addHeaders("Authorization", "Bearer " + accessToken);
        //start export report
        Map<String, String> filters = new HashMap<>();
        //start export report
        String url = HOST + FEED.replace("{accountId}", accountId);
        String response = client.sendPost(url, filters, fileSource);
        String requestId = String.valueOf(Float.valueOf(getFieldFromJson("request_id", response)).intValue());


        //wait until export finished
        url = HOST + FEED_STATUS.replace("{accountId}", accountId).replace("{requestId}", requestId);

        waitUntilFinished(client, url);


        //get result file and save it
        url = HOST + FEED_RESULT_FILE.replace("{accountId}", accountId).replace("{requestId}", requestId);
        File file = new File("/result.csv");
        InputStream is = client.sendGetReturnStream(url);
        FileOutputStream fos = new FileOutputStream(file);
        int inByte;
        while ((inByte = is.read()) != -1) {
            fos.write(inByte);
        }
        is.close();
        fos.close();

        return file;

    }

    private static void waitUntilFinished(HttpRestClient client, String url) throws Exception {
        String response;
        String status;
        do {
            sleep(WAIT_TIME_IN_MILL);
            response = client.sendGet(url);
            status = getFieldFromJson("status", response);

            if (status.equals(FAILED)) {
                throw new Exception("Export report Failed");
            }

        } while (!status.equals(FINISHED));
    }

    public static XSSFWorkbook exportReport(String accountId, String accessToken, String fileTarget) throws Exception {

        HttpRestClient client = new HttpRestClient();
        client.addHeaders("Authorization", "Bearer " + accessToken);
        //start export report
        Map<String, String> filters = new HashMap<>();
        filters.put("fileType", "XLSX");
        filters.put("reportType", "CONFIGURATION");
        //start export report
        String url = HOST + REPORT.replace("{accountId}", accountId);
        String response = client.sendPost(url, filters);
        String requestId = String.valueOf(Float.valueOf(getFieldFromJson("request_id", response)).intValue());


        //wait until export finished
        url = HOST + REPORT_STATUS.replace("{accountId}", accountId).replace("{requestId}", requestId);

        waitUntilFinished(client, url);

        //get file
        url = HOST + REPORT_FILE.replace("{accountId}", accountId).replace("{requestId}", requestId);

        InputStream is = client.sendGetReturnStream(url);
        FileOutputStream fos = new FileOutputStream(fileTarget);
        int inByte;
        while ((inByte = is.read()) != -1) {
            fos.write(inByte);
        }
        is.close();
        fos.close();
        FileInputStream fis = new FileInputStream(fileTarget);
        XSSFWorkbook result = new XSSFWorkbook(fis);
        fis.close();

        return result;
    }

    private static String getFieldFromJson(String name, String json) {
        Gson gson = new GsonBuilder().create();
        Map jsonMap = gson.fromJson(json, Map.class);
        return jsonMap.get(name).toString();
    }


}
