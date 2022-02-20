package com.example.skooldio.service;

import com.example.skooldio.entity.Product;
import com.example.skooldio.model.request.ProductQuantityModel;
import com.example.skooldio.model.request.UpdateProductQuantityListModel;
import com.example.skooldio.model.response.ProductModel;
import com.example.skooldio.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class ProductService extends CommonService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository productRespository) {
        this.repository = productRespository;
    }

    public Product create(Product entity) {
        checkNotNull(entity, "user must not be null.");
        checkNotNull(entity.getName(), "name must not be null");

        return repository.save(entity);
    }

    public Product update(Long id, Product entity) {
        checkNotNull(id, "id must not be null.");
        checkNotNull(entity.getName(), "name must not be null");

        Product product = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("product with id %d does not exists.", id)));

        repository.update(id, entity.getName(), entity.getDetail(),
                entity.getQuantity(), entity.getPriceBaht());
        return product;
    }

    public Product updateQuantity(Long id, int want, String mode) {
        checkNotNull(id, "id must not be null.");

        Product product = repository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(String.format("product with id %d does not exists.", id)));

        int remain = product.getQuantity();
        if ("deduct".equalsIgnoreCase(mode) && want > remain) {
            throw new IllegalArgumentException(String.format("product %d not enough quantity.You want %d but we have %d",
                    id, want, remain));
        }
        int quantity = "deduct".equalsIgnoreCase(mode) ? remain - want : remain + want;
        repository.updateQuantity(id, quantity);

        product.setQuantity(quantity);
        return product;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ProductModel> updateQuantityList(UpdateProductQuantityListModel model, String mode) {
        List<ProductModel> products = new ArrayList<>();
        try {
            checkNotNull(model, "model must not be null.");
            checkNotNull(model.getProductQuantityModelList(), "list must not be null.");

            boolean isDeduct = "deduct".equalsIgnoreCase(mode);
            for (ProductQuantityModel item : model.getProductQuantityModelList()) {
                int remain = repository.findById(item.getProductId())
                        .orElseThrow(() ->
                                new NoSuchElementException(String.format("product id %d not found.", item.getProductId()))).
                        getQuantity();
                int want = item.getQuantity();

                if (isDeduct) {
                    if (want > remain) {
                        throw new NoSuchElementException(String.format("product %d not enough quantity.You want %d but we have %d",
                                item.getProductId(), want, remain));
                    }
                }

                int quantity = "deduct".equalsIgnoreCase(mode) ? remain - want : remain + want;

                Product product = this.getById(item.getProductId());
                ProductModel productModel = new ProductModel(product.getId(), product.getName(), product.getPriceBaht(), quantity, product.getDetail());
                products.add(productModel);

                repository.updateQuantity(item.getProductId(), quantity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return products;
    }

    public Product getById(Long id) {
        checkNotNull(id, "id must not be null.");

        Optional<Product> product = repository.findById(id);
        return product.orElse(null);
    }

    public void deleteById(Long id) {
        checkNotNull(id, "id must not be null.");

        repository.deleteById(id);
    }

    public int countAll() {
        return (int) repository.count();
    }

    public List<Product> getList(int page, int size, String sortString, String dir) {

        return repository.findAll(getPageable(page, size, sortString, dir)).getContent();
    }
}
