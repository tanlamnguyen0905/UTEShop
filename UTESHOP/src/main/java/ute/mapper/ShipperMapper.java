package ute.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ute.dto.ShipperDTO;
import ute.entities.Users;

public class ShipperMapper {
    
    /**
     * Chuyển đổi Entity Users (Shipper) sang ShipperDTO
     */
    public static ShipperDTO toDTO(Users user) {
        if (user == null) {
            return null;
        }
        
        return ShipperDTO.builder()
                .userID(user.getUserID())
                .fullname(user.getFullname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .build();
    }
    
    /**
     * Chuyển đổi danh sách Entity sang DTO
     */
    public static List<ShipperDTO> toDTOList(List<Users> users) {
        if (users == null) {
            return new ArrayList<>();
        }
        return users.stream()
                .map(ShipperMapper::toDTO)
                .collect(Collectors.toList());
    }
}

