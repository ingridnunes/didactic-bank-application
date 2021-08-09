package bank;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import bank.business.AccountManagementService;
import bank.business.domain.ATM;
import bank.business.domain.Branch;
import bank.business.domain.OperationLocation;
import bank.business.impl.AccountManagementServiceImpl;
import bank.business.impl.AccountOperationServiceImpl;
import bank.data.Database;
import bank.ui.BankInterface;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class Bank {

	public static final String PROPERTIES_FILE_LOG4J = "log4j.properties";
	public static final String TEXT_FLAG = "-t";

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(Bank.class
				.getResource(PROPERTIES_FILE_LOG4J));

		Bank bank = null;
		if (args != null && args.length > 0 && TEXT_FLAG.equals(args[0])) {
			bank = new BankText();
		} else {
			bank = new BankGraphic();
		}
		bank.showUI();
	}

	protected final List<BankInterface> bankInterfaces;

	public Bank() {
		Database database = new Database();

		AccountManagementService accountManagementService = new AccountManagementServiceImpl(
				database);
		AccountOperationServiceImpl accountOperationService = new AccountOperationServiceImpl(
				database);

		this.bankInterfaces = new ArrayList<>(database
				.getAllOperationLocations().size());

		for (OperationLocation ol : database.getAllOperationLocations()) {
			if (ol instanceof Branch) {
				bankInterfaces.add(createBranchInterface((Branch) ol,
						accountManagementService, accountOperationService));

			} else if (ol instanceof ATM) {
				bankInterfaces.add(createATMInterface((ATM) ol,
						accountOperationService));

			}
		}

	}

	public abstract BankInterface createATMInterface(ATM atm,
			AccountOperationServiceImpl accountOperationService);

	public abstract BankInterface createBranchInterface(Branch branch,
			AccountManagementService accountManagementService,
			AccountOperationServiceImpl accountOperationService);

	public abstract void showUI();

}
