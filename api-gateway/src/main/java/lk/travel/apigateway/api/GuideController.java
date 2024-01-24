package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.GuideDTO;
import lk.travel.apigateway.dto.GuideDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("api/v1/gateway/guide")
@RequiredArgsConstructor
public class GuideController {

   final private RestTemplate restTemplate;

    private final String URL = SecurityConstant.URL+"8083/api/v1/guide";
    @PostMapping
    public ResponseEntity<GuideDTO> saveGuide(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody GuideDTO guideDTO) {

        return WebClient.create(URL).post().body(Mono.just(guideDTO), GuideDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers))
                .retrieve().toEntity(GuideDTO.class).block();
    }
    @PutMapping
    public ResponseEntity<GuideDTO> updateGuide(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody GuideDTO guideDTO) {
    return WebClient.create(URL).put().body(Mono.just(guideDTO), GuideDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers))
                .retrieve().toEntity(GuideDTO.class).block();

    }


    @GetMapping(path = "search/{guideID}")
    public ResponseEntity<GuideDTO> searchGuide(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@PathVariable("guideID") int guideID) {
        try {

            return WebClient.create(URL+"/search"+guideID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(GuideDTO.class).block();

        }catch (Exception e){
            throw new RuntimeException("Guide Not Exists..!!");
        }
    }
    @GetMapping(path = "search/email", params = "email")
    public ResponseEntity<GuideDTO> searchEmailGuide(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@RequestParam String email) {
        return restTemplate.exchange(URL+"/search/email?email="+email, HttpMethod.GET, new HttpEntity<>(headers), GuideDTO.class);

    }

    @DeleteMapping(params = "{guideID}")
    public ResponseEntity<GuideDTO> deleteGuide(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@PathVariable("guideID") int guideID) {
        return restTemplate.exchange(URL+"/"+guideID, HttpMethod.DELETE, new HttpEntity<>(headers), GuideDTO.class);

    }

    @GetMapping
    public ResponseEntity<List<GuideDTO>> getAllGuide(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        GuideDTO[] body = WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(GuideDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);

    }

}
