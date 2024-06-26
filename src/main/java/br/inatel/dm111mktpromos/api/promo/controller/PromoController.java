package br.inatel.dm111mktpromos.api.promo.controller;


import br.inatel.dm111mktpromos.api.promo.PromoRequest;
import br.inatel.dm111mktpromos.api.promo.service.PromoService;
import br.inatel.dm111mktpromos.core.ApiException;
import br.inatel.dm111mktpromos.persistence.promo.Promo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/dm111")
public class PromoController {

    private final PromoService service;

    public PromoController(PromoService service) {
        this.service = service;
    }

    // Post operations
    @PostMapping("/promo/{userId}")
    public ResponseEntity<Promo> postPromo(@PathVariable("userId") String userId,@RequestBody PromoRequest request) throws ApiException {
        var product = service.createPromo(userId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Get operations
    @GetMapping("/promo")
    public ResponseEntity<List<Promo>> getAllPromos() throws ApiException {
        var allPromos = service.getAllPromos();
        return ResponseEntity.ok(allPromos);
    }

    @GetMapping("/promo/{id}")
    public Promo getPromoById(@PathVariable("id") String promoId) throws ApiException {
        return service.getPromoById(promoId);
    }

        @GetMapping("/promo/users/{userId}")
    public Map<String, Object> getPromoByUserId(@PathVariable("userId") String userId) throws ApiException {
        return service.getPromoByUser(userId);
    }

    // Update operation
    @PutMapping("/promo/{id}/users/{userId}")
    public ResponseEntity<Promo> updatePromo(@PathVariable("id") String promoId,
                                             @PathVariable("userId") String userId,
                                             @RequestBody PromoRequest request) throws ApiException {
        var promo = service.updatePromo(userId,promoId,request);

        return ResponseEntity.ok(promo);
    }


    // Delete operation
    @DeleteMapping("/promo/{id}/users/{userId}")
    public ResponseEntity<?> deletePromo(@PathVariable("id") String promoId,
                                         @PathVariable("userId") String userId) throws ApiException {

        service.deletePromo(userId,promoId);

        return ResponseEntity.noContent().build();
    }

}
