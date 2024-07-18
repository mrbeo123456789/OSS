package com.oss.service;

import com.oss.model.ShippingAddress;
import com.oss.repository.ShippingAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingAddressService {
@Autowired
ShippingAddressRepository shippingAddressRepository;
    public ShippingAddress save(ShippingAddress sa) {
        return shippingAddressRepository.save(sa);
    }
}
