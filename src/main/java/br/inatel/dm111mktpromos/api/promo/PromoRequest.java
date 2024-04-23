package br.inatel.dm111mktpromos.api.promo;

import java.util.Date;
import java.util.List;

/*
{
"name": "Promoção do Dia das Mães",
"starting": "15/04/2024",
"expiration": "15/05/2024",
"products": [
{
"productId": "id do produto",
"discount": 15 // Qual o porcentagem do desconto no valor do
produto. ex: 5, 10, 15, 50, 70... limitado a 99%
},
{
"productId": "id do produto",
"discount": 15 // Qual o porcentagem do desconto no valor do
produto. ex: 5, 10, 15, 50, 70... limitado a 99%
}
]
}
 */
public record PromoRequest(
        String id,
        String name,
        Date startingDate,
        Date expirationDate,
        List<ProductRequest> products
) {
}
