package com.epam.incubation.service.reservationbooking.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import com.epam.incubation.service.reservationbooking.util.JwtUtil;

@Service
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	public static final String BEARER = "Bearer ";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader("Authorization");
		Collection<? extends GrantedAuthority> grantedAuthorities = null;
		if (authorization != null && authorization.startsWith(BEARER)) {
			String token = authorization.substring(7);
			grantedAuthorities = jwtUtil.extractAuthorities(token);
		}

		if (grantedAuthorities != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			Map<String, String> authority = (Map<String, String>) ((ArrayList) grantedAuthorities).get(0);

			List<GrantedAuthority> authorities = new ArrayList<>();

			for (Map.Entry<String, String> entry : authority.entrySet()) {
				SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(entry.getValue());
				authorities.add(simpleGrantedAuthority);
			}
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(null, authorization,
					authorities);
			token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(token);
		}

		filterChain.doFilter(request, response);
	}
}
