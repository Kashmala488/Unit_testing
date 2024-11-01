package User;

//User.java
public class User {
 private Long id;
 private String username;
 private String password;

 public User(Long id, String username, String password) {
     this.id = id;
     this.username = username;
     this.password = password;
 }


 public Long getId() 
 { return id;
 }
 public String getUsername() { 
	 return username;
	 }
 public String getPassword() {
	 return password; 
	 }

public void setUsername(String username2) {
	username=username2;
	
}

public void setPassword(String password2) {
	
	password=password2;
}
}

