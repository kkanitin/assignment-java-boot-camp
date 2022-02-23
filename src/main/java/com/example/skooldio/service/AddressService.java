package com.example.skooldio.service;

import com.example.skooldio.entity.Address;
import com.example.skooldio.entity.User;
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

        return repository.getByUserId(user, getPageable(page, size, sort, dir));
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
