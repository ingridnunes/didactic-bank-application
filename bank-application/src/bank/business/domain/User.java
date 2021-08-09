package bank.business.domain;

import java.util.Date;

/**
 * This class should be very carefully used in collections, as some of its
 * children implements equals and hashcode.
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class User {

	protected Date birthday;
	protected String firstName;
	protected String lastName;
	protected String password;

	protected User(String firstName, String lastName, String password,
			Date birthday) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.birthday = birthday;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	public boolean isValidPassword(String password) {
		return this.password.equals(password);
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
