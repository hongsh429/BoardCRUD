package firstweek.board.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstweek.board.dto.LoginRequestDto;
import firstweek.board.entity.UserRoleEnum;
import firstweek.board.login.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String test = "홍승현";
        log.info("로그인 시도 {}", test);

        try {
            LoginRequestDto dto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            /**
             * AuthenticationManager 받아와서,
             * authenticate() 메소드로 UsernamePasswordAuthenticationToken 세팅하여
             * Authentication 객체 리턴 holder > context > authentication
             * */
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getUsername(),
                            dto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 -> JWT 토큰 생성 -> 쿠키에 담기");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) (authResult.getPrincipal())).getUser().getRole();
        System.out.println("role = " + role);
        // jwt 토큰 만들기
        String token = jwtUtil.createToken(username, role);

        System.out.println("token = " + token);

        // cookie에 저장
        jwtUtil.addJwtToCookie(token, response);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401); // 인증 실패
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
