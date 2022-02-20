package com.example.skooldio.service;

import com.example.skooldio.entity.Address;
import com.example.skooldio.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class AddressService extends CommonService {

    private final AddressRepository repository;

    @Autowired
    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public Address create(Address entity) {
        checkNotNull(entity, "address must not be null.");

        return repository.save(entity);
    }

    public Address getById(Long id) {
        checkNotNull(id, "id must not be null.");

        Optional<Address> address = repository.findById(id);
        return address.orElse(null);
    }

    public void deleteById(Long id) throws Exception {
        checkNotNull(id, "id must not be null.");

        repository.deleteById(id);
    }

    @Transactional
    public Address updateExceptUserId(Long id, Address entity) throws IllegalAccessException {
        checkNotNull(id, "id must not be null.");

        Address address = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("user with id %d does not exists.", id)));

        repository.updateExceptUserId(id, entity.getHouseNo(),
                entity.getBuildingName(), entity.getFloor(), entity.getVillage(),
                entity.getSoi(), entity.getRoad(), entity.getKhet(),
                entity.getKwang(), entity.getProvince(), entity.getPostCode(), entity.getPriority());
        return address;
    }

    public int countAll() {
        return (int) repository.count();
    }

    public List<Address> getList(int page, int size, String sortString, String dir) {

        return repository.findAll(getPageable(page, size, sortString, dir)).getContent();
    }
}
