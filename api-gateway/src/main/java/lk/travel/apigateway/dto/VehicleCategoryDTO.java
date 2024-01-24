package lk.travel.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class VehicleCategoryDTO  {
   private int vehicleCategoryID;
   private String categoryName;
    @JsonIgnore
   private List<VehicleBrandDTO> vehicleBrandList;

    public VehicleCategoryDTO(int vehicleCategoryID) {
        this.vehicleCategoryID = vehicleCategoryID;
    }
}
