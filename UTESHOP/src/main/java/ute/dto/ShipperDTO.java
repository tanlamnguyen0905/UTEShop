package ute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipperDTO {
    private Long userID;
    private String fullname;
    private String email;
    private String phone;
    private String avatar;
}

