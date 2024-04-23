package br.inatel.dm111mktpromos.api.promo.service;


import br.inatel.dm111mktpromos.api.promo.PromoRequest;
import br.inatel.dm111mktpromos.consumer.SuperMarketListConsumer;
import br.inatel.dm111mktpromos.core.ApiException;
import br.inatel.dm111mktpromos.core.AppErrorCode;
import br.inatel.dm111mktpromos.persistence.promo.Promo;
import br.inatel.dm111mktpromos.persistence.promo.PromoRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class PromoService {
    private static final Logger log = LoggerFactory.getLogger(PromoService.class);
    private final PromoRepository promoRepository;

    private final SuperMarketListConsumer splConsumer;

    public PromoService(PromoRepository promoRepository, SuperMarketListConsumer splConsumer) {
        this.promoRepository = promoRepository;
        this.splConsumer = splConsumer;
    }


    public Promo createPromo(String userId,PromoRequest promoRequest) throws ApiException{

        // TO DO - Validate user Id
        Promo promo = buildPromoObject(promoRequest);

        var allProductsAvailable = true;

        for (String productId:promo.getProducts()) {
            try{
                allProductsAvailable=true;
                // TO DO - Consumer for products


            }catch (ExecutionException | InterruptedException e){
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }
            
        }

        if(allProductsAvailable){
            promoRepository.save(promo);
        }else{
            throw new ApiException(AppErrorCode.PRODUCT_NOT_FOUND);
        }


        return promo;
    }

    public List<Promo> getAllPromos() throws ApiException {
        try{
            List <Promo> validPromos = new ArrayList<>();
            var allPromos = promoRepository.findAllPromos();

            for (int i = 0; i < allPromos.size(); i++) {
                var startDate = allPromos.get(i).getStartingDate();
                var expirationDate = allPromos.get(i).getExpirationDate();

                if(validatePromoDate(startDate.toString(),
                        expirationDate.toString())){
                    validPromos.add(allPromos.get(i));
                }
            }
            return validPromos;

        }catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }

    public Promo getPromoById(String promoId) throws ApiException{

        try{
            var promo = promoRepository.findAllByPromoId(promoId);

            if(validatePromoDate(promo.getStartingDate().toString(),
                    promo.getExpirationDate().toString()))
                return promo;
            else{
                throw new ApiException(AppErrorCode.PROMO_INVALID_DATE);
            }


        }catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }

    public Promo getPromoByUserId(String userId) throws ApiException{

        try{
            var promo = promoRepository.findPromoByUserId(userId);

            if(validatePromoDate(promo.getStartingDate().toString(),
                    promo.getExpirationDate().toString())) {
                // Lógica interna para cumprir products for you and more

                // Pegar produtos da lista do usuário

                // Pegar produtos em promoção
                var products = promoRepository.findAllPromos();

                // Comparar
                return promo;
            }
            else{
                throw new ApiException(AppErrorCode.PROMO_INVALID_DATE);
            }
        }catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }



    public Promo updatePromo(String userId,String promoId, PromoRequest promoRequest) throws ApiException {
        validateUser(userId);

        var promo = retrievePromo(promoId);

        promo.setName(promoRequest.name());
        promo.setStartingDate(promoRequest.startingDate());
        promo.setExpirationDate(promoRequest.expirationDate());
        promo.setProducts(promoRequest.products());

        if(!promo.getId().equalsIgnoreCase(promoId))
            throw new ApiException(AppErrorCode.PROMO_OPERATION_NOT_ALLOWED);

        var allProductsAvailable = true;

        for (String productId:promo.getProducts()) {
            // TO DO - Logic for consumer product repository
        }

        if(allProductsAvailable){
            promoRepository.update(promo);

            // TO DO - LOGIC FOR PUBLISHER promo
        }else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }
    }


    public void deletePromo(String userId,String promoId) throws ApiException {
        validateUser(userId);
        try{
            var promo = retrievePromo(promoId);

            if(!Objects.equals(promo.getId(), "")){
                promoRepository.delete(promoId);

                // TODO - Publisher
                /*
                var published = publisher.publishDelete(spl);
                if (published) {
                    log.info("The message about SPL {} was successfully published.", spl.getId());
                } else {
                    //TODO: Do the rollback of the changes
                    // splRepository.delete(list.getId());
                }
                 */


            }

        }catch (ExecutionException|InterruptedException e){
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }


    }


    private Promo buildPromoObject(PromoRequest request){
        var id = UUID.randomUUID().toString();

        return new Promo(id, request.name(), request.startingDate(),request.expirationDate(),request.products());

    }

    private Promo retrievePromo(String promoId) throws ApiException{
        try {
            return promoRepository.findAllByPromoId(promoId);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }

    }

    private void validateUser(String userId){
        // TO DO - Consumer logic for user service, deployed into dm111 service

    }


    private boolean validatePromoDate(String startingDate, String expirationDate){
        // Converting
        LocalDate starting = LocalDate.parse(startingDate);
        LocalDate expiration = LocalDate.parse(expirationDate);

        // Now
        LocalDate now = LocalDate.now();

        return now.isAfter(starting) && now.isBefore(expiration);

    }
}
