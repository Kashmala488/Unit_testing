package Product;

//Product.java
public class Product {
 private Long id;
 private String name;
 private String category;

 public Product(Long id, String name, String category) {
     this.id = id;
     this.name = name;
     this.category = category;
 }

 // Getters and Setters
 public Long getId() { return id; }
 public String getName() { return name; }
 public String getCategory() { return category; }

public void setName(String name2) {
	this.name=name2;
	
}

public void setCategory(String category2) {
	this.category=category2;
	
}
}
