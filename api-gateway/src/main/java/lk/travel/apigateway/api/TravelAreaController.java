package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.TravelAreaDTO;
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
@RequestMapping("api/v1/gateway/travel/area")
@RequiredArgsConstructor
public class TravelAreaController {
    private final String URL = SecurityConstant.URL+"8084/api/v1/travel/area";
    @PostMapping
    public ResponseEntity<TravelAreaDTO> saveTravelArea(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody TravelAreaDTO travelArea) {
        return WebClient.create(URL).post().body(Mono.just(travelArea), TravelAreaDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelAreaDTO.class).block();
    }

    @PutMapping
    public ResponseEntity<TravelAreaDTO> updateTravelArea(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody TravelAreaDTO travelArea) {
        return WebClient.create(URL).put().body(Mono.just(travelArea), TravelAreaDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelAreaDTO.class).block();
    }


    @GetMapping(path = "search/{travelAreaID}")
    public ResponseEntity searchTravelArea(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int travelAreaID) {
        try {
            return WebClient.create(URL + "/search/" + travelAreaID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelAreaDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("TravelArea Not Exists..!!");
        }
    }



    @DeleteMapping(path= "{travelAreaID}")
    public ResponseEntity<Void> deleteTravelArea(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int travelAreaID) {
        return WebClient.create(URL + "/" + travelAreaID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity<List<TravelAreaDTO>> getAllTravelArea(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        TravelAreaDTO[] body = WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(TravelAreaDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);
    }
}
