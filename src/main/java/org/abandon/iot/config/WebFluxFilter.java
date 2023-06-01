package org.abandon.iot.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * WebFluxFilter
 *
 * @author abandon
 * @version 1.0
 * @since 2023/5/17 11:12
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class WebFluxFilter implements WebFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getPath().pathWithinApplication().value();
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        AtomicReference<String> bodyParams = new AtomicReference<>("");
        String contentType = exchange.getRequest().getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            AtomicReference<String> bodyRef = new AtomicReference<>();
            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap(dataBuffer -> {
                CharBuffer charBuffer = StandardCharsets.UTF_8.decode(dataBuffer.asByteBuffer());
                DataBufferUtils.retain(dataBuffer);
                bodyRef.set(charBuffer.toString());
                String bodyStr = RegExUtils.replaceAll(bodyRef.get(), "\\s*", "");
                bodyParams.set(bodyStr);
                showLog(method, path, queryParams, bodyParams);
                Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));
                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    @Override
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };
                return chain.filter(exchange.mutate().request(mutatedRequest).build());
            });
        }
        showLog(method, path, queryParams, bodyParams);
        return chain.filter(exchange);
    }

    private static void showLog(HttpMethod method, String path, MultiValueMap<String, String> queryParams, AtomicReference<String> bodyParams) {
        log.info("""

                ------------------------------------------------------------------
                \tHttpMethod: \t{}
                \tPath: \t\t\t{}
                \tQueryParams: \t{}
                \tBodyParams: \t{}
                ------------------------------------------------------------------
                """, method, path, queryParams, bodyParams);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
