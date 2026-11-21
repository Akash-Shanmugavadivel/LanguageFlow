package com.learning.languageflow.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard RFC 7519 HS256 JWT implementation.
 * Self-contained using standard Java Cryptography and Spring Boot Jackson.
 */
@Service
public class JwtService {

    private final byte[] secretKeyBytes;
    private final long expirationMs;
    private final ObjectMapper objectMapper;

    public JwtService(
            @Value("${jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs,
            ObjectMapper objectMapper) {
        this.secretKeyBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationMs = expirationMs;
        this.objectMapper = objectMapper;
    }

    public String generateToken(String username) {
        return generateToken(username, new HashMap<>());
    }

    public String generateToken(String username, Map<String, Object> extraClaims) {
        try {
            long nowSec = Instant.now().getEpochSecond();
            long expSec = nowSec + (expirationMs / 1000);

            // 1. Header
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("alg", "HS256");
            headerMap.put("typ", "JWT");
            String headerJson = objectMapper.writeValueAsString(headerMap);
            String encodedHeader = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));

            // 2. Payload
            Map<String, Object> payloadMap = new HashMap<>(extraClaims);
            payloadMap.put("sub", username);
            payloadMap.put("iat", nowSec);
            payloadMap.put("exp", expSec);
            String payloadJson = objectMapper.writeValueAsString(payloadMap);
            String encodedPayload = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));

            // 3. Signature
            String contentToSign = encodedHeader + "." + encodedPayload;
            byte[] signatureBytes = signHmacSha256(contentToSign.getBytes(StandardCharsets.UTF_8), secretKeyBytes);
            String encodedSignature = base64UrlEncode(signatureBytes);

            return contentToSign + "." + encodedSignature;
        } catch (Exception ex) {
            throw new RuntimeException("Error generating JWT token", ex);
        }
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String contentToSign = parts[0] + "." + parts[1];
            byte[] expectedSignature = signHmacSha256(contentToSign.getBytes(StandardCharsets.UTF_8), secretKeyBytes);
            byte[] providedSignature = base64UrlDecode(parts[2]);

            // Constant-time comparison to prevent timing attacks
            if (!MessageDigest.isEqual(expectedSignature, providedSignature)) {
                return false;
            }

            // Check expiration
            byte[] payloadBytes = base64UrlDecode(parts[1]);
            JsonNode payloadNode = objectMapper.readTree(payloadBytes);
            if (!payloadNode.has("exp")) {
                return false;
            }

            long expSec = payloadNode.get("exp").asLong();
            long nowSec = Instant.now().getEpochSecond();
            return expSec > nowSec;
        } catch (Exception ex) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            byte[] payloadBytes = base64UrlDecode(parts[1]);
            JsonNode payloadNode = objectMapper.readTree(payloadBytes);
            if (payloadNode.has("sub")) {
                return payloadNode.get("sub").asText();
            }
            return null;
        } catch (Exception ex) {
            return null;
        }
    }

    private byte[] signHmacSha256(byte[] data, byte[] key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKeySpec);
        return mac.doFinal(data);
    }

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private byte[] base64UrlDecode(String str) {
        return Base64.getUrlDecoder().decode(str);
    }
}
