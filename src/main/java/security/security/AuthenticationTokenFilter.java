package security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final AuthenticationTokenProvider authenticationTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String headerValue = request.getHeader("Authorization");
            if (headerValue != null) {
                String jwtString = authenticationTokenProvider.findToken(headerValue);
                if (jwtString != null) {
                    String username = authenticationTokenProvider.getUsername(jwtString);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
