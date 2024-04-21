package br.inatel.dm111mktpromos.api.promo.controller;


import br.inatel.dm111mktpromos.api.promo.PromoRequest;
import br.inatel.dm111mktpromos.api.promo.service.PromoService;
import br.inatel.dm111mktpromos.persistence.promo.Promo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dm111")
public class PromoController {

    private final PromoService service;

    public PromoController(PromoService service) {
        this.service = service;
    }

    // Post operations
    @PostMapping("/promo/{userId}")
    public ResponseEntity<Promo> postPromo(@PathVariable("userId") String userId,@RequestBody PromoRequest request) {
        var product = service.createPromo(userId,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // Get operations
    @GetMapping("/promo")
    public ResponseEntity<List<Promo>> getAllPromos(){
        var allPromos = service.getAllPromos();
        return ResponseEntity.ok(allPromos);
    }

    @GetMapping("/promo/{id}")
    public ResponseEntity<Promo> getPromoById(@PathVariable("id") String promoId){
        var promo = service.getPromoById(promoId);

        return ResponseEntity.ok(promo);
    }

    @GetMapping("/promo/users/{userId}")
    public ResponseEntity<Promo> getPromoByUserId(@PathVariable("userId") String userId){
        var promo = service.getPromoByUserId(userId);

        return ResponseEntity.ok(promo);
    }

    // Update operation
    @PutMapping("/promo/{id}/users/{userId}")
    public ResponseEntity<Promo> updatePromo(@PathVariable("id") String promoId,
                                             @PathVariable("userId") String userId){
        var promo = service.updatePromo(userId,promoId);

        return ResponseEntity.ok(promo);
    }


    // Delete operation
    @DeleteMapping("/promo/{id}/users/{userId}")
    public ResponseEntity<?> deletePromo(@PathVariable("id") String promoId,
                                         @PathVariable("userId") String userId){

        service.deletePromo(userId,promoId);

        return ResponseEntity.noContent().build();
    }

}