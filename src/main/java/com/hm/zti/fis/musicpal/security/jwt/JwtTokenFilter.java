package com.hm.zti.fis.musicpal.security.jwt;

import com.hm.zti.fis.musicpal.person.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;
    private final PersonService personService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String auth_token = httpServletRequest.getHeader("auth-token");
        System.out.println("Token sent by client: " + auth_token);
        String email = null;
        if (auth_token != null)
            email = jwtTokenUtil.getUserEmail(auth_token);


        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails details = this.personService.loadUserByUsername(email);
            if (jwtTokenUtil.validateToken(auth_token, details)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
