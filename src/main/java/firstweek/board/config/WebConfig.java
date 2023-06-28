package firstweek.board.config;

import firstweek.board.jwt.JwtAuthenticationFilter;
import firstweek.board.jwt.JwtAuthorizationFilter;
import firstweek.board.jwt.JwtUtil;
import firstweek.board.login.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
@RequiredArgsConstructor
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 애너테이션 활성화
public class WebConfig {

    private final AuthenticationConfiguration configuration;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;


    // authenticationManager 생성
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 커스텀할 필터에 매니저를 세팅
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(configuration));
        return jwtAuthenticationFilter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable());

        httpSecurity.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        httpSecurity.authorizeHttpRequests(req ->

                        req
//                        .requestMatchers(HttpMethod.POST, "/api/boards").authenticated()
//                        .requestMatchers(HttpMethod.PUT, "/api/board/**").hasAnyAuthority("USER") // 인가 권한설정
//                        .requestMatchers(HttpMethod.DELETE, "/api/board/**").hasAnyAuthority()
                                .requestMatchers("/api/user/**").permitAll()
//                        .requestMatchers("/api/**").authenticated()
                                .anyRequest().authenticated() // 로그인 후 사용할 수 있도록
        );

//        httpSecurity.formLogin(form ->
//                form.loginPage("/api/user/login-page")
//        );


        httpSecurity.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
