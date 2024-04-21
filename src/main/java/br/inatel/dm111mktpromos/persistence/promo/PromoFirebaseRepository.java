package br.inatel.dm111mktpromos.persistence.promo;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class PromoFirebaseRepository implements PromoRepository{

    private static final String COLLECTION_NAME = "promotions";
    private final Firestore firestore;


    public PromoFirebaseRepository(Firestore firestore) {
        this.firestore = firestore;
    }


    @Override
    public void save(Promo promo) {
        firestore.collection(COLLECTION_NAME).
                document(promo.getId()).create(promo);
    }

    @Override
    public Optional<Promo> findAllByPromoId(String promoId) throws ExecutionException, InterruptedException {
        var spl = firestore.collection(COLLECTION_NAME)
                .document(promoId)
                .get()
                .get()
                .toObject(Promo.class);


        return Optional.empty();
    }

    @Override
    public Optional<Promo> findPromoByUserId(String userId) throws ExecutionException, InterruptedException {

        return Optional.empty();
    }

    @Override
    public void delete(String promoId) throws ExecutionException, InterruptedException {
        firestore.collection(COLLECTION_NAME).document(promoId).delete().get();
    }

    @Override
    public void update(Promo promo) {
        save(promo);
    }
}
