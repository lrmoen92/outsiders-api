package slogan.motion.outsidersapi.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler
        extends TextWebSocketHandler {
    List<WebSocketSession> sessions = new CopyOnWriteArrayList<WebSocketSession>();

    public synchronized void afterConnectionEstablished(WebSocketSession session) {
        this.sessions.add(session);
    }

    public synchronized void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.sessions.remove(session);
    }
}
