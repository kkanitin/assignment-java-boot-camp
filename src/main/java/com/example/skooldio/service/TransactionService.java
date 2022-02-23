package com.example.skooldio.service;

import com.example.skooldio.constant.TransactionStatus;
import com.example.skooldio.constant.UpdateQuantityMode;
import com.example.skooldio.entity.*;
import com.example.skooldio.model.request.ProductQuantityModel;
import com.example.skooldio.model.request.TransactionModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.model.response.TransactionSummaryResponseModel;
import com.example.skooldio.repository.TransactionRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Getter
@Setter
public class TransactionService extends CommonService {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ProductService productService;

    public Transaction create(TransactionModel model) {
        checkNotNull(model, "Transaction must not be null.");
        checkNotNull(model.getUserId(), "user must not be null.");
        checkNotNull(model.getUserCardId(), "usercard must not be null.");
        checkNotNull(model.getAddressId(), "address must not be null.");

        User user = userService.getById((long) model.getUserId());
        UserCard userCard = userCardService.getById((long) model.getUserCardId());
        Address address = addressService.getById((long) model.getAddressId());
        Product product = productService.getById((long) model.getProductId());
        Date date = new Date(System.currentTimeMillis());

        Transaction transaction = new Transaction(user, userCard, address,
                product, model.getQuantity(), model.getAmount(),
                TransactionStatus.PENDING.name(), date);

        return repository.save(transaction);
    }

    public Transaction create(Transaction entity) {
        checkNotNull(entity, "Transaction must not be null.");
        checkNotNull(entity.getUser(), "user must not be null.");
        checkNotNull(entity.getUserCard(), "usercard must not be null.");
        checkNotNull(entity.getAddress(), "address must not be null.");
        checkNotNull(entity.getProduct(), "product must not be null.");

        entity.setGroupNumber(this.getMaxGroupNumber() + 1);
        return repository.save(entity);
    }

    public void createAll(List<Transaction> transactions) {
        checkNotNull(transactions, "Transaction must not be null.");
        Integer groupNumber = this.getMaxGroupNumber() + 1;
        for (Transaction item : transactions) {
            item.setGroupNumber(groupNumber);
        }

        repository.saveAll(transactions);
    }

    public List<Transaction> listByUserId(Long userId, int page, int size, String sort, String dir) {
        checkNotNull(userId, "user must not be null.");

        User user = userService.getById(userId);
        return repository.listByUserId(user, getPageable(page, size, sort, dir)).orElseThrow(() ->
                new NoSuchElementException(String.format("Transaction by user id %d not found.", userId)));
    }

    public Integer getMaxGroupNumber() {
        Integer maxGroupNumber = repository.getMaxGroupNumber();
        if (maxGroupNumber != null) {
            return maxGroupNumber;
        }
        return 0;
    }

    public Transaction getById(Long id) {
        checkNotNull(id, "id must not be null.");
        return repository.findById(id).orElse(null);
    }

    public void deleteById(Long id) throws Exception {
        checkNotNull(id, "id must not be null.");
        repository.deleteById(id);
    }

    public List<Transaction> listPaging(int page, int size, String sort, String dir) {
        return repository.findAll(getPageable(page, size, sort, dir)).getContent();
    }

    public int countAll() {
        return (int) repository.count();
    }

    public int countByUserId(Long userId) {
        User user = userService.getById(userId);
        return Math.toIntExact(repository.countByUserId(user));
    }

    public TransactionModel updateStatus(Long id, String status) {
        checkNotNull(id, "id must not be null.");
        checkNotNull(status, "status must not be null.");

        Date date = new Date(System.currentTimeMillis());
        Transaction transaction = this.getById(id);
        repository.updateStatus(transaction.getId(), status, date);
        return new TransactionModel(id, (int) transaction.getUser().getId(),
                (int) transaction.getUserCard().getId(), (int) transaction.getAddress().getId(),
                (int) transaction.getProduct().getId(), transaction.getQuantity(),
                transaction.getAmount(), transaction.getCreatedDate() == null ? null : transaction.getCreatedDate().toString(),
                transaction.getUpdatedDate() == null ? null : transaction.getUpdatedDate().toString(), status.trim());
    }

    public List<TransactionModel> updateStatusList(List<Long> ids, String status) {
        checkNotNull(ids, "list id must not be null.");
        checkNotNull(status, "status must not be null.");

        List<TransactionModel> transactionModels = new ArrayList<>();
        List<Transaction> transactions = new ArrayList<>();
        for (Long id : ids) {
            Transaction transaction = this.getById(id);
            transactions.add(transaction);
            transactionModels.add(new TransactionModel(transaction.getId(),
                    (int) transaction.getId(), (int) transaction.getUserCard().getId(),
                    (int) transaction.getAddress().getId(), transaction.getQuantity(),
                    (int) transaction.getProduct().getId(), transaction.getAmount(), status));
        }
        Date date = new Date(System.currentTimeMillis());
        for (Transaction item : transactions) {
            repository.updateStatus(item.getId(), status, date);
        }
        //if confirm deduct product
        if (TransactionStatus.CONFIRM.name().equalsIgnoreCase(status)) {
            UpdateProductQuantityListModel updateProductQuantityListModel = new UpdateProductQuantityListModel();
            for (Transaction item : transactions) {
                updateProductQuantityListModel.addProductQuantityModel(new ProductQuantityModel(item.getProduct().getId(), item.getQuantity()));
            }
            productService.updateQuantityList(updateProductQuantityListModel, UpdateQuantityMode.DEDUCT.name());
        }

        return transactionModels;
    }


    public TransactionSummaryResponseModel listByGroupNumber(Integer groupNumber) {
        List<Transaction> transactions = repository.listByGroupNumber(groupNumber, Pageable.unpaged()).orElse(new ArrayList<>());
        TransactionSummaryResponseModel responseModel = new TransactionSummaryResponseModel();
        double summary = 0.0;
        if (!transactions.isEmpty()) {
            User user = transactions.get(0).getUser();
            Address address = transactions.get(0).getAddress();
            UserCard userCard = transactions.get(0).getUserCard();
            responseModel.setUser(user);
            responseModel.setAddress(address);
            responseModel.setUserCard(userCard);
            responseModel.setCreatedDate(transactions.get(0).getCreatedDate());
            responseModel.setUpdatedDate(transactions.get(0).getUpdatedDate());
            for (Transaction item : transactions) {
                responseModel.addProducts(new ProductModel(
                        item.getProduct().getId(), item.getProduct().getName(),
                        item.getProduct().getPriceBaht(), item.getQuantity(),
                        item.getProduct().getDetail()));
                summary += (item.getProduct().getPriceBaht() * item.getQuantity());
            }
            responseModel.setSummary(summary);
        }
        return responseModel;
    }
}
