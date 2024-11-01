package Product;

//ProductRepository.java
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
 private List<Product> products = new ArrayList<>();

 public Product findById(Long id) {
     return products.stream().filter(product -> product.getId().equals(id)).findFirst().orElse(null);
 }

 public void save(Product product) {
     products.add(product);
 }

 public boolean delete(Long id) {
     Product product = findById(id);
     if (product != null) {
         products.remove(product);
         return true;
     }
     return false;
 }

 public List<Product> findAll() {
     return new ArrayList<>(products);
 }

 public List<Product> findByCategory(String category) {
     List<Product> categoryProducts = new ArrayList<>();
     for (Product product : products) {
         if (product.getCategory().equals(category)) {
             categoryProducts.add(product);
         }
     }
     return categoryProducts;
 }
}
