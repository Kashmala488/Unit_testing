package Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
 private List<Order> orders = new ArrayList<>();

 public Order findById(Long id) {
     return orders.stream().filter(order -> order.getOrderId().equals(id)).findFirst().orElse(null);
 }

 public void save(Order order) {
     orders.add(order);
 }

 public boolean delete(Long id) {
     Order order = findById(id);
     if (order != null) {
         orders.remove(order);
         return true;
     }
     return false;
 }

 public List<Order> findAll() {
     return new ArrayList<>(orders);
 }

 public List<Order> findByUserId(Long userId) {
     List<Order> userOrders = new ArrayList<>();
     for (Order order : orders) {
         if (order.getUserId().equals(userId)) {
             userOrders.add(order);
         }
     }
     return userOrders;
 }
}

