package tipone.buone.api.vehiclemanagement.services.implement;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tipone.buone.api.vehiclemanagement.enums.RoleType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class JwtServiceImplTest {

    @Mock
    private HttpServletRequest httpServletRequest;

    private JwtServiceImpl jwtService;

    private final String SECRET_KEY = "H8n@4Zw$LmC#2yXvP7g!kTeF9aRqUdW3";//just for test, not important

    private String validToken;
    private String invalidToken;
    private final String testUserId = "user";
    private final String testEmail = "test@gmail.com";
    private final Set<String> testRoles = Arrays.stream(RoleType.values())
            .map(Enum::name)
            .collect(Collectors.toSet());

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(SECRET_KEY);

        // Generate valid token to test
        validToken = jwtService.generateToken(testUserId, testEmail, testRoles);
        invalidToken = "invalid.jwt.token";
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        String token = jwtService.generateToken(testUserId, testEmail, testRoles);

        assertNotNull(token, "Token must be not null");
        assertFalse(token.isEmpty(), "Token must be empty");
        assertTrue(token.contains("."), "Token must have JWT format(should contain a dot)");
    }

    @Test
    void getToken_WithValidBearerHeader_ShouldReturnToken() {
        // Mock request with valid Authorization header
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + validToken);

        String extractedToken = jwtService.getToken(httpServletRequest);

        assertEquals(validToken, extractedToken, "The extracted token must match the original token");
    }

    @Test
    void getToken_WithoutBearerPrefix_ShouldReturnNull() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(validToken);

        String extractedToken = jwtService.getToken(httpServletRequest);

        assertNull(extractedToken, "if there is no 'Bearer ' prefix, it should return null");
    }

    @Test
    void getToken_WithNullHeader_ShouldReturnNull() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        String extractedToken = jwtService.getToken(httpServletRequest);

        assertNull(extractedToken, "if Authorization Header is null, it should return null");
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnTrue() {
        boolean isValid = jwtService.validateToken(validToken);

        assertTrue(isValid, "Valid token must return true");
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnFalse() {
        boolean isValid = jwtService.validateToken(invalidToken);

        assertFalse(isValid, "Invalid token must return false");
    }

    @Test
    void validateToken_WithNullToken_ShouldReturnFalse() {
        boolean isValid = jwtService.validateToken(null);

        assertFalse(isValid, "Token null must return false");
    }

    @Test
    void extractId_WithValidToken_ShouldReturnCorrectId() {
        String extractedId = jwtService.extractId(validToken);

        assertEquals(testUserId, extractedId, "The extracted Id must match the original Id");
    }

    @Test
    void extractRoles_WithValidToken_ShouldReturnCorrectRole() {
        Set<String> extractedRoles = jwtService.extractRoles(validToken);

        assertEquals(testRoles, extractedRoles, "The extracted role must match the original role");
    }

    @Test
    void extractClaims_WithValidToken_ShouldReturnClaims() {
        Claims claims = jwtService.extractClaims(validToken);

        assertNotNull(claims, "Claims must not be null");
        assertEquals(testUserId, claims.getSubject(), "Subject must be user ID");
        assertEquals(testRoles, new HashSet<>(claims.get("roles", List.class)), "Role claim must be correct");
        assertNotNull(claims.getIssuedAt(), "IssuedAt must not be null");
        assertNotNull(claims.getExpiration(), "Expiration must not be null");
    }

    @Test
    void extractClaims_WithInvalidToken_ShouldThrowException() {
        assertThrows(JwtException.class, () -> {
            jwtService.extractClaims(invalidToken);
        }, "Invalid Token must throw JwtException");
    }

    @Test
    void generatedToken_ShouldHaveCorrectExpiration() {
        String token = jwtService.generateToken(testUserId, testEmail, testRoles);

        Claims claims = jwtService.extractClaims(token);
        long expiration = claims.getExpiration().getTime();
        long issuedAt = claims.getIssuedAt().getTime();

        long tokenLifetime = expiration - issuedAt;
        assertTrue(tokenLifetime >= 3590000 && tokenLifetime <= 3600000,
                "The token should have a lifespan of approximately 1 hour (3600000 ms)");
    }
}