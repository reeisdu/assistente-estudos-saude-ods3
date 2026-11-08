import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class GeminiApiClient {

    // URL atualizada para o modelo 3.6 Flash
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=";

    public static String enviarPrompt(String prompt, String apiKey) {
        try {
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(20))
                    .build();

            String promptEscapado = prompt.replace("\\", "\\\\")
                                          .replace("\n", "\\n")
                                          .replace("\"", "\\\"");

            String jsonInput = "{"
                    + "\"contents\": [{\"parts\": [{\"text\": \"" + promptEscapado + "\"}]}]"
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + apiKey))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonInput, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            if (response.statusCode() == 200) {
                return extrairTextoDaResposta(response.body());
            } else {
                return "[Erro na API] Código HTTP " + response.statusCode() + " - " + response.body();
            }

        } catch (Exception e) {
            return "[Erro de Conexão] Não foi possível conectar ao Google Gemini: " + e.getMessage();
        }
    }

    private static String extrairTextoDaResposta(String jsonBody) {
        try {
            int startIndex = jsonBody.indexOf("\"text\": \"") + 9;
            if (startIndex < 9) return jsonBody;

            int endIndex = jsonBody.indexOf("\"", startIndex);
            while (jsonBody.charAt(endIndex - 1) == '\\') {
                endIndex = jsonBody.indexOf("\"", endIndex + 1);
            }

            String conteudo = jsonBody.substring(startIndex, endIndex)
                    .replace("\\n", "\n")
                    .replace("\\\"", "\"");

            return decodificarUnicode(conteudo);
        } catch (Exception e) {
            return jsonBody;
        }
    }

    private static String decodificarUnicode(String input) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '\\' && i + 5 < input.length() && input.charAt(i + 1) == 'u') {
                try {
                    int codePoint = Integer.parseInt(input.substring(i + 2, i + 6), 16);
                    sb.append((char) codePoint);
                    i += 6;
                    continue;
                } catch (NumberFormatException ignored) {}
            }
            sb.append(c);
            i++;
        }
        return sb.toString();
    }
}
