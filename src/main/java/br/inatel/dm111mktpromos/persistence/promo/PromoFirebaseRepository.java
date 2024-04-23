package br.inatel.dm111mktpromos.persistence.promo;

import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
    public List<Promo> findAllPromos() throws ExecutionException, InterruptedException {
        List<Promo> promoList = new ArrayList<>();

        var collection = firestore.collection(COLLECTION_NAME);

        var documents = collection.listDocuments();

        for (var documentRef : documents) {

            var document = documentRef.get().get();
            if (document.exists()) {
                var promo = document.toObject(Promo.class);
                promoList.add(promo);
            }
        }

        return promoList;

    }

    @Override
    public Promo findAllByPromoId(String promoId) throws ExecutionException, InterruptedException {
        var promo = firestore.collection(COLLECTION_NAME)
                .document(promoId)
                .get()
                .get()
                .toObject(Promo.class);


        return promo;
    }

    @Override
    public Promo findPromoByUserId(String userId) throws ExecutionException, InterruptedException {
        var promo = firestore.collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .get()
                .toObject(Promo.class);


        return promo;
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
