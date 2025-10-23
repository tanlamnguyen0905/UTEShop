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
        addressDao.insert(address);
    }

    @Override
    public void updateAddress(Addresses address) {
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

