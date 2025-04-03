package slogan.motion.outsidersapi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import slogan.motion.outsidersapi.domain.dto.*;

import java.util.Objects;

@Service
@Slf4j
public class BattleSocketHandler
        extends SocketHandler {

    @Autowired
    protected BattleMessageService battleMessageService;

    @Autowired
    protected ObjectMapper objectMapper;

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        this.processMessage(session, message);
    }

    public boolean isTwoPlayersMessage(String type) {
        return (type.equals("MMM") || type.equals("TEM") || type.equals("CHM") || type.equals("GEM"));
    }

    public boolean sessionsShareUri(WebSocketSession session, WebSocketSession s) {
        return (Objects.equals(session.getUri(), s.getUri()) && !session.equals(s));
    }

    public void processMessage(WebSocketSession session, TextMessage message) throws Exception {

        // substring the first 3 characters, and ignore the 4th (:) for type
        String payload = message.getPayload();
        String type = payload.substring(0, 3);
        String dto = payload.substring(4);

        boolean twoPlayersMessage = isTwoPlayersMessage(type);
        String path = session.getUri().getPath();

        String arenaId = path.substring(path.lastIndexOf('/') + 1);
        WebSocketMessage<String> msg = this.createTextMessage(dto, type, arenaId);

        for (WebSocketSession s : sessions) {
            if (sessionsShareUri(session, s) && twoPlayersMessage) {
                trySend(s, msg);
                trySend(session, msg);
            }
        }
        if (!twoPlayersMessage) {
            trySend(session, msg);
        }
    }

    public TextMessage createTextMessage(String json, String type, String arenaId) throws Exception {
        return new TextMessage(this.processDto(json, type, arenaId));
    }

    public synchronized void trySend(WebSocketSession s, WebSocketMessage<String> msg) {
        try {
            synchronized (s) {
                s.sendMessage(msg);
            }
        } catch (Exception e) {
            log.error("Exception in sending websocket message: ", e);
        }
    }

    public String processDto(String dtoJson, String type, String arenaId) throws Exception {
        log.info("{}>>IN   |{}:{}", arenaId, type, dtoJson);

        String responseJson = switch (type) {
            case "MMM" -> {
                WebSocketDTO<MatchMakingDTO> dto = readStringAs(dtoJson, MatchMakingDTO.class);
                yield this.battleMessageService.handleMatchmakingMessage(dto);
            }
            case "CCM" -> {
                WebSocketDTO<CostCheckDTO> dto = readStringAs(dtoJson, CostCheckDTO.class);
                yield this.battleMessageService.handleCostCheckMessage(dto);
            }
            case "TCM" -> {
                WebSocketDTO<TargetCheckDTO> dto = readStringAs(dtoJson, TargetCheckDTO.class);
                yield this.battleMessageService.handleTargetCheckMessage(dto);
            }
            case "TEM" -> {
                WebSocketDTO<TurnEndDTO> dto = readStringAs(dtoJson, TurnEndDTO.class);
                yield this.battleMessageService.handleTurnEndMessage(dto);
            }
            case "ETM" -> {
                WebSocketDTO<EnergyTradeDTO> dto = readStringAs(dtoJson, EnergyTradeDTO.class);
                yield this.battleMessageService.handleEnergyTradeMessage(dto);
            }
            case "GEM" -> {
                WebSocketDTO<GameEndDTO> dto = readStringAs(dtoJson, GameEndDTO.class);
                yield this.battleMessageService.handleGameEndMessage(dto);
            }
            case "CHM" -> {
                WebSocketDTO<ChatDTO> dto = readStringAs(dtoJson, ChatDTO.class);
                yield this.battleMessageService.handleChatMessage(dto);
            }
            default -> "{\"error\":\"Message Type " + type + " not recognized\"}";
        };

        log.info("{}<<OUT  |{}:{}", arenaId, type, responseJson);
        return responseJson;
    }

    private <T> WebSocketDTO<T> readStringAs(String json, Class<T> clazz) throws JsonProcessingException {
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        JavaType javaType = typeFactory.constructParametricType(WebSocketDTO.class, clazz);
        return objectMapper.readValue(json, javaType);
    }
}