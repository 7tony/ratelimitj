package es.moki.ratelimitj.inmemory;


import com.google.common.collect.ImmutableSet;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryRateLimiterFactoryTest {

    private InMemoryRateLimiterFactory factory = new InMemoryRateLimiterFactory();

    @Test
    void shouldReturnTheSameInstanceForSameRules() {

        RequestLimitRule rule1 = RequestLimitRule.of(Duration.ofMinutes(1), 10);
        RequestRateLimiter rateLimiter1 = factory.getInstance(ImmutableSet.of(rule1));

        RequestLimitRule rule2 = RequestLimitRule.of(Duration.ofMinutes(1), 10);
        RequestRateLimiter rateLimiter2 = factory.getInstance(ImmutableSet.of(rule2));

        assertThat(rateLimiter1).isSameAs(rateLimiter2);
    }

    @Test
    void shouldReturnTheSameInstanceForSameSetOfRules() {

        RequestLimitRule rule1a = RequestLimitRule.of(Duration.ofMinutes(1), 10);
        RequestLimitRule rule1b = RequestLimitRule.of(Duration.ofHours(1), 100);
        RequestRateLimiter rateLimiter1 = factory.getInstance(ImmutableSet.of(rule1a, rule1b));

        RequestLimitRule rule2a = RequestLimitRule.of(Duration.ofMinutes(1), 10);
        RequestLimitRule rule2b = RequestLimitRule.of(Duration.ofHours(1), 100);
        RequestRateLimiter rateLimiter2 = factory.getInstance(ImmutableSet.of(rule2a, rule2b));

        assertThat(rateLimiter1).isSameAs(rateLimiter2);
    }

    @Test
    void shouldNotReturnTheSameInstanceForSameRules() {

        RequestLimitRule rule1 = RequestLimitRule.of(Duration.ofMinutes(1), 22);
        RequestRateLimiter rateLimiter1 = factory.getInstance(ImmutableSet.of(rule1));

        RequestLimitRule rule2 = RequestLimitRule.of(Duration.ofMinutes(1), 33);
        RequestRateLimiter rateLimiter2 = factory.getInstance(ImmutableSet.of(rule2));

        assertThat(rateLimiter1).isNotSameAs(rateLimiter2);
    }
}