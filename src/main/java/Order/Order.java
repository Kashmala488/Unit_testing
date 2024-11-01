package Order;

public class Order {
    private Long orderId;
    private Long userId;
    private String product;

    public Order(Long orderId, Long userId, String product) {
        this.orderId = orderId;
        this.userId = userId;
        this.product = product;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Order order = (Order) obj;
        return orderId.equals(order.orderId) && userId.equals(order.userId) && product.equals(order.product);
    }

    @Override
    public int hashCode() {
        return orderId.hashCode();
    }

	public void setProduct(String product2) {
	this.product=product2;
		
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
