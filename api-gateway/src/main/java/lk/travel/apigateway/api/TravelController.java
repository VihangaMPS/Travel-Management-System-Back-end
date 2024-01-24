package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.TravelDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/gateway/travel")
@RequiredArgsConstructor
public class TravelController {
    private final String URL = SecurityConstant.URL+"8084/api/v1/travel";
    @PostMapping
    public Mono<TravelDTO> saveTravel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody TravelDTO travelDTO) {
        return WebClient.create(URL).post().body(Mono.just(travelDTO), TravelDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().bodyToMono(TravelDTO.class);
    }

    @PutMapping
    public ResponseEntity<TravelDTO> updateTravel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody TravelDTO travelDTO) {
        return WebClient.create(URL).put().body(Mono.just(travelDTO), TravelDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelDTO.class).block();
    }


    @GetMapping(path = "search/{travelID}")
    public ResponseEntity searchTravel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable("travelID") int travelID) {
        try {
            return WebClient.create(URL + "/search/" + travelID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(TravelDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("Travel Not Exists..!!");
        }
    }



    @DeleteMapping(path = "{travelID}")
    public ResponseEntity<Void> deleteTravel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int travelID) {
        return WebClient.create(URL + "/" + travelID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity<List<TravelDTO>> getAllTravel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        TravelDTO[] body = WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(TravelDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);
    }
}
