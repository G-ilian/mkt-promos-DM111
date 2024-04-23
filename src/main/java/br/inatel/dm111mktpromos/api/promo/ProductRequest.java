package br.inatel.dm111mktpromos.api.promo;

import java.math.BigDecimal;

public record ProductRequest(String product_id, BigDecimal discount) {
}
