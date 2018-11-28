import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import model.ListingUpdateRequest;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
import static java.util.Base64.getEncoder;

class ExternalApiHelper {
    private static final String HOST = "https://api.feedvisor.com/external";

    private static final String GET_TOKEN = "/oauth/token/";
    private static final String UPDATE_LISTINGS = "/{accountId}/listings";
    private static final String CUSTOMER_INFO = "/info";
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

    public static String updateListings(String accountId, String accessToken, List<ListingUpdateRequest> listingUpdateRequestList) throws Exception {
        HttpRestClient client = new HttpRestClient();
        client.addHeaders("Authorization", "Bearer " + accessToken);
        String url = HOST + UPDATE_LISTINGS.replace("{accountId}", accountId);
        return client.sendPut(url, listingUpdateRequestList);
    }

    private static String getFieldFromJson(String name, String json) {
        Gson gson = new GsonBuilder().create();
        Map jsonMap = gson.fromJson(json, Map.class);
        return jsonMap.get(name).toString();
    }


}
