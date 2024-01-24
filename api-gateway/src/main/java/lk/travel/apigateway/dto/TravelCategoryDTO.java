package lk.travel.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TravelCategoryDTO {
    private int travelCategoryID;
    private String categoryName;
    @JsonIgnore
    private List<TravelDTO> travelDTOList;
}
