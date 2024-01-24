package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/gateway/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final String URL = SecurityConstant.URL +"8082/api/v1/customer";
    private final WebClient.Builder webclient;

    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<CustomerDTO> saveCustomer( @RequestBody CustomerDTO customerDTO) {
        System.out.println("Customer Name Gate : "+customerDTO.getName());
        return WebClient.create(URL + "/register").post().body(Mono.just(customerDTO), CustomerDTO.class).retrieve().bodyToMono(CustomerDTO.class);
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody CustomerDTO customerDTO) {
        return WebClient.create(URL).put().body(Mono.just(customerDTO), CustomerDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(CustomerDTO.class).block();
    }


    @GetMapping(path = "search/{customerID}")
    public ResponseEntity searchCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable("customerID") int customerID) {
        try {
            return WebClient.create(URL + "/search/" + customerID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(CustomerDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("Customer Not Exists..!!");
        }
    }

    @GetMapping(path = "search/email", params = "email")
    public ResponseEntity<CustomerDTO> searchEmailCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestParam String email) {
        return WebClient.create(URL + "/search/email?email=" + email).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(CustomerDTO.class).block();

    }

    @DeleteMapping(params = "customerID")
    public ResponseEntity<Void> deleteCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestParam int customerID) {
        return WebClient.create(URL + "/" + customerID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity getAllCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        return WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(CustomerDTO[].class).block();
    }
}
