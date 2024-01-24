package lk.travel.apigateway.api;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.VehicleBrandDTO;
import lk.travel.apigateway.dto.VehicleCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/gateway/vehicle/brand")
@RequiredArgsConstructor
public class VehicleBrandController {
    private final WebClient webClient;
    private final String URL = SecurityConstant.URL+"8085/api/v1/vehicle/brand";
    @PostMapping
    public Mono<VehicleBrandDTO> saveVehicleBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody VehicleBrandDTO vehicleBrandDTO) {
        return WebClient.create(URL).post().body(Mono.just(vehicleBrandDTO), VehicleBrandDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().bodyToMono(VehicleBrandDTO.class);
    }
   /* @PostMapping(path = "image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehicleBrandDTO>  saveWithImageVehicleBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@RequestPart int vehicleID, @RequestPart String brandName, @RequestPart int seat, @RequestPart double fee1Day, @RequestPart double fee1KM, @RequestPart double fuel1KM, @RequestPart byte[] image, @RequestPart int vehicleCategoryID){
     return WebClient.create(URL).post().body(Mono.just(new VehicleBrandDTO(vehicleID,brandName,seat,fee1Day, fee1KM,fuel1KM,image,new VehicleCategoryDTO(vehicleCategoryID))), VehicleBrandDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(VehicleBrandDTO.class).block();
    }*/


    @PutMapping
    public ResponseEntity<VehicleBrandDTO> updateVehicleBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @RequestBody VehicleBrandDTO vehicleBrandDTO) {
        return WebClient.create(URL).put().body(Mono.just(vehicleBrandDTO), VehicleBrandDTO.class).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(VehicleBrandDTO.class).block();
    }
    @PutMapping(path = "image/{vehicleID}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateVehicleBrandImage(@RequestHeader(HttpHeaders.AUTHORIZATION) MultiValueMap<String ,String> headers, @RequestPart byte[] file, @PathVariable int vehicleID) throws IOException {
                headers.remove("origin");
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file",file,MediaType.MULTIPART_FORM_DATA);
        return WebClient.create(URL+"/image/"+vehicleID).put().body(BodyInserters.fromMultipartData(multipartBodyBuilder.build())).headers(h -> h.addAll(headers)).retrieve().toEntity(Void.class).block();
    }

    @GetMapping(path = "search/{vehicleID}")
    public ResponseEntity searchVehicleBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable("vehicleID") int vehicleID) {
        try {
            return webClient.get().uri(URL + "/search/" + vehicleID).headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(VehicleBrandDTO.class).block();

        } catch (Exception e) {
            throw new RuntimeException("VehicleBrand Not Exists..!!");
        }
    }



    @DeleteMapping(path = "{vehicleID}")
    public ResponseEntity<Void> deleteVehicleBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers, @PathVariable int vehicleID) {
        return WebClient.create(URL + "/" + vehicleID).delete().headers(h -> h.set(HttpHeaders.AUTHORIZATION,headers)).retrieve().toEntity(Void.class).block();

    }
    @GetMapping
    public ResponseEntity getAllVehicleBrand(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        VehicleBrandDTO[] body = webClient.get().uri(URL).headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(VehicleBrandDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);
    }
    @GetMapping(path = "!image")
    public ResponseEntity<List<VehicleCategoryDTO>> getAllVehicleBrandWithOutImage(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers) {
        VehicleBrandDTO[] body = webClient.get().uri(URL + "/!image").headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(VehicleBrandDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);
    }
    @GetMapping(path = "search/category/{vehicleCategoryID}")
    public ResponseEntity findAllVehicleCategoryID(@RequestHeader(HttpHeaders.AUTHORIZATION) String headers,@PathVariable int vehicleCategoryID) {
        VehicleBrandDTO[] body = webClient.get().uri(URL+"/search/category/"+vehicleCategoryID).headers(h -> h.set(HttpHeaders.AUTHORIZATION, headers)).retrieve().toEntity(VehicleBrandDTO[].class).block().getBody();
        return new ResponseEntity(Arrays.asList(body), HttpStatus.OK);
    }
}
