package br.inatel.dm111mktpromos.api.supermaketlist;

import java.util.List;

//{
//    "name": "Materiais e limpeza",
//    "products": []
//}
public record SuperMarketListRequest(String name, List<String> products) {
}
