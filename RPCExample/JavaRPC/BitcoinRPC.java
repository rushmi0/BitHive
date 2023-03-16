import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BitcoinRPC {

    public static void main(String[] args) {
        String rpcUser = "admin";
        String rpcPassword = "admin123";
        String rpcUrl = "http://localhost:8332/";

        String authString = rpcUser + ":" + rpcPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

        try {
            URL url = new URL(rpcUrl);
            HttpURLConnection Connect = (HttpURLConnection) url.openConnection();
            Connect.setRequestMethod("POST");
            Connect.setRequestProperty("Content-Type", "application/json");
            Connect.setRequestProperty("Authorization", "Basic " + encodedAuth);

            String method = "createmultisig";
            int Signature = 2;
            
            /*
             * [ 2, [ "02769edae0b315eec56d48b2ace560a036e0b6cc060b2a7e3b8c7300534bfbfd7b", 
             *        "02fa6666b46f86e423bd3ca73f71688e25936df4779628704d8f9f7485c198b3c5", 
             *        "04f3a9729bc891f2cab0852ab958b055782bee622ea54e5c7f7bc174451bbbf8e5655e527f17b83746711a939922f11985b90c3967d711ada16be906990ece1693" ] ]
             */

            Object[] params = new Object[] {

                    Signature, new Object[] {

                        /*
                         * Private Key 1: L3Qzpgd619vNdELW52u4FUSmCBSy3bV7SGzR2z8WP3zidHyZcKKs
                         * Public Key 1: 02769edae0b315eec56d48b2ace560a036e0b6cc060b2a7e3b8c7300534bfbfd7b
                         *
                         * Private Key 2: KyQPqi3oCci7os8KL5UXxXjYchZCh2hRsi6BrSNHsq7j733bsD5V
                         * Public Key 2: 02fa6666b46f86e423bd3ca73f71688e25936df4779628704d8f9f7485c198b3c5
                         *
                         * Private Key 3: 5JAnH28H3hcHqBAPkXWgoiTg3KVzDZ2zPm7NT8twmaHA3ZPeZSc
                         * Public Key 3: 04f3a9729bc891f2cab0852ab958b055782bee622ea54e5c7f7bc174451bbbf8e5655e527f17b83746711a939922f11985b90c3967d711ada16be906990ece1693
                         */

                        // Public Key : 1
                        "02769edae0b315eec56d48b2ace560a036e0b6cc060b2a7e3b8c7300534bfbfd7b",

                        // Public Key : 2
                        "02fa6666b46f86e423bd3ca73f71688e25936df4779628704d8f9f7485c198b3c5",

                        // Public Key : 3
                        "04f3a9729bc891f2cab0852ab958b055782bee622ea54e5c7f7bc174451bbbf8e5655e527f17b83746711a939922f11985b90c3967d711ada16be906990ece1693"

                    }
            };

            String Script = arrayToParamsString(params);

            String payload = "{ \"jsonrpc\": \"2.0\", "
                            + "\"method\": \"" + method + "\", "
                            + "\"params\": " + Script + ", "
                            + "\"id\": 0 }";

            byte[] postData = payload.getBytes(StandardCharsets.UTF_8);
            int request = postData.length;

            Connect.setRequestProperty("Content-Length", Integer.toString(request));
            Connect.setUseCaches(false);
            Connect.setDoOutput(true);

            DataOutputStream wr = new DataOutputStream(Connect.getOutputStream());
            wr.write(postData);

            BufferedReader in = new BufferedReader(new InputStreamReader(Connect.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String Result = response.toString();

            System.out.println(Result);
        }

        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String arrayToParamsString(Object[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (int i = 0; i < arr.length; i++) {

            Object obj = arr[i];

            if (obj instanceof Object[]) {
                sb.append(arrayToParamsString((Object[]) obj));
            }

            else if (obj instanceof String) {
                sb.append("\"").append(obj.toString()).append("\"");
            }

            else {
                sb.append(obj.toString());
            }

            if (i != arr.length - 1) {
                sb.append(", ");
            }
        }

        sb.append(" ]");
        return sb.toString();
    }
}
