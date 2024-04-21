package br.inatel.dm111mktpromos.persistence.promo;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface PromoRepository {
    void save(Promo promo);

    Optional<Promo> findAllByPromoId(String promoId) throws ExecutionException, InterruptedException;


    Optional<Promo> findPromoByUserId(String userId) throws ExecutionException, InterruptedException;

    void delete(String promoId) throws ExecutionException, InterruptedException;

    void update(Promo promo);


}
