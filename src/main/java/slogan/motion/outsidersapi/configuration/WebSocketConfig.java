package slogan.motion.outsidersapi.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import slogan.motion.outsidersapi.handler.BattleSocketHandler;
import slogan.motion.outsidersapi.handler.GlobalSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig
  implements WebSocketConfigurer
{
  @Autowired
  private BattleSocketHandler battleSocketHandler;
  @Autowired
  private GlobalSocketHandler globalSocketHandler;
  
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
  {
    registry.addHandler(battleSocketHandler, "/arena/{arenaId}").setAllowedOrigins("*");
    registry.addHandler(globalSocketHandler, "/chat").setAllowedOrigins("*");
  }
 
}
