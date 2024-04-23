package br.inatel.dm111mktpromos.core.interceptor;

public record JwtTokenPayload(String issuer,
                              String subject,
                              String role,
                              String method,
                              String uri) {
}
