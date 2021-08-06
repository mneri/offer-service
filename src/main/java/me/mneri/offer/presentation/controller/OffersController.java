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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.mneri.offer.business.bean.Paging;
import me.mneri.offer.business.exception.OfferIsCancelledException;
import me.mneri.offer.business.exception.OfferIsExpiredException;
import me.mneri.offer.business.exception.OfferNotFoundException;
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotAuthorizedException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.business.service.OfferService;
import me.mneri.offer.business.service.UserService;
import me.mneri.offer.data.entity.Offer;
import me.mneri.offer.presentation.api.OffersAPI;
import me.mneri.offer.presentation.dto.OfferCreateDto;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.OfferUpdateDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.ResponseDto;
import me.mneri.offer.presentation.dto.UserDto;
import me.mneri.offer.presentation.mapping.PresentationLayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for paths starting with {@code /offers}.
 *
 * @author Massimo Neri
 */
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
public class OffersController implements OffersAPI {
    private final Clock clock;

    private final OfferService offerService;

    private final PresentationLayerMapper presentationLayerMapper;

    private final UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteOffer(UUID offerId, UUID auth)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotFoundException, UserNotAuthorizedException {
        offerService.delete(offerId, auth);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto<List<OfferDto>> getOffers(PagingDto pagingDto) {
        Paging paging = presentationLayerMapper.mapPagingDtoToPaging(pagingDto);
        return new ResponseDto<>(presentationLayerMapper.mapOfferToOfferDto(offerService.findAllOpen(paging)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto<OfferDto> getOfferById(UUID offerId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException {
        Offer offer = offerService
                .findById(offerId)
                .orElseThrow(() -> new OfferNotFoundException(offerId));

        if (offer.isCancelled()) {
            throw new OfferIsCancelledException(offerId);
        }

        if (offer.isExpired(clock)) {
            throw new OfferIsExpiredException(offerId);
        }

        return new ResponseDto<>(presentationLayerMapper.mapOfferToOfferDto(offer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto<UserDto> getUserByOfferId(UUID offerId)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException {
        return new ResponseDto<>(presentationLayerMapper.mapUserToUserDto(userService.findByOfferId(offerId)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto<OfferDto> postOffer(OfferCreateDto createDto, UUID auth)
            throws UserIsNotEnabledException, UserNotFoundException {
        Offer offer = offerService.save(presentationLayerMapper.mapOfferCreateDtoToOfferCreate(createDto), auth);
        return new ResponseDto<>(presentationLayerMapper.mapOfferToOfferDto(offer));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putOffer(UUID offerId, OfferUpdateDto updateDto, UUID auth)
            throws OfferIsCancelledException, OfferIsExpiredException, OfferNotFoundException,
            UserIsNotEnabledException, UserNotFoundException, UserNotAuthorizedException {
        offerService.update(offerId, presentationLayerMapper.mapOfferUpdateDtoToOfferUpdate(updateDto), auth);
    }
}
