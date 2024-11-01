
package Product;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public boolean addProduct(Product product) {
        if (product == null) {
            return false;
        }
        productRepository.save(product);
        return true;
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id);
    }

    public boolean updateProduct(Product product) {
        Product existingProduct = getProductById(product.getId());
        if (existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setCategory(product.getCategory());
            return true;
        }
        return false;
    }

    public boolean deleteProduct(Long id) {
        return productRepository.delete(id);
    }

    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }
}
