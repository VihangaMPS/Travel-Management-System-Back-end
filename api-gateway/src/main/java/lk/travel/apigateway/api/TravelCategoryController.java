package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.TravelCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/gateway/travel/category")
@RequiredArgsConstructor
public class TravelCategoryController {
    private final String URL = SecurityConstant.URL+"8084/api/v1/travel/category";
    @PostMapping
    public Mono<TravelCategoryDTO> saveTravelCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody TravelCategoryDTO travelCategory) {
        return WebClient.create(URL).post().body(Mono.just(travelCategory), TravelCategoryDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().bodyToMono(TravelCategoryDTO.class);
    }

    @PutMapping
    public ResponseEntity<TravelCategoryDTO> updateTravelCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody TravelCategoryDTO travelCategory) {
        return WebClient.create(URL).put().body(Mono.just(travelCategory), TravelCategoryDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelCategoryDTO.class).block();
    }


    @GetMapping(path = "search/{travelCategoryID}")
    public ResponseEntity searchTravelCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int travelCategoryID) {
        try {
            return WebClient.create(URL + "/search/" + travelCategoryID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelCategoryDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("TravelCategory Not Exists..!!");
        }
    }



    @DeleteMapping(path = "{travelCategoryID}")
    public ResponseEntity<Void> deleteTravelCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int travelCategoryID) {
        return WebClient.create(URL + "/" + travelCategoryID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity<List<TravelCategoryDTO>> getAllTravelCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        TravelCategoryDTO[] body = WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(TravelCategoryDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);
    }
}
