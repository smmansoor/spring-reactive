package com.innovatecode.example.springwebflux.controller;


import com.innovatecode.example.springwebflux.model.CryptoCurrency;
import com.innovatecode.example.springwebflux.util.CryptoGenerator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Controller
public class CryptoTradeController {



    @GetMapping(value = "/trade", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    Flux<CryptoCurrency> getCryptoTradePrices() {

        return new CryptoGenerator().fetchCryptoStream(Duration.ofMillis(1000));
    }


    @GetMapping("/")
    String home() {
        return "cryptoTrade";
    }

}
