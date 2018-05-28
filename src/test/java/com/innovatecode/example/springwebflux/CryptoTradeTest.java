package com.innovatecode.example.springwebflux;


import com.innovatecode.example.springwebflux.model.CryptoCurrency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = CryptoTradeApplication.class)
public class CryptoTradeTest {

    @Test
    public void testCryptoTrade() throws InterruptedException {

        Flux<CryptoCurrency> stream = WebClient.create("http://localhost:8080")
                .get().uri("/trade")
                .retrieve()
                .bodyToFlux(CryptoCurrency.class);
        stream.subscribe(sse ->
                Assert.assertTrue(
                        sse.getPrice().compareTo(BigDecimal.ZERO) > 0
                                && sse.getName().matches("BTC|BCC|ETH|ICX")
                )
        );

    }
}
