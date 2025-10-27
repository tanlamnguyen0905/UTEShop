package ute.service.inter;

import java.util.List;
import ute.entities.Addresses;

public interface AddressService {
    void addAddress(Addresses address);
    void updateAddress(Addresses address);
    void deleteAddress(Long addressID);
    Addresses getAddressById(Long addressID);
    List<Addresses> getAddressesByUserId(Long userID);
    int countAddressesByUserId(Long userID);
}

