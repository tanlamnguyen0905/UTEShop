package ute.dao.inter;

import java.util.List;
import ute.entities.Addresses;

public interface AddressDao {
    void insert(Addresses address);
    void update(Addresses address);
    void delete(Long addressID);
    Addresses findById(Long addressID);
    List<Addresses> findByUserId(Long userID);
    int countByUserId(Long userID);
    void unsetDefaultForUser(Long userID);
}

