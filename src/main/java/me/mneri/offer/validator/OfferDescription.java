package me.mneri.offer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static me.mneri.offer.validator.Constants.OFFER_DESCRIPTION_MAX_LENGTH;
import static me.mneri.offer.validator.Constants.OFFER_DESCRIPTION_MIN_LENGTH;

/**
 * Constraint to be applied to offer descriptions.
 *
 * @author mneri
 */
@Constraint(validatedBy = {})
@Documented
@NotEmpty
@Retention(RUNTIME)
@Size(min = OFFER_DESCRIPTION_MIN_LENGTH, max = OFFER_DESCRIPTION_MAX_LENGTH)
@Target(FIELD)
public @interface OfferDescription {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
