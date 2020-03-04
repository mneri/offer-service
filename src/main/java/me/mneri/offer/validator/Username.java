/*
 * Copyright 2020 Massimo Neri <hello@mneri.me>
 *
 * This file is part of mneri/offer-service.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.mneri.offer.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.*;

/**
 * Constraint to be applied to a user's username.
 *
 * @author mneri
 */
@Constraint(validatedBy = {})
@Documented
@NotEmpty
@Pattern(regexp = Constants.USERNAME_REGEXP)
@Retention(RetentionPolicy.RUNTIME)
@Size(min = Constants.USERNAME_MIN_LENGTH, max = Constants.USERNAME_MAX_LENGTH)
@Target(ElementType.FIELD)
public @interface Username {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
