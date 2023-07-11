package org.abandon.iot.handler;

import org.abandon.iot.bean.IotDBService;
import org.abandon.iot.entity.IotDataBase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * IotDBHandler
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/13 11:47
 */
@Component
public class IotDBHandler {

    private final IotDBService iotDBService;

    public IotDBHandler(IotDBService iotDBService) {
        this.iotDBService = iotDBService;
    }

    public Mono<ServerResponse> showDatabases(ServerRequest request) {
        String database = request.queryParam("database").orElse("");
        return ok().contentType(MediaType.APPLICATION_JSON).body(iotDBService.showDatabases(database), List.class);
    }

    public Mono<ServerResponse> creatDatabase(ServerRequest request) {
        return request.bodyToMono(IotDataBase.class)
                .flatMap(r -> iotDBService.creatDatabase(r.getDatabase()))
                .flatMap(p -> ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(p), String.class));
    }

    public Mono<ServerResponse> deleteDatabase(ServerRequest request) {
        String database = request.pathVariable("database");
        return ok().contentType(MediaType.APPLICATION_JSON).body(iotDBService.deleteDatabase(database), String.class);
    }

    public Mono<ServerResponse> executeCustomerSQL(ServerRequest request) {
        String customerSQL = request.queryParam("sql").orElse("");
        return ok().contentType(MediaType.APPLICATION_JSON).body(iotDBService.executeCustomerSQL(customerSQL), String.class);
    }
}
