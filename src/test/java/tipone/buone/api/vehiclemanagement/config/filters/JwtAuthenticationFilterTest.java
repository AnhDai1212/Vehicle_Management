package tipone.buone.api.vehiclemanagement.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import tipone.buone.api.vehiclemanagement.enums.RoleType;
import tipone.buone.api.vehiclemanagement.services.JwtService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final String validToken = "valid.jwt.token";
    private final String testEmail = "test@gmail.com";
    private UserDetails mockUserDetails;
    private final Set<String> testRoles = Arrays.stream(RoleType.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    @BeforeEach
    void setUp() {
        // Setup SecurityContextHolder with mock SecurityContext
        SecurityContextHolder.setContext(securityContext);
        List<SimpleGrantedAuthority> authorities = testRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
        mockUserDetails = User.builder()
                .username(testEmail)
                .password("password")
                .authorities(authorities)
                .build();
    }

    @Test
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        when(jwtService.getToken(request)).thenReturn(validToken);
        when(jwtService.validateToken(validToken)).thenReturn(true);
        when(jwtService.extractEmail(validToken)).thenReturn(testEmail);
        when(userDetailsService.loadUserByUsername(testEmail)).thenReturn(mockUserDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        UsernamePasswordAuthenticationToken expectedAuth =
                new UsernamePasswordAuthenticationToken(
                        mockUserDetails,
                        null,
                        mockUserDetails.getAuthorities()
                );

        // Verify that setAuthentication is called with correct authentication object
        verify(securityContext).setAuthentication(expectedAuth);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNullToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        when(jwtService.getToken(request)).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());

        verify(filterChain).doFilter(request, response);

        verify(jwtService, never()).validateToken(any());
        verify(jwtService, never()).extractId(any());
        verify(jwtService, never()).extractRoles(any());
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        String invalidToken = "invalid.token";
        when(jwtService.getToken(request)).thenReturn(invalidToken);
        when(jwtService.validateToken(invalidToken)).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(securityContext, never()).setAuthentication(any());

        verify(filterChain).doFilter(request, response);

        verify(jwtService, never()).extractId(any());
        verify(jwtService, never()).extractRoles(any());
    }

    @Test
    void doFilterInternal_WithEmptyToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        when(jwtService.getToken(request)).thenReturn("");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtService).validateToken("");
        verify(securityContext, never()).setAuthentication(any());
        verify(filterChain).doFilter(request, response);
    }
}