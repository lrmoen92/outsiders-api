package slogan.motion.outsidersapi.handler;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Service
public class GlobalSocketHandler
        extends SocketHandler {

    public void processMessage(WebSocketSession webSocketSession, TextMessage message) throws IOException {
        Map valueMap = new Gson().fromJson(message.getPayload(), Map.class);
        webSocketSession.sendMessage(this.createTextMessage(valueMap));
    }

    public String processMapEntry(Map valueMap) {
        StringBuilder response = new StringBuilder();
        valueMap.forEach((k, v) -> response.append(k.toString() + ": " + v.toString() + ", "));
        return response.toString();
    }

    public TextMessage createTextMessage(Map valueMap) {
        return new TextMessage(this.processMapEntry(valueMap));
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        for (WebSocketSession webSocketSession : this.sessions) {
            this.processMessage(webSocketSession, message);
        }
    }
}