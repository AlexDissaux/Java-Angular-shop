package shop.alten.shop.Product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.alten.shop.Product.exception.ProductNotFoundException;
import shop.alten.shop.Product.model.Product;
import shop.alten.shop.Product.repository.ProductRepository;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByCode(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Map<String, Object> updates) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "code" -> product.setCode((String) value);
                case "name" -> product.setName((String) value);
                case "description" -> product.setDescription((String) value);
                case "image" -> product.setImage((String) value);
                case "category" -> product.setCategory((String) value);
                case "price" -> product.setPrice(Double.valueOf(value.toString()));
                case "quantity" -> product.setQuantity(Integer.parseInt(value.toString()));
                case "internalReference" -> product.setInternalReference((String) value);
                case "shellId" -> product.setShellId(Long.parseLong(value.toString()));
                case "inventoryStatus" -> product.setInventoryStatus(Product.InventoryStatus.valueOf(value.toString()));
                case "rating" -> product.setRating(Double.valueOf(value.toString()));
            }
        });

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}