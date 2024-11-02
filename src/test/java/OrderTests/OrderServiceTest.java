package OrderTests;
import Order.Order;
import Order.OrderService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import static org.testng.Assert.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class OrderServiceTest {
    private OrderService orderService;

    @BeforeMethod
    public void setUp() {
        orderService = new OrderService();
    }
   

    @Test(priority=4)
    @Parameters({"orderId", "userId", "product"})
    public void testCreateOrder(@Optional("1") Long orderId, @Optional("1") Long userId, @Optional("Sample Product") String product) {
        Order order = new Order(orderId, userId, product);
        boolean result = orderService.createOrder(order);
        assertTrue(result);
        assertEquals(orderService.getOrderById(order.getOrderId()), order);
    }

    @Test(priority = 1)
    public void testGetOrderById() throws IOException {
        Order order = null;

        FileInputStream file = new FileInputStream("src/test/resources/Order/Orders.xlsx");
        Workbook workbook = null;

        try {
            workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(1); 
            Long orderId = (long) row.getCell(0).getNumericCellValue();
            Long userId = (long) row.getCell(1).getNumericCellValue();
            String productId = row.getCell(2).getStringCellValue();


            order = new Order(orderId, userId, productId); 

            orderService.createOrder(order); 

            Order fetchedOrder = orderService.getOrderById(order.getOrderId());
            assertNotNull(fetchedOrder, "Fetched order should not be null.");
            assertEquals(fetchedOrder, order, "Fetched order should match the original order.");
        } finally {
            if (workbook != null) {
                workbook.close(); 
            }
            file.close(); 
        }
    }
    @Test(priority=1)
    public void testUpdateOrder() {
        Order order = new Order(1L, 101L, "ProductA");
        orderService.createOrder(order);
        Order updatedOrder = new Order(1L, 101L, "ProductUpdated");
        boolean result = orderService.updateOrder(updatedOrder);
        assertTrue(result);
        assertEquals(orderService.getOrderById(1L).getProduct(), "ProductUpdated");
    }

    @Test(priority=2)
    public void testCancelOrder() {
        Order order = new Order(2L, 102L, "ProductB");
        orderService.createOrder(order);
        boolean result = orderService.cancelOrder(2L);
        assertTrue(result);
        assertNull(orderService.getOrderById(2L));
    }

    @DataProvider(name = "userOrdersData")
    public Object[][] userOrdersData() {
        return new Object[][] {
            {101L, Arrays.asList(new Order(1L, 101L, "ProductA"), new Order(3L, 101L, "ProductC"))},
            {102L, Collections.singletonList(new Order(2L, 102L, "ProductB"))}
        };
    }

    @Test(priority=5,dataProvider = "userOrdersData")
    public void testListOrdersByUser(Long userId, List<Order> expectedOrders) {
        for (Order order : expectedOrders) {
            orderService.createOrder(order);
        }
        List<Order> userOrders = orderService.listOrdersByUser(userId);
        assertEquals(userOrders.size(), expectedOrders.size());
        assertTrue(userOrders.containsAll(expectedOrders));
    }
}
