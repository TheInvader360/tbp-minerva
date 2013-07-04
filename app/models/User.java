package models;

import javax.persistence.Entity;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	public String username;
	public String password;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static User connect(String username, String password) {
		return find("byUsernameAndPassword", username, password).first();
	}
}
