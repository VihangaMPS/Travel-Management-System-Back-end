package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.BookingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;


@RestController
@RequestMapping("api/v1/gateway/booking")
@RequiredArgsConstructor
public class BookingController {
    private final RestTemplate restTemplate;
    private final WebClient.Builder webclient;
    private final String URL = SecurityConstant.URL+ "8082/api/v1/booking";

    @PostMapping
    public ResponseEntity<BookingDTO> saveBooking(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody BookingDTO bookingDTO) {
                        MultiValueMap<String,String> value=new HttpHeaders();
                        value.set(HttpHeaders.AUTHORIZATION,auth);
        try {
        return restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(bookingDTO,value), BookingDTO.class);

        }catch (Exception e){
            throw new RuntimeException("Booking Already Exists..!");
        }
    }

    @PutMapping
    public ResponseEntity<BookingDTO> updateBooking(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth,@RequestBody BookingDTO bookingDTO) {
        MultiValueMap<String,String> value=new HttpHeaders();
        value.set(HttpHeaders.AUTHORIZATION,auth);
        try {
        return restTemplate.exchange(URL, HttpMethod.PUT, new HttpEntity<>(bookingDTO,value), BookingDTO.class);
        }catch (Exception e){
            throw new RuntimeException("Booking Already Exists..!");

        }
    }
    @GetMapping(path = "search", params = "bookingID")
    public ResponseEntity<BookingDTO> searchBooking(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@RequestParam int bookingID){

        try {
        return new ResponseEntity( restTemplate.exchange(URL + "/search?bookingID=" + bookingID, HttpMethod.GET, new HttpEntity<>(headers), BookingDTO.class),HttpStatus.OK);

        }catch (HttpClientErrorException e){
            throw new RuntimeException("Booking Not Exists...!!");
        }

    }
    @GetMapping(path = "search/customer", params = "customerID")
    public ResponseEntity<BookingDTO> searchBookingCustomerID(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@RequestParam int customerID) {
        try {

        return restTemplate.exchange(URL+"/search/customer?customerID="+customerID, HttpMethod.GET, new HttpEntity<>(headers), BookingDTO.class);
        }catch (HttpClientErrorException e){
            throw new RuntimeException("Customer Not Exists...!!");
        }
    }


    @DeleteMapping(params = "bookingID")
    public ResponseEntity<BookingDTO> deleteBooking(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@RequestParam int bookingID) {
        try {
        return restTemplate.exchange(URL+"?bookingID="+bookingID, HttpMethod.DELETE, new HttpEntity<>(headers), BookingDTO.class);
    }catch (Exception e){
        throw new RuntimeException("Booking Not Exists...!!");
    }
    }

    @GetMapping
    public ResponseEntity getAllBooking(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {

        ResponseEntity<BookingDTO[]> exchange = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<>(headers), BookingDTO[].class);
        return new ResponseEntity<>(Arrays.asList(exchange),HttpStatus.OK);
    }

 /*   private HttpEntity getHttpHeaders(BookingDTO... bookingDTOS) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(httpHeaders.AUTHORIZATION, requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION));
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, requestAttributes.getRequest().getHeader(HttpHeaders.CONTENT_TYPE));

        if(bookingDTOS.length>0){
            return   new HttpEntity<>(bookingDTOS[0],httpHeaders);
        }else{
            return   new HttpEntity<>(httpHeaders);
        }
    }*/
}
