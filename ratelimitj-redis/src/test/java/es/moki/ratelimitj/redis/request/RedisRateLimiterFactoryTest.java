package es.moki.ratelimitj.redis.request;


import com.google.common.collect.ImmutableSet;
import es.moki.ratelimitj.core.limiter.request.RequestLimitRule;
import es.moki.ratelimitj.core.limiter.request.RequestRateLimiter;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedisRateLimiterFactoryTest {

    private RedisClient client = mock(RedisClient.class);

    private StatefulRedisConnection<String, String> connection = mock(StatefulRedisConnection.class);
    private RedisReactiveCommands<String, String> commands = mock(RedisReactiveCommands.class);

    private RedisRateLimiterFactory factory;

    @BeforeEach
    void beforeEach() {
        factory = new RedisRateLimiterFactory(client);
        when(client.connect()).thenReturn(connection);
        when(connection.reactive()).thenReturn(commands);
    }

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