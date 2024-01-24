package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.CustomerDTO;
import lk.travel.apigateway.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/gateway/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final RestTemplate restTemplate;
    private final String URL = SecurityConstant.URL+"8081/api/v1/user";
    @PostMapping(path = "/register")
    public Mono<UserDTO> saveUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody UserDTO userDTO) {
        System.out.println("IS HERE");
        return WebClient.create(URL+"/register").post().body(Mono.just(userDTO), UserDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().bodyToMono(UserDTO.class);
    }

    @PutMapping(path = "update")
    public ResponseEntity<UserDTO> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody UserDTO userDTO) {
        return WebClient.create(URL+"/update").put().body(Mono.just(userDTO), UserDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(UserDTO.class).block();
    }


    @GetMapping(path = "search/{userID}")
    public ResponseEntity searchUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable("userID") int userID) {
        try {
            return WebClient.create(URL + "/search/" + userID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(UserDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("User Not Exists..!!");
        }
    }
    @GetMapping(path = "search/email",params = "email")
    public ResponseEntity searchUserEmail(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestParam String email) {
        MultiValueMap<String,String> value=new HttpHeaders();
        value.set(HttpHeaders.AUTHORIZATION,headers);
        System.out.println(headers);
       return WebClient.create(SecurityConstant.URL + "8081/api/v1/user/search/email?email=" + email).get().headers(h -> h.add(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(UserDTO.class).block();
    }


    @DeleteMapping(path = "{userID}")
    public ResponseEntity<Void> deleteUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int userID) {
        System.out.println(headers);
        return WebClient.create(URL + "/" + userID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        UserDTO[] userDTOS = WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(UserDTO[].class).block().getBody();
        return ResponseEntity.ok(Arrays.asList(userDTOS));

    }
 /*   private MultiValueMap<String,String > getDefaultOrigin(MultiValueMap<String,String> headers){
        headers.forEach((key,value)->{
            if(key.equals("origin")){
                value.clear();
                value.add("http://desktop-m37ask3.lan:8080");
                return;
            }
        });
        return headers;
    }*/
}
