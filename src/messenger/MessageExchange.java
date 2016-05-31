package messenger;
import messenger.functional.entities.Message;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.List;

class MessageExchange {

    private JSONParser jsonParser = new JSONParser();

    private String getToken(int index) {
        Integer number = index * 8 + 11;

        return "TN" + number + "EN";
    }

    int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    String getServerResponse(List<Message> tasks, int index) {
        List<Message> chunk = tasks.subList(index, tasks.size());
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("messages", chunk);
        jsonObject.put("token", getToken(tasks.size()));

        return jsonObject.toJSONString();
    }

    Message getClientMessage(InputStream inputStream) throws Exception {
        JSONObject json = getJSONObject(inputStreamToString(inputStream));

        return new Message((String)json.get("id"), (String)json.get("text"),
                (String)json.get("author"), (String)json.get("time"), (String)json.get("date"));
    }

    String getIdToDelete(InputStream inputStream) throws Exception {
        JSONObject json = getJSONObject(inputStreamToString(inputStream));

        return  (String)json.get("id");
    }

    String getErrorMessage(String text) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("error", text);

        return jsonObject.toJSONString();
    }

    private JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json.trim());
    }

    private String inputStreamToString(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;

        while ((length = in.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }

        System.out.println("Input stream " + new String(baos.toByteArray()));
        return new String(baos.toByteArray());
    }
}
