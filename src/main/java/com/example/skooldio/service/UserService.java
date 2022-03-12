package com.example.skooldio.service;

import com.example.skooldio.config.security.AuthUtil;
import com.example.skooldio.config.security.EncryptUtil;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.request.AuthenRequestModel;
import com.example.skooldio.model.response.AuthenResponseModel;
import com.example.skooldio.model.response.UserResponseModel;
import com.example.skooldio.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Getter
@Setter
public class UserService extends CommonService {

    @Autowired
    private UserRepository repository;

    public User create(User entity) {
        checkNotNull(entity, "user must not be null.");
        checkNotNull(entity.getName(), "name must not be null");
        checkNotNull(entity.getUsername(), "username must not be null");

        String hashPassword = EncryptUtil.hashPassword(entity.getPassword().trim(), EncryptUtil.SALT).orElseThrow(
                () -> new IllegalArgumentException("hash password error.")
        );
        entity.setPassword(hashPassword);

        if (null == getByUsername(entity.getUsername())) {
            return repository.save(entity);
        }
        return null;
    }

    public User getByUsername(String username) {
        checkNotNull(username, "username must not be null.");

        Optional<User> user = repository.getByUsername(username);
        return user.orElse(null);
    }

    public User getById(Long id) {
        checkNotNull(id, "id must not be null.");

        Optional<User> user = repository.findById(id);
        return user.orElse(null);
    }

    public void deleteById(Long id) throws Exception {
        checkNotNull(id, "id must not be null.");

        repository.deleteById(id);
    }

    @Transactional
    public User update(Long id, User entity) throws IllegalAccessException {
        checkNotNull(id, "id must not be null.");
        checkNotNull(entity.getName(), "name must not be null");
        checkNotNull(entity.getUsername(), "username must not be null");

        User user = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("user with id %d does not exists.", id)));

        repository.update(id, entity.getUsername(), entity.getName());
        return user;
    }

    @Transactional
    public User updateExceptUsername(Long id, String name) throws IllegalAccessException {
        checkNotNull(id, "id must not be null.");
        checkNotNull(name, "name must not be null");

        User user = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("user with id %d does not exists.", id)));
        user.setName(name);

        repository.updateExceptUsername(id, name);
        return user;
    }

    public int countAll() {
        return (int) repository.count();
    }

    public List<User> listPaging(int page, int size, String sortString, String dir) {

        return repository.findAll(getPageable(page, size, sortString, dir)).getContent();
    }

    public AuthenResponseModel login(AuthenRequestModel model) {
        checkNotNull(model, "model must not be null.");
        checkNotNull(model.getUsername(), "username must not be null.");
        checkNotNull(model.getPassword(), "password must not be null.");

        AuthenResponseModel responseModel = new AuthenResponseModel();

        String hashPassword = EncryptUtil.hashPassword(model.getPassword().trim(), EncryptUtil.SALT).orElseThrow(
                () -> new IllegalArgumentException("hash password error.")
        );

        User user = repository.login(model.getUsername().trim(), hashPassword.trim()).orElseThrow(
                () -> new NoSuchElementException("username or password not correct.")
        );

        responseModel.setJwtToken(AuthUtil.getJWTToken(user.getUsername().trim()));

        return responseModel;
    }

    public UserResponseModel convertEntityToModel(User entity) {
        UserResponseModel model = null;
        if (entity != null) {
            model = new UserResponseModel(entity.getId(), entity.getUsername(), entity.getName());
        }
        return model;
    }
}
