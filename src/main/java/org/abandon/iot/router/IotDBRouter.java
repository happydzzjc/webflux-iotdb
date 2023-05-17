package org.abandon.iot.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.abandon.iot.entity.IotDataBase;
import org.abandon.iot.handler.IotDBHandler;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * IotDBRouter
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/13 11:14
 */
@Slf4j
@Configuration
public class IotDBRouter {
    private final static String BASE_PATH = "/iotDB";

    private final IotDBHandler iotDBHandler;

    public IotDBRouter(IotDBHandler iotDBHandler) {
        this.iotDBHandler = iotDBHandler;
    }

    @RouterOperations({
            @RouterOperation(path = "/iotDB/showDatabases", method = RequestMethod.GET, beanClass = IotDBHandler.class, beanMethod = "showDatabases", operation = @Operation(operationId = "showDatabases", summary = "查询数据库", parameters = @Parameter(name = "database", description = "数据库名称", example = "root.**", required = true), responses = {@ApiResponse(content = @Content(schema = @Schema(implementation = IotDataBase.class)))})),
            @RouterOperation(path = "/iotDB/creatDatabase", method = RequestMethod.POST, beanClass = IotDBHandler.class, beanMethod = "creatDatabase", operation = @Operation(operationId = "creatDatabase", summary = "创建数据库", parameters = @Parameter(name = "database", description = "数据库名称", required = true))),
            @RouterOperation(path = "/iotDB/deleteDatabase/{database}", method = RequestMethod.DELETE, beanClass = IotDBHandler.class, beanMethod = "deleteDatabase", operation = @Operation(operationId = "deleteDatabase", summary = "删除数据库", parameters = @Parameter(name = "database", description = "数据库名称", required = true)))
    })
    @Bean
    public RouterFunction<ServerResponse> iotDBRoutersFunction() {
        return route()
                .GET(BASE_PATH + "/showDatabases", iotDBHandler::showDatabases)
                .POST(BASE_PATH + "/creatDatabase", accept(APPLICATION_JSON), iotDBHandler::creatDatabase)
                .DELETE(BASE_PATH + "/deleteDatabase/{database}", iotDBHandler::deleteDatabase)
                .build();
    }
}
