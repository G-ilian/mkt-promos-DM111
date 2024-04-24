package br.inatel.dm111mktpromos.api.promo.service;


import br.inatel.dm111mktpromos.api.promo.ProductForUser;
import br.inatel.dm111mktpromos.api.promo.ProductRequest;
import br.inatel.dm111mktpromos.api.promo.PromoRequest;
import br.inatel.dm111mktpromos.consumer.SuperMarketListConsumer;
import br.inatel.dm111mktpromos.core.ApiException;
import br.inatel.dm111mktpromos.core.AppErrorCode;
import br.inatel.dm111mktpromos.persistence.product.Product;
import br.inatel.dm111mktpromos.persistence.product.ProductRepository;
import br.inatel.dm111mktpromos.persistence.promo.Promo;
import br.inatel.dm111mktpromos.persistence.promo.PromoRepository;
import br.inatel.dm111mktpromos.persistence.supermarketlist.SuperMarketList;
import br.inatel.dm111mktpromos.persistence.supermarketlist.SuperMarketListRepository;
import br.inatel.dm111mktpromos.persistence.user.User;
import br.inatel.dm111mktpromos.persistence.user.UserFirebaseRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PromoService {
    private static final Logger log = LoggerFactory.getLogger(PromoService.class);
    private final PromoRepository promoRepository;
    private final ProductRepository productRepository;

    private final SuperMarketListConsumer splConsumer;

    private final UserFirebaseRepository userFirebaseRepository;

    private final SuperMarketListRepository splRepository;

    public PromoService(PromoRepository promoRepository, ProductRepository productRepository, SuperMarketListConsumer splConsumer, UserFirebaseRepository userFirebaseRepository, SuperMarketListRepository splRepository) {
        this.promoRepository = promoRepository;
        this.productRepository = productRepository;
        this.splConsumer = splConsumer;
        this.userFirebaseRepository = userFirebaseRepository;
        this.splRepository = splRepository;
    }


    public Promo createPromo(String userId,PromoRequest promoRequest) throws ApiException{
        validateAdminUser(userId);

        var promo = buildPromoObject(promoRequest);

        var allProductsAvailable = true;


        for (ProductRequest product:promo.getProducts()) {
            try {
                if (productRepository.findById(product.product_id()).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
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

                if(validatePromoDate(startDate,
                        expirationDate)){
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

            if(validatePromoDate(promo.getStartingDate(),
                    promo.getExpirationDate()))
                return promo;
            else{
                throw new ApiException(AppErrorCode.PROMO_INVALID_DATE);
            }


        }catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }

    public Map<String, Object> getPromoByUser(String userId) throws ApiException{
        var cont =0;
        try{
            var promoProducts = promoRepository.findAllPromos();
            var promoIds = new ArrayList<String>();
            for (Promo promo: promoProducts) {
                if(validatePromoDate(promo.getStartingDate(),
                        promo.getExpirationDate())) {
                    System.out.println(promo);
                    Map<String, Object> promoData = new HashMap<>();
                    promoData.put("id", promo.getId());
                    promoData.put("name", promo.getName());
                    promoData.put("starting", promo.getStartingDate());
                    promoData.put("expiration", promo.getExpirationDate());

                    var splProducts = splRepository.findAllByUserId(userId);


                    var promosForYou = selectedPromos(promo.getProducts(),splProducts);

                    if(promosForYou.isEmpty())
                        promoData.put("productsForYou","Nothing for today!");
                    promoData.put("productsForYou", promosForYou);



                    promoData.put("products", promo.getProducts());
                    promoIds.add(promo.getId());
                    return promoData;

                }
            }
            return null;
        }catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }
    }



    public Promo updatePromo(String userId,String promoId, PromoRequest promoRequest) throws ApiException {
        validateAdminUser(userId);

        var promo = retrievePromo(promoId);

        promo.setName(promoRequest.name());
        promo.setStartingDate(promoRequest.startingDate());
        promo.setExpirationDate(promoRequest.expirationDate());
        promo.setProducts(promoRequest.products());

        if(!promo.getId().equalsIgnoreCase(promoId))
            throw new ApiException(AppErrorCode.PROMO_OPERATION_NOT_ALLOWED);

        var allProductsAvailable = true;

        for (ProductRequest product:promo.getProducts()) {
            try {
                if (productRepository.findById(product.product_id()).isEmpty()) {
                    allProductsAvailable = false;
                    break;
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new ApiException(AppErrorCode.PRODUCTS_QUERY_ERROR);
            }

        }

        if(allProductsAvailable){
            promoRepository.update(promo);
            return promo;
        }else {
            throw new ApiException(AppErrorCode.PRODUCTS_NOT_FOUND);
        }
    }


    public void deletePromo(String userId,String promoId) throws ApiException {
        validateAdminUser(userId);
        try{
            var promo = retrievePromo(promoId);

            if(!Objects.equals(promo.getId(), "")){
                promoRepository.delete(promoId);
            }

        }catch (ExecutionException|InterruptedException e){
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }


    }


    @org.jetbrains.annotations.NotNull
    private Promo buildPromoObject(PromoRequest request){
        var id = UUID.randomUUID().toString();

        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startingDate = LocalDate.parse(request.startingDate());
        LocalDate expirationDate = LocalDate.parse(request.expirationDate());

        String starting = startingDate.format(dateFormatter);
        String expiration = expirationDate.format(dateFormatter);

        return new Promo(id, request.name(), starting,expiration,request.products());
    }

    private Promo retrievePromo(String promoId) throws ApiException{
        try {
            return promoRepository.findAllByPromoId(promoId);
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.PROMO_QUERY_ERROR);
        }

    }

    private void validateAdminUser(String userId) throws ApiException {
        try {
            User user = userFirebaseRepository.findById(userId)
                    .orElseThrow(() -> new ApiException(AppErrorCode.USER_NOT_FOUND));

            if(!(user.getRole().equalsIgnoreCase("ADMIN"))){
                throw new ApiException(AppErrorCode.USER_UNAUTHORIZED_ACCESS);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }


    private boolean validatePromoDate(String startingDate, String expirationDate){
        LocalDate starting = LocalDate.parse(startingDate);
        LocalDate expiration = LocalDate.parse(expirationDate);

        LocalDate now = LocalDate.now();

        return now.isAfter(starting) && now.isBefore(expiration);

    }

    private List<ProductForUser> selectedPromos(List<ProductRequest> promoProducts, List<SuperMarketList> splProducts){

        List<ProductForUser> selectedProducts = new ArrayList<>();

        List<String> promoProductsIds = promoProducts.
                stream()
                .map(ProductRequest::product_id)
                .toList();

        List<String> splProductsIds = splProducts.stream()
                .flatMap(spl -> spl.getProducts().stream())
                .toList();

        List<String> selectedPromoIds = promoProductsIds.stream()
                .filter(splProductsIds::contains)
                .toList();

        for (String productId : selectedPromoIds){
            BigDecimal discount = promoProducts.stream()
                    .filter(p -> p.product_id().equals(productId))
                    .findFirst()
                    .map(ProductRequest::discount)
                    .orElse(BigDecimal.ZERO);
            ProductForUser product = new ProductForUser(productId, discount);
            selectedProducts.add(product);
        }

        return selectedProducts;


    }
}
