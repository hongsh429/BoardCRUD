package firstweek.board.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import firstweek.board.login.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal 실행");

        // 권한 체크
        // 1. 토큰 확인
        String token = jwtUtil.getTokenFromRequest(request);

        if (StringUtils.hasText(token)) {
            log.info("토큰 확인 성공");
            // 토큰 정보 뽑아오기

            //      1. 토큰 짜르기
            String realToken = jwtUtil.substringToken(token);
            log.info("realToken  {}", realToken);
            //      2. 토큰 검증
            if (!jwtUtil.validateToken(realToken)) {
                log.error("Token error");
                return;
            }
            //      3. 토큰에서 정보 뽑아오기
            Claims info = jwtUtil.getUserInfoFromToken(realToken);

            //      4.info 의 식별자 값으로 context 에 들어가는 Authentication 객체 만들기
            setAuthentication(info, response);


        }
        log.info("authorization filter 에서 do_filter 실행");
        // 다음필터로 넘겨주기
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(Claims info, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication = createAuthentication(info, response);
        context.setAuthentication(authentication);
    }

    private UsernamePasswordAuthenticationToken createAuthentication(Claims info, HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(info.getSubject());

        String userdetail = null;
        try {
            userdetail = new ObjectMapper().writeValueAsString(userDetails);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(userdetail);
        } catch (IOException e) {

            throw new RuntimeException(e);

        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return authentication;
    }
}
