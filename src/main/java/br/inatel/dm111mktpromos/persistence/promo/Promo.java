package br.inatel.dm111mktpromos.persistence.promo;

import br.inatel.dm111mktpromos.api.promo.ProductRequest;

import java.util.List;
/*
{
        "name": "nome_da_promo",
        "starting": "data_inicio",
        "expiration": "data_final",
        "products": []
}
 */
public class Promo {

    private String id;
    private String name;
    private String startingDate;
    private String expirationDate;
    private List<ProductRequest> products;


    public Promo(String id, String name, String startingDate, String expirationDate, List<ProductRequest> products) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.expirationDate = expirationDate;
        this.products = products;
    }

    public String getName() {
        return name;
    }



    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
