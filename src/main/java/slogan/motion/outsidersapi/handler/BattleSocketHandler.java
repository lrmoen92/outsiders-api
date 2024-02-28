
package slogan.motion.outsidersapi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import slogan.motion.outsidersapi.domain.dto.CostCheckDTO;

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
//		LOG.info("Literally Anything");
//		LOG.info(message.toString());
//		LOG.info(message.getPayload());
		
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
    
    public synchronized void trySend(WebSocketSession s, WebSocketMessage msg) throws IOException, InterruptedException {
		try {
			synchronized(s) {
	    		s.sendMessage(msg);
			}
		} catch (Exception e) {
			LOG.info(e.getMessage());
		}
    }

	private String mapToString(Map<Object, Object> valueMap) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry entry : valueMap.entrySet()) {
			sb.append("\"" + entry.getKey().toString() + "\": \"" + entry.getValue().toString() + "\",");
		}
		return sb.toString();
	}

    public String processMapEntry(Map valueMap) throws Exception {
    	String type = valueMap.get("type").toString();
    	LOG.info("=== Incoming " + type + " Message");

    	String responseJson = "{}";
		switch (type) {
		    case "MATCH_MAKING":

//				objectMapper.convertValue(valueMap, MatchMakingDTO.class);
//		      LOG.info("Match Making...");
		      responseJson = this.battleMessageService.handleMatchmakingMessage(valueMap);
		    case "COST_CHECK":
//				CostCheckDTO dto = objectMapper.convertValue(valueMap, CostCheckDTO.class);
//		      LOG.info("Cost Check");
		      responseJson = this.battleMessageService.handleCostCheckMessage(valueMap);
		    case "TARGET_CHECK":
//				objectMapper.convertValue(valueMap, TargetCheckDTO.class);
//		      LOG.info("Target Check");
		      responseJson = this.battleMessageService.handleTargetCheckMessage(valueMap);
		    case "TURN_END":
//				objectMapper.convertValue(valueMap, TurnEndDTO.class);
//		      LOG.info("Turn End");
		      responseJson = this.battleMessageService.handleTurnEndMessage(valueMap);
    	  	case "ENERGY_TRADE":
//				objectMapper.convertValue(valueMap, EnergyTradeDTO.class);
//		      LOG.info("Energy Trade");
		      responseJson = this.battleMessageService.handleEnergyTradeMessage(valueMap);
    	  	case "GAME_END":
//				objectMapper.convertValue(valueMap, GameEndDTO.class);
//		      LOG.info("Game End");
		      responseJson = this.battleMessageService.handleGameEndMessage(valueMap);
	    } 

        LOG.info("<<< RESPONSE: " + responseJson);
        return responseJson;
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