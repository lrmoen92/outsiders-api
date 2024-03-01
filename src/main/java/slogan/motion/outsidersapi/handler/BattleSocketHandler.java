
package slogan.motion.outsidersapi.handler;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import slogan.motion.outsidersapi.domain.dto.*;

import java.io.IOException;
import java.util.Map;

@Service
public class BattleSocketHandler
extends SocketHandler {
    public static Logger LOG = LoggerFactory.getLogger(BattleSocketHandler.class);
    
    @Autowired
    protected BattleMessageService battleMessageService;

	@Autowired
	protected ObjectMapper objectMapper;

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        this.processMessage(session, message);
    }
    
    public boolean isTwoPlayersMessage(String type) {
    	return (type.equals("MATCH_MAKING") || type.equals("TURN_END") || 
    			type.equals("CHAT") || type.equals("GAME_END"));
    }
    
    public boolean sessionsShareUri(WebSocketSession session, WebSocketSession s) {
    	return (session.getUri().equals(s.getUri()) && !session.equals(s));
    }

    public void processMessage(WebSocketSession session, TextMessage message) throws Exception {
		
		// map representing json sent in
		Map m = new Gson().fromJson(message.getPayload(), Map.class);
		String type = m.get("type").toString();
		boolean twoPlayersMessage = isTwoPlayersMessage(type);
		WebSocketMessage msg = this.createTextMessage(m);
		
	    for (WebSocketSession s : sessions) {
	    	if (sessionsShareUri(session, s) && twoPlayersMessage) {
//	    			LOG.info(m.get("type").toString() + " MESSAGE RECIEVED FROM " + session.getRemoteAddress().toString() + " MATCHED AND SENT TO " + s.getRemoteAddress().toString() + " ON ARENA : " + s.getUri().toString());
	    			trySend(s, msg);
	    			trySend(session, msg);
	    	} 
	    }
	    if (!twoPlayersMessage) {
//	    	LOG.info(m.get("type").toString() + " MESSAGE RECIEVED FROM " + session.getRemoteAddress().toString() + " AND ARENA : " + session.getUri().toString());
	    	trySend(session, msg);
	    }
	}
    
    public synchronized void trySend(WebSocketSession s, WebSocketMessage msg) {
		try {
			synchronized(s) {
	    		s.sendMessage(msg);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
    }

    public String processMapEntry(Map valueMap) throws Exception {
    	String type = valueMap.get("type").toString();
    	LOG.info("=== Incoming " + type + " Message");

    	String responseJson = switch (type) {
            case "MATCH_MAKING" -> {
				WebSocketDTO<MatchMakingDTO> dto = readMapAs(valueMap, MatchMakingDTO.class);
				yield this.battleMessageService.handleMatchmakingMessage(dto);
			}
            case "COST_CHECK" -> {
				WebSocketDTO<CostCheckDTO> dto = readMapAs(valueMap, CostCheckDTO.class);
				yield this.battleMessageService.handleCostCheckMessage(dto);
			}
            case "TARGET_CHECK" -> {
				WebSocketDTO<TargetCheckDTO> dto = readMapAs(valueMap, TargetCheckDTO.class);
				yield this.battleMessageService.handleTargetCheckMessage(dto);
			}
            case "TURN_END" -> {
				WebSocketDTO<TurnEndDTO> dto = readMapAs(valueMap, TurnEndDTO.class);
				yield this.battleMessageService.handleTurnEndMessage(dto);
			}
            case "ENERGY_TRADE" -> {
				WebSocketDTO<EnergyTradeDTO> dto = readMapAs(valueMap, EnergyTradeDTO.class);
				yield this.battleMessageService.handleEnergyTradeMessage(dto);
			}
            case "GAME_END" -> {
				WebSocketDTO<GameEndDTO> dto = readMapAs(valueMap, GameEndDTO.class);
				yield this.battleMessageService.handleGameEndMessage(dto);
			}
            default -> "{}";
        };

        LOG.info("<<< RESPONSE: " + responseJson);
        return responseJson;
    }

	private <T> WebSocketDTO<T> readMapAs(Map input, Class<T> clazz) {
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		JavaType javaType = typeFactory.constructParametricType(WebSocketDTO.class, clazz);
		return objectMapper.convertValue(input, javaType);
	}

    public TextMessage createTextMessage(Map valueMap) throws Exception {
    	String res = this.processMapEntry(valueMap);
    	if (!res.isEmpty()) {
    		return new TextMessage(res);
    	} else {
    		return new TextMessage("{}");
    	}
    }
}