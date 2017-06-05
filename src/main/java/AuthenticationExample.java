import com.google.gson.internal.LinkedTreeMap;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;


public class AuthenticationExample {

    private static final String CLIENT_ID = "Y0u2Cl1N71d";
    private static final String CLIENT_SECRET = "Y0u2Cl1N753c237";
    private static final String FILE_TARGET = "AuthenticationExample.xlsx";


    static public void main(String args[]) throws IOException {
        try {
            //exchange the client credentials for an access token
            String accessToken = ExternalApiHelper.createAccessToken(CLIENT_ID, CLIENT_SECRET);

            //get account ids (id of the storefront)
            LinkedTreeMap<String, String> accountIds = ExternalApiHelper.getAccountId(accessToken);

            //get a configurations report for the first storefront
            String accountId = accountIds.keySet().iterator().next();
            XSSFWorkbook file = ExternalApiHelper.exportReport(accountId, accessToken, accountId + "_" + FILE_TARGET);

            //changing a listing to reprice
            XLSXHelper.setCellValue(2, "Reprice", "Yes", 1, 1, file);
            XLSXHelper.saveFile(accountId + "_" + FILE_TARGET, file);

            //sending a feed
            File resultFile = ExternalApiHelper.importFeeds(accountId, accessToken, accountId + "_" + FILE_TARGET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
