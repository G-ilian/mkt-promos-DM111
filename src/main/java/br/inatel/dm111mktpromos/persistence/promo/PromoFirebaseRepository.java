    package br.inatel.dm111mktpromos.persistence.promo;

    import br.inatel.dm111mktpromos.api.promo.ProductRequest;
    import com.google.cloud.firestore.Firestore;
    import org.springframework.stereotype.Component;

    import java.math.BigDecimal;
    import java.util.*;
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
            Map<String, Object> promoMap = new HashMap<>();
            promoMap.put("id", promo.getId());
            promoMap.put("name", promo.getName());
            promoMap.put("startingDate", promo.getStartingDate());
            promoMap.put("expirationDate", promo.getExpirationDate());

            List<Map<String, Object>> productsMap = new ArrayList<>();
            for (ProductRequest product : promo.getProducts()) {
                Map<String, Object> productMap = new HashMap<>();
                System.out.println(product.product_id());
                productMap.put("product_id", product.product_id());
                productMap.put("discount", product.discount());
                productsMap.add(productMap);
            }
            promoMap.put("products", productsMap);

            firestore.collection(COLLECTION_NAME)
                    .document(promo.getId())
                    .set(promoMap);
        }

        @Override
        public List<Promo> findAllPromos() throws ExecutionException, InterruptedException {
            List<Promo> promoList = new ArrayList<>();

            var collection = firestore.collection(COLLECTION_NAME);

            var documents = collection.listDocuments();

            for (var documentRef : documents) {
                var document = documentRef.get().get();
                if (document.exists()) {
                    Map<String, Object> promoMap = document.getData();
                    String id = document.getId();
                    String name = (String) promoMap.get("name");
                    String startingDate = (String) promoMap.get("startingDate");
                    String expirationDate = (String) promoMap.get("expirationDate");

                    // Convertendo a lista de produtos do mapa para uma lista de ProductRequest
                    List<ProductRequest> products = new ArrayList<>();
                    List<Map<String, Object>> productsMap = (List<Map<String, Object>>) promoMap.get("products");
                    for (Map<String, Object> productMap : productsMap) {
                        String productId = (String) productMap.get("product_id");
                        BigDecimal discount = new BigDecimal(String.valueOf(productMap.get("discount")));
                        products.add(new ProductRequest(productId, discount));
                    }

                    // Criando o objeto Promo e adicionando à lista
                    Promo promo = new Promo(id, name, startingDate, expirationDate, products);
                    promoList.add(promo);
                }
            }

            return promoList;
        }

        @Override
        public Promo findAllByPromoId(String promoId) throws ExecutionException, InterruptedException {
            var documentSnapshot = firestore.collection(COLLECTION_NAME)
                    .document(promoId)
                    .get()
                    .get();

            if (documentSnapshot.exists()) {
                Map<String, Object> promoMap = documentSnapshot.getData();

                // Convertendo a lista de produtos do mapa para uma lista de ProductRequest
                List<ProductRequest> products = new ArrayList<>();
                List<Map<String, Object>> productsMap = (List<Map<String, Object>>) promoMap.get("products");
                for (Map<String, Object> productMap : productsMap) {
                    String productId = (String) productMap.get("product_id");

                    // Convertendo o desconto de String para BigDecimal
                    BigDecimal discount = new BigDecimal(String.valueOf(productMap.get("discount")));

                    products.add(new ProductRequest(productId, discount));
                }

                // Criando um novo objeto Promo com os dados do mapa
                return new Promo(
                        (String) promoMap.get("id"),
                        (String) promoMap.get("name"),
                        (String) promoMap.get("startingDate"),
                        (String) promoMap.get("expirationDate"),
                        products
                );
            } else {
                return null; // Retorna null se a promoção não existir
            }
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
