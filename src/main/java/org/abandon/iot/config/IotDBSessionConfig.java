package org.abandon.iot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;

/**
 * IotDBSessionConfig
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/12 14:40
 */
@Slf4j
@Configuration
public class IotDBSessionConfig {

    @Value("${spring.iotdb.user:root}")
    private String user;
    @Value("${spring.iotdb.password:root}")
    private String password;
    @Value("${spring.iotdb.host:127.0.0.1}")
    private String host;
    @Value("${spring.iotdb.port:6667}")
    private int port;
    @Value("${spring.iotdb.maxSize:100}")
    private int maxSize;

    @Bean
    public SessionPool iotSession() {
        return new SessionPool(host, port, user, password, maxSize, ZoneId.of("+08:00"));
    }

}
