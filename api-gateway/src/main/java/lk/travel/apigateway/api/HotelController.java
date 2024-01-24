package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.HotelDTO;
import lk.travel.apigateway.dto.TravelDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/gateway/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final WebClient webClient;
    private final String URL = SecurityConstant.URL+"8083/api/v1/hotel";
    @PostMapping
    public Mono<HotelDTO> saveHotel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody HotelDTO hotelDTO) {
        return WebClient.create(URL).post().body(Mono.just(hotelDTO), HotelDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().bodyToMono(HotelDTO.class);
    }

    @PutMapping
    public ResponseEntity<HotelDTO> updateHotel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody HotelDTO hotelDTO) {
        return WebClient.create(URL).put().body(Mono.just(hotelDTO), HotelDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(HotelDTO.class).block();
    }
    @PutMapping(path = "image/{hotelID}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateHotelImage(@RequestHeader(HttpHeaders.AUTHORIZATION) MultiValueMap<String ,String> headers, @RequestPart byte[] image, @PathVariable int hotelID) throws IOException {
        headers.remove("origin");
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();

        multipartBodyBuilder.part("image",image,MediaType.MULTIPART_FORM_DATA);
        return WebClient.create(URL+"/image/"+hotelID).put().body(BodyInserters.fromMultipartData(multipartBodyBuilder.build())).headers(h -> h.addAll(headers)).retrieve().toEntity(Void.class).block();
    }
    @GetMapping(path = "search/location/{location}")
    public ResponseEntity<List<HotelDTO>> searchHotelFindByLocation(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable String location) {
        try {
            HotelDTO[] hotelDTOS = webClient.get().uri(URL + "/search/location/" + location).headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(HotelDTO[].class).block().getBody();
            return ResponseEntity.ok(Arrays.asList(hotelDTOS));

        } catch (Exception e) {
            throw new RuntimeException("Hotel Not Exists..!!");
        }
    }

    @GetMapping(path = "search/{hotelID}")
    public ResponseEntity searchHotel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable("hotelID") int hotelID) {
        try {
            return webClient.get().uri(URL + "/search/" + hotelID).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(HotelDTO[].class).block();

        } catch (Exception e) {
            throw new RuntimeException("Hotel Not Exists..!!");
        }
    }

    @GetMapping(path = "search/email", params = "email")
    public ResponseEntity<HotelDTO> searchEmailHotel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestParam String email) {
      return WebClient.create(URL + "/search/email?email=" + email).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(HotelDTO.class).block();

    }

    @DeleteMapping(params = "hotelID")
    public ResponseEntity<Void> deleteHotel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestParam int hotelID) {
        return WebClient.create(URL + "/" + hotelID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity<List<HotelDTO>> getAllHotel(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        HotelDTO[] hotelDTOS = webClient.get().uri(URL).headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(HotelDTO[].class).block().getBody();
        return ResponseEntity.ok(Arrays.asList(hotelDTOS));
    }
    @GetMapping(path = "!image")
    public ResponseEntity<List<HotelDTO>> getAllHotelWithOutImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        HotelDTO[] hotelDTOS = webClient.get().uri(URL+"/!image").headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(HotelDTO[].class).block().getBody();
        return ResponseEntity.ok(Arrays.asList(hotelDTOS));
    }
}
