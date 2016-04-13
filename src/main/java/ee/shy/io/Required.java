package ee.shy.io;

import java.lang.annotation.*;

/**
 * Annotation for required fields.
 * Used for annotating fields strictly required in JSON serialization/deserialization via {@link RequiredTypeAdapterFactory}.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Required {

}
