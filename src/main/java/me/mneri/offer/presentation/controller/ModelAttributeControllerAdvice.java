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

package me.mneri.offer.presentation.controller;

import me.mneri.offer.presentation.api.APIParameters;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.exception.IllegalPageSizeException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * General controller advice for complex attribute construction.
 *
 * @author Massimo Neri
 */
@RestControllerAdvice
public class ModelAttributeControllerAdvice {
    @ModelAttribute
    public PagingDto pagingDto(@RequestParam(value = APIParameters.PARAM_PAGE_NUMBER, required = false) Integer pageNumber,
                               @RequestParam(value = APIParameters.PARAM_PAGE_SIZE, required = false) Integer pageSize)
            throws IllegalPageSizeException {
        if (pageNumber == null) {
            pageNumber = APIParameters.PARAM_PAGE_NUMBER_DEFAULT;
        }

        if (pageSize == null) {
            pageSize = APIParameters.PARAM_PAGE_SIZE_DEFAULT;
        } else if (pageSize < APIParameters.PARAM_PAGE_SIZE_MIN || pageSize > APIParameters.PARAM_PAGE_SIZE_MAX) {
            throw new IllegalPageSizeException(pageSize);
        }

        return new PagingDto(pageNumber, pageSize);
    }
}
