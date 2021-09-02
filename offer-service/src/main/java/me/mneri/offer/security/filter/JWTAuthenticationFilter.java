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

package me.mneri.offer.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for JWT authentication.
 *
 * @author Massimo Neri
 */
@Component
@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Value("${security.jwt.secret}")
    private String secret;

    private final UserDetailsService userDetailsService;

    /**
     * Authenticate the user.
     *
     * @param securityContext The security context.
     * @param request         The request object.
     * @param username        The username.
     */
    private void authenticate(SecurityContext securityContext, HttpServletRequest request, String username) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authToken;
            authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authToken);
        } catch (UsernameNotFoundException e) {
            log.info("Attempt to login failed. username={}", username);
        }
    }

    /**
     * Retrieve the authorization token from the request headers.
     *
     * @param request The request.
     * @return The token if present, {@code null} otherwise.
     */
    private String getAuthorizationToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            return header.substring("Bearer ".length());
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain)
            throws ServletException, IOException {
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if (securityContext.getAuthentication() == null) {
            String token = getAuthorizationToken(request);

            if (token != null) {
                try {
                    DecodedJWT jwt = verifyToken(token);
                    String username = jwt.getSubject();

                    if (username != null) {
                        authenticate(securityContext, request, username);
                    }
                } catch (JWTVerificationException e) {
                    log.info("Failed to verify JWT. token={}", token);
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Verify and decrypt an authentication token.
     *
     * @param token The authentication token.
     * @return The decrypted JWT.
     */
    private DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
}
