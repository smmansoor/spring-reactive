package com.innovatecode.example.springwebflux.util;

import com.innovatecode.example.springwebflux.model.CryptoCurrency;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class CryptoGenerator {

    private final MathContext mathContext = new MathContext(2);

    private final Random random = new Random();

    private final List<CryptoCurrency> cryptoList = new ArrayList<>();

    /**
     * Bootstraps the generator with tickers and initial prices
     */
    public CryptoGenerator() {
        this.cryptoList.add(new CryptoCurrency("BTC", 82.26));
        this.cryptoList.add(new CryptoCurrency("ETH", 63.74));
        this.cryptoList.add(new CryptoCurrency("ICX", 847.24));
        this.cryptoList.add(new CryptoCurrency("BCC", 65.11));
    }


    /**
     * This mehtod create a indefinite reactive stream of Crypto market price updates after specified
     * duration
     *
     * @param duration
     * @return Reactive Stream of CryptoCurrency
     */
    public Flux<CryptoCurrency> fetchCryptoStream(Duration duration) {

        return Flux.interval(duration)
                .onBackpressureDrop()
                .map(this::generateCryptoPrice)
                .flatMapIterable(crypto -> crypto)
                .log("com.innovatecode.cryptotrade");
    }

    /**
     * This method update the price and returns the list of CryptoCurrency objects
     *
     * @param interval
     * @return List of CryptoCurrency generated objects
     */
    private List<CryptoCurrency> generateCryptoPrice(long interval) {
        final Instant instant = Instant.now();
        return cryptoList.stream()
                .map(crypto -> {
                    BigDecimal priceChange = crypto.getPrice()
                            .multiply(
                                    new BigDecimal(0.05 * this.random.nextDouble()),
                                    this.mathContext
                            );
                    CryptoCurrency result =
                            new CryptoCurrency(
                                    crypto.getName(), crypto.getPrice().add(priceChange)
                            );
                    result.setInstant(instant);
                    return result;
                })
                .collect(Collectors.toList());
    }

}