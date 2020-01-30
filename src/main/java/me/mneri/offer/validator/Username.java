package me.mneri.offer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static me.mneri.offer.validator.Constants.*;

/**
 * Constraint to be applied to a user's username.
 *
 * @author mneri
 */
@Constraint(validatedBy = {})
@Documented
@NotEmpty
@Pattern(regexp = USERNAME_REGEXP)
@Retention(RUNTIME)
@Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH)
@Target(FIELD)
public @interface Username {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
