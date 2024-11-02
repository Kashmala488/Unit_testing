package OrderTests;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.*;
import org.testng.annotations.Optional;

import Order.Order;
import Order.OrderRepository;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public class OrderRepositoryTest {
    private OrderRepository orderRepository;

    @BeforeMethod
    public void setUp() {
        orderRepository = new OrderRepository();
    }

    @Test(priority=1)
    public void testFindByIdWithExcelData() throws Exception {

        FileInputStream file = new FileInputStream(new File("src/test/resources/Order/Orders.xlsx"));
        XSSFWorkbook workbook = null;
        List<Order> orders = new ArrayList<>();
        try {
            workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { 
                Row row = sheet.getRow(i);
                Long orderId = (long) row.getCell(0).getNumericCellValue();
                Long userId = (long) row.getCell(1).getNumericCellValue();
                String product = row.getCell(2).getStringCellValue();
                orders.add(new Order(orderId, userId, product));
            }
        } finally {
            if (workbook != null) workbook.close();
            file.close();
        }


        for (Order order : orders) {
            orderRepository.save(order);
        }

        for (Order order : orders) {
            assertEquals(orderRepository.findById(order.getOrderId()), order, "Order should be found by ID.");
        }
    }


    @Test(priority=2)
    @Parameters({"userId"})
    public void testFindByUserId(@Optional("101") Long userId) {
        Order order1 = new Order(1L, userId, "ProductA");
        Order order2 = new Order(2L, 102L, "ProductB");
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> userOrders = orderRepository.findByUserId(userId);
        assertEquals(userOrders.size(), 1, "Should return one order for the specified user ID.");
        assertEquals(userOrders.get(0), order1, "The returned order should match the order created for the user ID.");
    }

}
