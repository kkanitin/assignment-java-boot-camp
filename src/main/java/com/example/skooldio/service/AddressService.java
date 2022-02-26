package com.example.skooldio.service;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.response.AddressResponseModel;
import com.example.skooldio.repository.AddressRepository;
import com.example.skooldio.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Getter
@Setter
public class AddressService extends CommonService {

    @Autowired
    private AddressRepository repository;
    @Autowired
    private UserRepository userRepository;

    public Address create(Address entity) {
        checkNotNull(entity, "address must not be null.");

        return repository.save(entity);
    }

    public Address getById(Long id) {
        checkNotNull(id, "id must not be null.");
        return repository.findById(id).orElse(null);
    }

    public List<Address> listByUserId(Long userId, int page, int size, String sort, String dir) {
        checkNotNull(userId, "userId must not be null.");

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(String.format("user id %d not found.", userId)));

        return repository.listByUserId(user, getPageable(page, size, sort, dir)).orElse(null);
    }

    public void deleteById(Long id) {
        checkNotNull(id, "id must not be null.");
        repository.deleteById(id);
    }

    @Transactional
    public AddressResponseModel updateExceptUserId(Long id, Address entity) {
        checkNotNull(id, "id must not be null.");

        Address address = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("user with id %d does not exists.", id)));
        AddressResponseModel model = new AddressResponseModel(address.getId(),
                address.getUser().getId(), address.getHouseNo(), address.getBuildingName(),
                address.getFloor(), address.getVillage(), address.getSoi(),
                address.getRoad(), address.getKhet(), address.getKwang(),
                address.getProvince(), address.getPostCode(), address.getPriority());

        repository.updateExceptUserId(id, entity.getHouseNo(),
                entity.getBuildingName(), entity.getFloor(),
                entity.getVillage(), entity.getSoi(), entity.getRoad(), entity.getKhet(),
                entity.getKwang(), entity.getProvince(),
                entity.getPostCode(), entity.getPriority());
        return model;
    }

    public int countAll() {
        return (int) repository.count();
    }

    public List<Address> listPaging(int page, int size, String sort, String dir) {
        return repository.findAll(getPageable(page, size, sort, dir)).getContent();
    }

    public int countByUserId(Long userId) {
        checkNotNull(userId, "userId must not be null.");

        User user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException(String.format("user id %d not found.", userId)));

        return repository.countByUserId(user);
    }
}
