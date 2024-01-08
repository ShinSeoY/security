package security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationTokenProvider provider;
    private final UserDetailsService userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // 인증된 사용자의 토큰을 탈취해 위조된 요청을 보냈을 경우 파악해 방지하는 것
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session을 사용하지 않음
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").permitAll()
//                .antMatchers("/api/private/**").authenticated()
                .and()
                .addFilterBefore(new AuthenticationTokenFilter(provider, userDetailsService), UsernamePasswordAuthenticationFilter.class);
        // UsernamePasswordAuthenticationFilter에 가기 전에 직접 만든 JwtAuthenticationFilter을 실행하겠다.
    }

    @Bean
    public PasswordEncoder userPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
