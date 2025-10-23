package ute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ute.entities.Addresses;
import ute.entities.Users;

/**
 * DTO cho Address - chỉ chứa dữ liệu cần thiết để trả về client
 * Không expose Entity trực tiếp để bảo mật
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long addressID;
    private String name;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String addressDetail;
    private Boolean isDefault;
    
    // Không trả về toàn bộ User entity, chỉ trả về userID
    private Long userID;
    
    /**
     * Convert từ Entity sang DTO
     */
    public static AddressDTO fromEntity(Addresses entity) {
        if (entity == null) {
            return null;
        }
        
        return AddressDTO.builder()
                .addressID(entity.getAddressID())
                .name(entity.getName())
                .phone(entity.getPhone())
                .province(entity.getProvince())
                .district(entity.getDistrict())
                .ward(entity.getWard())
                .addressDetail(entity.getAddressDetail())
                .isDefault(entity.getIsDefault())
                .userID(entity.getUser() != null ? entity.getUser().getUserID() : null)
                .build();
    }
    
    /**
     * Convert từ DTO sang Entity (cần truyền User)
     */
    public Addresses toEntity(Users user) {
        return Addresses.builder()
                .addressID(this.addressID)
                .name(this.name)
                .phone(this.phone)
                .province(this.province)
                .district(this.district)
                .ward(this.ward)
                .addressDetail(this.addressDetail)
                .isDefault(this.isDefault)
                .user(user)
                .build();
    }
    
    /**
     * Update entity từ DTO (để update existing entity)
     */
    public void updateEntity(Addresses entity) {
        if (entity == null) {
            return;
        }
        
        entity.setName(this.name);
        entity.setPhone(this.phone);
        entity.setProvince(this.province);
        entity.setDistrict(this.district);
        entity.setWard(this.ward);
        entity.setAddressDetail(this.addressDetail);
        entity.setIsDefault(this.isDefault);
        // User không update
    }
}

