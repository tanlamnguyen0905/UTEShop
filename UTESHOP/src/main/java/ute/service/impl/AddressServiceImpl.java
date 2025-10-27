package ute.service.impl;

import java.util.List;

import ute.dao.impl.AddressDaoImpl;
import ute.dao.inter.AddressDao;
import ute.entities.Addresses;
import ute.service.inter.AddressService;

public class AddressServiceImpl implements AddressService {
    
    private final AddressDao addressDao = new AddressDaoImpl();

    @Override
    public void addAddress(Addresses address) {
        // Nếu địa chỉ này được set làm default, bỏ default của các địa chỉ khác
        if (address.getIsDefault() != null && address.getIsDefault()) {
            addressDao.unsetDefaultForUser(address.getUser().getUserID());
        }
        addressDao.insert(address);
    }

    @Override
    public void updateAddress(Addresses address) {
        // Nếu địa chỉ này được set làm default, bỏ default của các địa chỉ khác
        if (address.getIsDefault() != null && address.getIsDefault()) {
            addressDao.unsetDefaultForUser(address.getUser().getUserID());
        }
        addressDao.update(address);
    }

    @Override
    public void deleteAddress(Long addressID) {
        addressDao.delete(addressID);
    }

    @Override
    public Addresses getAddressById(Long addressID) {
        return addressDao.findById(addressID);
    }

    @Override
    public List<Addresses> getAddressesByUserId(Long userID) {
        return addressDao.findByUserId(userID);
    }

    @Override
    public int countAddressesByUserId(Long userID) {
        return addressDao.countByUserId(userID);
    }
}

