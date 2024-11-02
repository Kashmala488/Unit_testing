package ProductTests;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;

import Product.Product;
import Product.ProductRepository;
import Product.ProductService;

import static org.testng.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ProductServiceTest {
    private ProductRepository productRepository;
    private ProductService productService;

    @BeforeMethod
    public void setUp() {
        productRepository = new ProductRepository();
        productService = new ProductService(productRepository);
    }

    @Test(priority = 1)
    public void testAddProduct() {
        Product product = new Product(1L, "ProductA", "Category1");
        boolean added = productService.addProduct(product);
        assertTrue(added, "Product should be added successfully.");
        Product foundProduct = productService.getProductById(1L);
        assertEquals(foundProduct, product, "Product should be found by ID.");
    }

    @Test(priority = 2)
    public void testGetProductById() {
        Product product = new Product(2L, "ProductB", "Category2");
        productService.addProduct(product);
        Product foundProduct = productService.getProductById(2L);
        assertEquals(foundProduct, product, "Product should be found by ID.");
    }

    @Test(priority = 3)
    public void testUpdateProduct() {
        Product product = new Product(3L, "ProductC", "Category1");
        productService.addProduct(product);
        
        Product updatedProduct = new Product(3L, "UpdatedProductC", "UpdatedCategory");
        boolean updated = productService.updateProduct(updatedProduct);
        assertTrue(updated, "Product should be updated successfully.");
        
        Product foundProduct = productService.getProductById(3L);
        assertEquals(foundProduct.getName(), "UpdatedProductC", "Product name should be updated.");
        assertEquals(foundProduct.getCategory(), "UpdatedCategory", "Product category should be updated.");
    }

    @Test(priority = 4)
    public void testDeleteProduct() {
        Product product = new Product(4L, "ProductD", "Category2");
        productService.addProduct(product);
        boolean deleted = productService.deleteProduct(4L);
        assertTrue(deleted, "Product should be deleted successfully.");
        Product foundProduct = productService.getProductById(4L);
        assertNull(foundProduct, "Product should not be found after deletion.");
    }

    @Test(priority = 5)
    public void testListAllProducts() {
        Product product1 = new Product(5L, "ProductE", "Category1");
        Product product2 = new Product(6L, "ProductF", "Category2");
        productService.addProduct(product1);
        productService.addProduct(product2);
        List<Product> allProducts = productService.listAllProducts();
        assertEquals(allProducts.size(), 2, "Should return two products.");
    }

    @Test(priority = 6)
    public void testAddProductsFromExcel() throws Exception {
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
                productService.addProduct(product);
            }

            for (Product product : products) {
                assertEquals(productService.getProductById(product.getId()), product, "Product should be found by ID.");
            }
        } finally {
            if (workbook != null) workbook.close();
            file.close();
        }
    }

    @Test(priority = 7)
    @Parameters({"productId", "productName", "category"})
    public void testAddProductFromXML(@Optional("8") Long productId, @Optional("ProductH") String productName, @Optional("Category3") String category) {
        Product product = new Product(productId, productName, category);
        boolean added = productService.addProduct(product);
        assertTrue(added, "Product should be added successfully from XML parameters.");
        
        Product foundProduct = productService.getProductById(productId);
        assertEquals(foundProduct, product, "Product should be found by ID from XML parameters.");
    }
}
