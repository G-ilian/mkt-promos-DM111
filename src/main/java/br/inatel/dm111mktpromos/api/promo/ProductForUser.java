package br.inatel.dm111mktpromos.api.promo;

import java.math.BigDecimal;

public record ProductForUser(String productId, BigDecimal discount) {
}
