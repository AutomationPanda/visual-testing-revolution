package com.automationpanda;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PractiTestResultPublisher implements TestWatcher {

    private final static Map<String, Integer> testIds = new HashMap<>() {{
       put("login()", 52753746);
    }};

    private void publishResult(int instanceId, int exitCode) {
        try {
            String email = System.getenv().get("PRACTITEST_EMAIL");
            String token = System.getenv().get("PRACTITEST_TOKEN");

            String authString = email + ":" + token;
            String encodedAuth = Base64.getEncoder().encodeToString(authString.getBytes());

            String data = "{\"data\": { \"type\": \"instances\", \"attributes\": {\"instance-id\": " + instanceId +
                    ", \"exit-code\": " + exitCode + " }}}";

            URL obj = new URL("https://api.practitest.com/api/v2/projects/23465/runs.json");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            int code = con.getResponseCode();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        int instanceId = testIds.get(context.getDisplayName());
        publishResult(instanceId, 1);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        int instanceId = testIds.get(context.getDisplayName());
        publishResult(instanceId, 0);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        int instanceId = testIds.get(context.getDisplayName());
        publishResult(instanceId, 1);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        int instanceId = testIds.get(context.getDisplayName());
        publishResult(instanceId, 1);
    }
}
