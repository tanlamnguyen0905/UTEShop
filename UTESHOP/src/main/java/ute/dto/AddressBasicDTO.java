package ute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressBasicDTO {
    private Long addressID;
    private String name;
    private String phone;
    private String addressDetail;
    private String ward;
    private String district;
    private String city;
    
    public String getFullAddress() {
        return String.format("%s, %s, %s, %s", addressDetail, ward, district, city);
    }
}

