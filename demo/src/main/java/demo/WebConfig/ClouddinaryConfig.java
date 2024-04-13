package demo.WebConfig;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ClouddinaryConfig {
    private final String CLOUD_NAME = "dzq8yflu8";
    private final String API_KEY = "893129826928781";
    private final String API_SECRET= "bkHCDDwBrK0RwEoRcwT6_aVI6_0";
    @Bean
    public Cloudinary cloudinary(){
        Map<String,String> config = new HashMap();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
//        config.put("secure", true);
        return new Cloudinary(config);

    }
}
