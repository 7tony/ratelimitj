package es.moki.ratelimij.dropwizard.annotation;

import es.moki.ratelimij.dropwizard.filter.KeyPart;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD})
public @interface RateLimited {

    KeyPart[] keys() default {};

    /**
     * An optional group key prefix allows for a grouped rate limit across multiple dropwizard resources.
     * Note the group key name will have no impact if using the deprecated key.
     * @return
     */
    String groupKeyPrefix() default "";

    Rate[] rates();

    /**
     * If true the rate limiter won't enforce over limit. ReportOnly can be used to evaluate rate limits in production environments.
     * @return should not enforce limit.
     */
    boolean reportOnly() default false;

}