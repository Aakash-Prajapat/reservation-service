package com.epam.incubation.service.reservationbooking.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

	private static final String AUTHORIZATION = "Authorization";

	@Override
	public void apply(RequestTemplate requestTemplate) {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder
				.getContext().getAuthentication();

		String token = (String) authentication.getCredentials();

		requestTemplate.header(AUTHORIZATION, token);

	}
}