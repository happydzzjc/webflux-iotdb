package org.abandon.iot.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4jConfig
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/15 16:21
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public GroupedOpenApi streamOpenApi(@Value("${springdoc.version}") String appVersion) {
        String[] paths = {"/iotDB/**"};
        String[] packagedToMatch = {"org.abandon.iot"};
        return GroupedOpenApi.builder().group("iotDB")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info().title("iotDB API").version(appVersion)))
                .pathsToMatch(paths).packagesToScan(packagedToMatch)
                .build();
    }

}
