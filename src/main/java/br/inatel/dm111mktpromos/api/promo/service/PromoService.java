package br.inatel.dm111mktpromos.api.promo.service;


import br.inatel.dm111mktpromos.api.promo.PromoRequest;
import br.inatel.dm111mktpromos.persistence.promo.Promo;
import br.inatel.dm111mktpromos.persistence.promo.PromoRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class PromoService {
    private static final Logger log = LoggerFactory.getLogger(PromoService.class);
    private final PromoRepository promoRepository;

    public PromoService(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }


    public Promo createPromo(String userId,PromoRequest promoRequest){

        // TO DO - Validate user Id

        Promo promo = buildPromoObject(promoRequest);
        promoRepository.save(promo);

        return promo;
    }

    public List<Promo> getAllPromos(){
        return null;
    }

    public Promo getPromoById(String promoId){
        return null;
    }

    public Promo getPromoByUserId(String userId){
        return null;
    }



    public Promo updatePromo(String userId,String promoId){
        return null;
    }


    public void deletePromo(String userId,String promoId){

    }


    private Promo buildPromoObject(PromoRequest request){
        var id = UUID.randomUUID().toString();

        return new Promo(id, request.name(), request.startingDate(),request.expirationDate(),request.products());

    }
}
