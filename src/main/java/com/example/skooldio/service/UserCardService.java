package com.example.skooldio.service;

import com.example.skooldio.entity.User;
import com.example.skooldio.entity.UserCard;
import com.example.skooldio.model.request.CardModel;
import com.example.skooldio.model.request.UserCardRequestModel;
import com.example.skooldio.repository.UserCardRepository;
import com.example.skooldio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class UserCardService extends CommonService{

    private UserCardRepository repository;
    private UserRepository userRepository;

    @Autowired
    public UserCardService(UserCardRepository userCardRepository, UserRepository userRepository) {
        this.repository = userCardRepository;
        this.userRepository = userRepository;
    }

    public int countAll() {
        return (int) repository.count();
    }

    public UserCard create(UserCardRequestModel model) {
        checkNotNull(model, "usercard must not be null.");
        checkNotNull(model.getUserId(), "userid must not be null.");
        checkNotNull(model.getCardNo(), "cardno must not be null.");

        UserCard userCard = new UserCard();
        User user = userRepository.findById((long) model.getUserId())
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("user id %d not found.", model.getUserId())
                ));
        userCard.setUserId(user);
        userCard.setCardNo(model.getCardNo());
        userCard.setCardType(model.getCardType());
        userCard.setExpireMonth(model.getExpireMonth());
        userCard.setExpireYear(model.getExpireYear());
        userCard.setCcvOrCvv(model.getCcvOrCvv());

        List<UserCard> userCards = getByUserId((long) model.getUserId(),
                0, 1, "priority", "desc");
        int priority;
        if (userCards.isEmpty()) {
            priority = 1;
        } else {
            if (null == userCards.get(0).getPriority()) {
                priority = 1;
            } else {
                priority = userCards.get(0).getPriority() + 1;
            }
        }
        userCard.setPriority(priority);

        return repository.save(userCard);
    }

    public List<UserCard> getByUserId(Long userId, int page, int size, String sortString, String dir) {
        checkNotNull(userId, "userId must not be nul");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(String.format("user id %d not found.", userId))
        );

        Optional<List<UserCard>> optional = repository.getByUserId(user, getPageable(page, size, sortString, dir));
        return optional.orElse(null);
    }

    @Transactional
    public UserCard updateCard(Long id, CardModel cardModel) {
        checkNotNull(id, "id must not be null.");
        checkNotNull(cardModel, "cardModel must not be null.");
        checkNotNull(cardModel.getCardNo(), "cardno must not be null.");

        UserCard userCard = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("usercard with id %d does not exists.", id)));

        userCard.setCardNo(cardModel.getCardNo());
        userCard.setCardType(cardModel.getCardType());
        userCard.setCcvOrCvv(cardModel.getCcvOrCvv());
        userCard.setExpireMonth(cardModel.getExpireMonth());
        userCard.setExpireYear(cardModel.getExpireYear());

        repository.updateCard(id, cardModel.getCardNo(), cardModel.getCardType(), cardModel.getCcvOrCvv(),
                cardModel.getExpireMonth(), cardModel.getExpireYear());

        return userCard;
    }

    public UserCard getById(Long id) {
        checkNotNull(id, "id must not be null.");

        Optional<UserCard> user = repository.findById(id);
        return user.orElse(null);
    }

    public void deleteById(Long id) throws Exception {
        checkNotNull(id, "id must not be null.");

        repository.deleteById(id);
    }

    public List<UserCard> getList(int page, int size, String sortString, String dir) {
        return repository.findAll(getPageable(page, size, sortString, dir)).getContent();
    }
}
