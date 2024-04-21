package br.inatel.dm111mktpromos.persistence.promo;

import java.util.Date;
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
    private Date startingDate;
    private Date expirationDate;
    private List<String> products;


    public Promo(String id, String name, Date startingDate, Date expirationDate, List<String> products) {
        this.id = id;
        this.name = name;
        this.startingDate = startingDate;
        this.expirationDate = expirationDate;
        this.products = products;
    }

    public Promo() {
    }

    public String getName() {
        return name;
    }

    public Date getStartingDate() {
        return startingDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public List<String> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartingDate(Date startingDate) {
        this.startingDate = startingDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
