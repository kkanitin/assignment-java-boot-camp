package com.example.skooldio.service;

import com.example.skooldio.entity.Basket;
import com.example.skooldio.entity.Product;
import com.example.skooldio.entity.User;
import com.example.skooldio.model.request.*;
import com.example.skooldio.model.response.BasketResponseModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.repository.BasketRepository;
import com.example.skooldio.repository.ProductRepository;
import com.example.skooldio.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class BasketService extends CommonService {

    private final BasketRepository repository;
    private final ProductRepository productRespository;
    private final UserRepository userRepository;

    @Autowired
    public BasketService(BasketRepository repository, ProductRepository productRespository, UserRepository userRepository) {
        this.repository = repository;
        this.productRespository = productRespository;
        this.userRepository = userRepository;
    }

// TODO: 19/2/2565 test all endpoint

    @Transactional
    public Basket create(BasketRequestModel model) {
        checkNotNull(model, "user must not be null.");
        checkNotNull(model.getUserId(), "userid must not be null");
        checkNotNull(model.getProductId(), "productId must not be null");

        Product product = productRespository.findById(model.getProductId()).orElseThrow(
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
        basket.setUserId(user);
        basket.setProductId(product);

        if (model.getQuantity() > product.getQuantity()) {
            throw new IllegalArgumentException(
                    String.format("product id %d is not enough.We have %d you want %d.", product.getId(), product.getQuantity(), model.getQuantity()));
        }
        basket.setQuantity(model.getQuantity());

        return repository.save(basket);
    }

    public BasketResponseModel getByUserid(Long userId, int page, int size, String sortString, String dir) {
        checkNotNull(userId, "userId must not be null.");

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException(String.format("user id %d not found.", userId)));

        BasketResponseModel basketResponseModel = new BasketResponseModel();
        List<Basket> baskets = repository.getByUserId(user, getPageable(page, size, sortString, dir)).orElse(null);

        basketResponseModel.setUser(user);
        if (null != baskets && !baskets.isEmpty()) {
            for (Basket item : baskets) {
                ProductModel model = new ProductModel(item.getProductId().getId(), item.getProductId().getName(),
                        item.getProductId().getPriceBaht(), item.getProductId().getQuantity(), item.getProductId().getDetail());
                model.setQuantity(item.getQuantity());
                basketResponseModel.addProduct(model);
            }
        }

        return basketResponseModel;
    }

    @Transactional
    public Basket updateExceptUserId(Long id, BasketExceptUserIdModel model) throws IllegalAccessException {
        checkNotNull(id, "id must not be null.");
        checkNotNull(model, "BasketExceptUserIdModel must not be null");
        checkNotNull(model.getProductId(), "ProductId must not be null");

        Basket basket = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("basket with id %d does not exists.", id)));
        Product product = productRespository.findById(model.getProductId()).
                orElseThrow(() -> new NoSuchElementException(String.format("product id %d not found.", model.getProductId())));
        basket.setProductId(product);
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

    public List<Basket> getList(int page, int size, String sortString, String dir) {
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
    public BasketResponseModel checkout(Long userId, List<Integer> requestCheckoutProduct, ProductService productService) {
        List<ProductModel> basketProduct = this.getByUserid(userId, 0, -1, "id", "asc").getProducts();
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(String.format("user id %d is not found.", userId)));

        List<ProductModel> deductProducts = new ArrayList<>();

        if (!basketProduct.isEmpty()){
            for (Integer productId : requestCheckoutProduct) {
                deductProducts.add(basketProduct.stream().filter(item -> item.getId() == productId).collect(Collectors.toList()).get(0));
                basketProduct = basketProduct.stream().filter(item -> item.getId() != productId).collect(Collectors.toList());
            }
        }

        //deduct product
        if (!deductProducts.isEmpty()) {
            UpdateProductQuantityListModel updateProductQuantityListModel = new UpdateProductQuantityListModel();
            for (ProductModel item : deductProducts) {
                updateProductQuantityListModel.addProductQuantityModel(new ProductQuantityModel(item.getId(), item.getQuantity()));
            }
            productService.updateQuantityList(updateProductQuantityListModel, "deduct");
        }

        BasketResponseModel remainProduct = new BasketResponseModel();
        remainProduct.setUser(user);
        remainProduct.setProducts(basketProduct);

        return remainProduct;
    }
}
