package config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = "test")
public class MusicMetadataServiceTestConfig {
}
