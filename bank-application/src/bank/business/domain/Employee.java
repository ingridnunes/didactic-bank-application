package bank.business.domain;

import java.util.Date;

/**
 * @author Ingrid Nunes
 * 
 */
public class Employee extends User implements Credentials {

	private String username;

	public Employee(String firstName, String lastName, String username,
			String password, Date birthday) {
		super(firstName, lastName, password, birthday);
		this.username = username;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

}
