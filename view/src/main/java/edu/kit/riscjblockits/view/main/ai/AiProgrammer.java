package edu.kit.riscjblockits.view.main.ai;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

/**
 * A class that queries the OpenAI API for RISC-V assembler code.
 */
public class AiProgrammer {
    private static final String ERROR_KEY_NOT_FOUND = "No API key found. Please add your OpenAI API key to the Instruction Set.";
    private static final String ERROR_MESSAGE = "An error occurred while trying to access the OpenAI API. \n Is your key correct?";
    private static final String OPEN_AI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String PROMPT = "You are an github copilot style ai that only outputs Risc-V assembler code based on the users wishes and the already available code. You can only write valid code with comments. Don't explain the code. The code should not include syscalls.";
    private final String openAiApiKey;
    private  boolean keyFound = true;

    /**
     * Creates a new AiProgrammer. This class can answer queries with RISC-V assembler code.
     * @param key the OpenAI API key
     */
    public AiProgrammer(String key) {
        openAiApiKey = key;
        if (openAiApiKey == null ||  openAiApiKey.isEmpty()) {
            keyFound = false;
        }
    }

    /**
     * Queries the OpenAI API for RISC-V assembler code.
     *
     * @param query the query to send to the API
     * @return the response from the API or an Error message.
     */
    public String queryAi(String query) {
        if (!keyFound) {
            return ERROR_KEY_NOT_FOUND;
        }
        query = query.replace("\n", " ");
        String response = "";
        HttpClient client = HttpClient.newHttpClient();
        String json = "{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n" +
                "      {\n" +
                "        \"role\": \"system\",\n" +
                "        \"content\": \"" + PROMPT + "\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"role\": \"user\",\n" +
                "        \"content\": \" "+ query + "\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(OPEN_AI_API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + openAiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
            response = httpResponse.body();
        } catch (Exception e) {
            return "";
        }
        return extractContent(response);
    }

    private static String extractContent(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            return message.getString("content");
        } catch (JSONException e) {
            return ERROR_MESSAGE;
        }
    }

}
