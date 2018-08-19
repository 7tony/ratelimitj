package es.moki.ratelimitj.core.limiter.request;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * Defines a limit rule that can support regular and token bucket rate limits.
 */
@ParametersAreNonnullByDefault
public class RequestLimitRule {

    private final int durationSeconds;
    private final long limit;
    private final int precision;
    private final String name;

    private RequestLimitRule(int durationSeconds, long limit, int precision) {
        this(durationSeconds, limit, precision, null);
    }

    private RequestLimitRule(int durationSeconds, long limit, int precision, String name) {
        this.durationSeconds = durationSeconds;
        this.limit = limit;
        this.precision = precision;
        this.name = name;
    }

    /**
     * Initialise a request rate limit. Imagine the whole duration window as being one large bucket with a single count.
     *
     * @param duration The time the limit will be applied over.
     * @param limit    A number representing the maximum operations that can be performed in the given duration.
     * @return A limit rule.
     */
    public static RequestLimitRule of(Duration duration, long limit) {
        requireNonNull(duration, "duration can not be null");
        int durationSeconds = (int) duration.getSeconds();
        return new RequestLimitRule(durationSeconds, limit, durationSeconds);
    }

    /**
     * Configures as a sliding window rate limit. Imagine the duration window divided into a number of smaller buckets, each with it's own count.
     * The number of smaller buckets is defined by the precision.
     *
     * @param precision Defines the number of buckets that will be used to approximate the sliding window.
     * @return a limit rule
     */
    public RequestLimitRule withPrecision(int precision) {
        return new RequestLimitRule(this.durationSeconds, this.limit, precision, this.name);
    }

    /**
     * Applies a name to the rate limit that is useful for metrics.
     *
     * @param name Defines a descriptive name for the rule limit.
     * @return a limit rule
     */
    public RequestLimitRule withName(String name) {
        return new RequestLimitRule(this.durationSeconds, this.limit, this.precision, name);
    }

    /**
     * @return The limits duration in seconds.
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * @return The limits precision.
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The limit.
     */
    public long getLimit() {
        return limit;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestLimitRule that = (RequestLimitRule) o;
        return durationSeconds == that.durationSeconds
                && limit == that.limit
                && Objects.equals(precision, that.precision)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationSeconds, limit, precision, name);
    }
}
