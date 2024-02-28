package slogan.motion.outsidersapi.util;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapper {
    @Bean
    ObjectMapper objectMapper(){
        return new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter());
    }
}
