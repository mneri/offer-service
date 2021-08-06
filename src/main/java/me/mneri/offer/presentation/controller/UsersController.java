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
import me.mneri.offer.business.exception.UserIsNotEnabledException;
import me.mneri.offer.business.exception.UserNotFoundException;
import me.mneri.offer.business.service.OfferService;
import me.mneri.offer.business.service.UserService;
import me.mneri.offer.data.entity.User;
import me.mneri.offer.presentation.api.UsersAPI;
import me.mneri.offer.presentation.dto.OfferDto;
import me.mneri.offer.presentation.dto.PagingDto;
import me.mneri.offer.presentation.dto.ResponseDto;
import me.mneri.offer.presentation.dto.UserDto;
import me.mneri.offer.presentation.mapping.PresentationLayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for paths starting with {@code /users}.
 *
 * @author Massimo Neri
 */
@Log4j2
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RestController
public class UsersController implements UsersAPI {
    private final OfferService offerService;

    private final PresentationLayerMapper presentationLayerMapper;

    private final UserService userService;

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseDto<List<UserDto>> getUsers(PagingDto pagingDto) {
        Paging paging = presentationLayerMapper.mapPagingDtoToPaging(pagingDto);
        return new ResponseDto<>(presentationLayerMapper.mapUserToUserDto(userService.findAllEnabled(paging)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseDto<UserDto> getUserById(UUID userId) throws UserIsNotEnabledException, UserNotFoundException {
        User user = userService
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException(userId);
        }

        return new ResponseDto<>(presentationLayerMapper.mapUserToUserDto(user));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public ResponseDto<List<OfferDto>> getOffersByPublisherId(UUID userId, PagingDto pagingDto)
            throws UserIsNotEnabledException, UserNotFoundException {
        Paging paging = presentationLayerMapper.mapPagingDtoToPaging(pagingDto);
        List<OfferDto> offers =
                presentationLayerMapper.mapOfferToOfferDto(offerService.findAllOpenByPublisherId(userId, paging));

        return new ResponseDto<>(offers);
    }
}
