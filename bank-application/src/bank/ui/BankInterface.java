/*
 * Created on 6 Jan 2014 21:27:38 
 */
package bank.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bank.business.domain.Credentials;
import bank.business.domain.OperationLocation;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class BankInterface {

	protected final Map<String, UIAction> actions;
	private Credentials credentials;
	protected final OperationLocation location;
	protected final Log log;

	protected BankInterface(OperationLocation location) {
		this.log = LogFactory.getLog(getClass());
		this.location = location;
		this.actions = new LinkedHashMap<>();
	}

	protected void addAction(String code, UIAction action) {
		this.actions.put(code, action);
	}

	public abstract void createAndShowUI();

	protected Credentials getCredentials() {
		return credentials;
	}

	public OperationLocation getOperationLocation() {
		return location;
	}

	public boolean isLoggedIn() {
		return this.credentials != null;
	}

	public void login(Credentials credentials) {
		this.credentials = credentials;
		if (isLoggedIn()) {
			toggleActions();
		}
	}

	public void logout() {
		this.credentials = null;
		toggleActions();
	}

	public abstract Long readBranchId();

	public abstract Long readCurrentAccountNumber();

	protected void toggleActions() {
		for (UIAction action : actions.values()) {
			action.setEnabled(!action.isEnabled());
		}
	}

}
