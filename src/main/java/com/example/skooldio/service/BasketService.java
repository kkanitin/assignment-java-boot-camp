package com.example.skooldio.service;

import com.example.skooldio.constant.TransactionStatus;
import com.example.skooldio.entity.*;
import com.example.skooldio.model.request.*;
import com.example.skooldio.model.response.BasketResponseModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.repository.BasketRepository;
import com.example.skooldio.repository.ProductRepository;
import com.example.skooldio.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Getter
@Setter
public class BasketService extends CommonService {

    @Autowired
    private BasketRepository repository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressService addressService;
    @Autowired
    private UserCardService userCardService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ProductService productService;

    @Transactional
    public Basket create(BasketRequestModel model) {
        checkNotNull(model, "user must not be null.");
        checkNotNull(model.getUserId(), "userid must not be null");
        checkNotNull(model.getProductId(), "productId must not be null");

        Product product = productRepository.findById(model.getProductId()).orElseThrow(
                () -> new NoSuchElementException(String.format("product id %d is not found.", model.getProductId())));

        User user = userRepository.findById(model.getUserId()).orElseThrow(
                () -> new NoSuchElementException(String.format("user id %d is not found.", model.getUserId())));

        Optional<Basket> optional = repository.getByUserIdAndProductId(user, product);

        if (optional.isPresent()) {
            Basket basket = optional.get();
            basket.setQuantity(basket.getQuantity() + model.getQuantity());
            if (basket.getQuantity() > product.getQuantity()) {
                throw new IllegalArgumentException(
                        String.format("product id %d is not enough.We have %d you want %d.", product.getId(), product.getQuantity(), basket.getQuantity()));
            }
            repository.updateQuantity(basket.getId(), model.getQuantity());
            return basket;
        }
        Basket basket = new Basket();
        basket.setUser(user);
        basket.setProduct(product);

        if (model.getQuantity() > product.getQuantity()) {
            throw new IllegalArgumentException(
                    String.format("product id %d is not enough.We have %d you want %d.", product.getId(), product.getQuantity(), model.getQuantity()));
        }
        basket.setQuantity(model.getQuantity());

        return repository.save(basket);
    }

    public BasketResponseModel listByUserId(Long userId, int page, int size, String sortString, String dir) {
        checkNotNull(userId, "userId must not be null.");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(String.format("user id %d not found.", userId)));

        BasketResponseModel basketResponseModel = new BasketResponseModel();
        List<Basket> baskets = repository.listByUserId(user, getPageable(page, size, sortString, dir)).orElse(null);

        basketResponseModel.setUser(user);
        if (null != baskets && !baskets.isEmpty()) {
            for (Basket item : baskets) {
                ProductModel model = new ProductModel(item.getProduct().getId(), item.getProduct().getName(),
                        item.getProduct().getPriceBaht(), item.getProduct().getQuantity(), item.getProduct().getDetail());
                model.setQuantity(item.getQuantity());
                basketResponseModel.addProduct(model);
            }
        }

        return basketResponseModel;
    }

    @Transactional
    public Basket updateExceptUserId(Long id, BasketExceptUserIdModel model) {
        checkNotNull(id, "id must not be null.");
        checkNotNull(model, "BasketExceptUserIdModel must not be null");
        checkNotNull(model.getProductId(), "ProductId must not be null");

        Basket basket = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("basket with id %d does not exists.", id)));
        Product product = productRepository.findById(model.getProductId()).
                orElseThrow(() -> new NoSuchElementException(String.format("product id %d not found.", model.getProductId())));
        basket.setProduct(product);
        if (model.getQuantity() > product.getQuantity()) {
            throw new IllegalArgumentException(
                    String.format("product id %d not enough." +
                                    "You want %d we have %d remain.",
                            product.getId(), model.getQuantity(), product.getQuantity()));
        }

        repository.updateExceptUserId(id, product, model.getQuantity());
        return basket;
    }

    public int countAll() {
        return (int) repository.count();
    }

    public List<Basket> listPaging(int page, int size, String sortString, String dir) {
        return repository.findAll(getPageable(page, size, sortString, dir)).getContent();
    }

    public Basket getById(Long id) {
        checkNotNull(id, "id must not be null.");

        Optional<Basket> basket = repository.findById(id);
        return basket.orElse(null);
    }

    public void deleteById(Long id) {
        checkNotNull(id, "id must not be null.");

        repository.deleteById(id);
    }

    @Transactional()
    public BasketResponseModel checkout(Long userId, List<Integer> requestCheckoutProduct) {
        List<ProductModel> basketProduct = this.listByUserId(userId, 0, -1, "id", "asc").getProducts();
        List<ProductModel> tempBasketProduct = new ArrayList<>(basketProduct);
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(String.format("user id %d is not found.", userId)));

        List<ProductModel> deductProducts = new ArrayList<>();

        List<Address> addresss = addressService.listByUserId(userId, 0, 1, "priority", "asc");
        List<UserCard> userCards = userCardService.listByUserId(userId, 0, 1, "priority", "asc");
        Address address = addresss.size() < 1 ? new Address() : addresss.get(0);
        UserCard userCard = userCards.size() < 1 ? new UserCard() : userCards.get(0);

        if (!basketProduct.isEmpty()) {
            for (Integer productId : requestCheckoutProduct) {
                deductProducts.add(basketProduct.stream().filter(item -> item.getId() == productId).collect(Collectors.toList()).get(0));
                basketProduct = basketProduct.stream().filter(item -> item.getId() != productId).collect(Collectors.toList());
            }
        }

        //add to transactions
        List<Transaction> transactions = new ArrayList<>();
        if (!deductProducts.isEmpty()) {
            for (ProductModel item : deductProducts) {
                ProductModel productModel = tempBasketProduct.stream().filter(element -> element.getId() == item.getId()).collect(Collectors.toList()).get(0);
                Product product = new Product(item.getId(), productModel.getName(),
                        productModel.getPriceBaht(), productModel.getQuantity(),
                        productModel.getDetail());
                Double amount = item.getQuantity() * item.getPriceBaht();
                Date date = new Date(System.currentTimeMillis());
                transactions.add(new Transaction(user, userCard, address, product,
                        item.getQuantity(), amount, TransactionStatus.PENDING.name(), date));
            }
        }

        //insert transaction
        transactionService.createAll(transactions);

        BasketResponseModel remainProduct = new BasketResponseModel();
        remainProduct.setUser(user);
        remainProduct.setProducts(basketProduct);

        return remainProduct;
    }
}
