package me.mneri.offer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Constraints to be applied to titles.
 *
 * @author mneri
 */
@Constraint(validatedBy = {})
@Documented
@NotEmpty
@Retention(RetentionPolicy.RUNTIME)
@Size(min = Constants.TITLE_MIN_LENGTH, max = Constants.TITLE_MAX_LENGTH)
@Target(ElementType.FIELD)
public @interface Title {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
