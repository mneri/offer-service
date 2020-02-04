package me.mneri.offer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Constraint to be applied to descriptions.
 *
 * @author mneri
 */
@Constraint(validatedBy = {})
@Documented
@NotEmpty
@Retention(RetentionPolicy.RUNTIME)
@Size(min = Constants.DESCRIPTION_MIN_LENGTH, max = Constants.DESCRIPTION_MAX_LENGTH)
@Target(ElementType.FIELD)
public @interface Description {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
