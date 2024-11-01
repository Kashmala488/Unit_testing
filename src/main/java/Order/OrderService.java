package Order;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<Order> orders = new ArrayList<>();

    public boolean createOrder(Order order) {
        if (order == null) {
            return false;
        }
        orders.add(order);
        return true;
    }

    public Order getOrderById(Long id) {
        for (Order order : orders) {
            long id1 = order.getOrderId();
            if (id1 == id) {
                return order;
            }
        }
        return null;
    }

    public boolean updateOrder(Order order) {
        long orderId = order.getOrderId();
        Order existingOrder = getOrderById(orderId);
        if (existingOrder != null) {
            String product = order.getProduct();
            existingOrder.setProduct(product);
            return true;
        }
        return false;
    }

    public boolean cancelOrder(Long id) {
        Order order = getOrderById(id);
        if (order != null) {
            orders.remove(order);
            return true;
        }
        return false;
    }

    public List<Order> listOrdersByUser(Long userId) {
        List<Order> userOrders = new ArrayList<>();
        for (Order order : orders) {
            long userId1 = order.getUserId();
            if (userId1 == userId) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }
}
