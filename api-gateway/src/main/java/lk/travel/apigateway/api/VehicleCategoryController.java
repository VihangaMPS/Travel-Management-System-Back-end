package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.VehicleCategoryDTO;
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
@RequestMapping("api/v1/gateway/vehicle/category")
@RequiredArgsConstructor
public class VehicleCategoryController {
    private final String URL = SecurityConstant.URL+"8085/api/v1/vehicle/category";
    @PostMapping
    public Mono<VehicleCategoryDTO> saveVehicleCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody VehicleCategoryDTO vehicleCategory) {
        return WebClient.create(URL).post().body(Mono.just(vehicleCategory), VehicleCategoryDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().bodyToMono(VehicleCategoryDTO.class);
    }

    @PutMapping
    public ResponseEntity<VehicleCategoryDTO> updateVehicleCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody VehicleCategoryDTO vehicleCategory) {
        return WebClient.create(URL).put().body(Mono.just(vehicleCategory), VehicleCategoryDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(VehicleCategoryDTO.class).block();
    }


    @GetMapping(path = "search/{vehicleCategoryID}")
    public ResponseEntity searchVehicleCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable("vehicleCategoryID") int vehicleCategoryID) {
        try {
            return WebClient.create(URL + "/search/" + vehicleCategoryID).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(VehicleCategoryDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("VehicleCategory Not Exists..!!");
        }
    }



    @DeleteMapping(path = "{vehicleCategoryID}")
    public ResponseEntity<Void> deleteVehicleCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int vehicleCategoryID) {
        return WebClient.create(URL + "/" + vehicleCategoryID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }

    @GetMapping
    public ResponseEntity<List<VehicleCategoryDTO>> getAllVehicleCategory(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        VehicleCategoryDTO[] body = WebClient.create(URL).get().headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(VehicleCategoryDTO[].class).block().getBody();
        return new ResponseEntity<>(Arrays.asList(body), HttpStatus.OK);
    }
}
