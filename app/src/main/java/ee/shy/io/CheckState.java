package ee.shy.io;

import java.lang.annotation.*;

/**
 * Annotation for state checking methods.
 * Used for annotating methods which perform state checks before JSON serialization/deserialization via {@link CheckStateTypeAdapterFactory}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckState {

}
