import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class CurrencyConverter {

    public static void main(String[] args) throws IOException {

        HashMap<Integer, String> currencyCodes = new HashMap<>();

        // add currency codes
        currencyCodes.put(1, "USD");
        currencyCodes.put(2, "EUR");
        currencyCodes.put(3, "GBP");
        currencyCodes.put(4, "JPY");
        currencyCodes.put(5, "KRW");

        String fromCode, toCode;
        double amount;

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to the currency converter");

        System.out.println("Currency converting FROM/*");
        System.out.println("1:u$s (Dollar) \t 2:€ (Euros) \t 3:£ (Libras Esterlinas) \t 4:¥ (Yen Japones) \t 5:₩ (Won sul-coreano)");
        fromCode = currencyCodes.get(sc.nextInt());

        System.out.println("Currency converting TO?");
        System.out.println("1:u$s (Dollar) \t 2:€ (Euros) \t 3:£ (Libras Esterlinas) \t 4:¥ (Yen Japones) \t 5:₩ (Won sul-coreano)");
        toCode = currencyCodes.get(sc.nextInt());

        System.out.println("Amount you wish to convert");
        amount = sc.nextDouble();

        // Agregar tu clave de acceso (API Access Key) aquí
        String API_KEY = "7e73cac16cfdc6abafa1941e4a047f28";
        sendHttpGetRequest(fromCode, toCode, amount, API_KEY);

        System.out.println("Thank you for using the currency converter!");
    }

    public static void sendHttpGetRequest(String fromCode, String toCode, double amount, String API_KEY) throws IOException {

        String GET_URL = "http://api.exchangeratesapi.io/v1/latest?base" + fromCode + "&symbols=" + toCode + "&access_key=" + API_KEY;
        URL url = new URL(GET_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        int responseCode = httpURLConnection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Éxito: Leer la respuesta JSON
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject obj = new JSONObject(response.toString());

            // Verificar si la clave "rates" existe en el objeto JSON
            if (obj.has("rates")) {
                double exchangeRate = obj.getJSONObject("rates").getDouble(toCode);
                double convertedAmount = amount / exchangeRate;

                System.out.println("Exchange Rate: 1 " + fromCode + " = " + exchangeRate + " " + toCode);
                System.out.printf("%.2f %s = %.2f %s%n", amount, fromCode, convertedAmount, toCode);
            } else {
                System.out.println("Error: Conversion rate not available.");
            }
        } else {
            System.out.println("GET request failed!");
        }
    }
}

