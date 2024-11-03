
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;

import Product.Product;
import Product.ProductRepository;

import static org.testng.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryTest {
    private ProductRepository productRepository;

    @BeforeMethod
    public void setUp() {
        productRepository = new ProductRepository();
    }

    @Test(priority = 1)
    public void testSaveAndFindById() {
        Product product = new Product(1L, "ProductA", "Category1");
        productRepository.save(product);
        Product foundProduct = productRepository.findById(1L);
        assertEquals(foundProduct, product, "Product should be found by ID.");
    }

    @Test(priority = 2)
    public void testDelete() {
        Product product = new Product(2L, "ProductB", "Category2");
        productRepository.save(product);

        boolean deleted = productRepository.delete(2L);
        assertTrue(deleted, "Product should be deleted successfully.");
        Product foundProduct = productRepository.findById(2L);
        assertNull(foundProduct, "Product should not be found after deletion.");
    }

    @Test(priority = 3)
    public void testFindAll() {
        Product product1 = new Product(3L, "ProductC", "Category1");
        Product product2 = new Product(4L, "ProductD", "Category2");
        productRepository.save(product1);
        productRepository.save(product2);
        List<Product> allProducts = productRepository.findAll();
        assertEquals(allProducts.size(), 2, "Should return two products.");
    }

    @Test(priority = 4)
    public void testFindByCategory() {
        Product product1 = new Product(5L, "ProductE", "Category1");
        Product product2 = new Product(6L, "ProductF", "Category1");
        Product product3 = new Product(7L, "ProductG", "Category2");
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        
        List<Product> categoryProducts = productRepository.findByCategory("Category1");
        assertEquals(categoryProducts.size(), 2, "Should return two products for Category1.");
        assertTrue(categoryProducts.contains(product1), "ProductE should be in the Category1 products.");
        assertTrue(categoryProducts.contains(product2), "ProductF should be in the Category1 products.");
    }

    @Test(priority = 5)
    public void testSaveProductsFromExcel() throws Exception {
        FileInputStream file = new FileInputStream(new File("src/test/resources/Product/Products.xlsx"));
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<Product> products = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { 
                Row row = sheet.getRow(i);
                Long productId = (long) row.getCell(0).getNumericCellValue();
                String productName = row.getCell(1).getStringCellValue();
                String category = row.getCell(2).getStringCellValue();
                products.add(new Product(productId, productName, category));
            }

            for (Product product : products) {
                productRepository.save(product);
            }


            for (Product product : products) {
                assertEquals(productRepository.findById(product.getId()), product, "Product should be found by ID.");
            }
        } finally {
            if (workbook != null) workbook.close();
            file.close();
        }
    }

    @Test(priority = 6)
    @Parameters({"productId", "productName", "category"})
    public void testSaveProductFromXML(@Optional("8") Long productId, @Optional("ProductH") String productName, @Optional("Category3") String category) {
        Product product = new Product(productId, productName, category);
        productRepository.save(product);
        Product foundProduct = productRepository.findById(productId);
        assertEquals(foundProduct, product, "Product should be found by ID from XML parameters.");
    }
}
