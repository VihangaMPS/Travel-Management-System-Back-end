package lk.travel.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private int userID;
    private String name;
    private String email;
    private String pwd;
    private RoleDTO role;
}
