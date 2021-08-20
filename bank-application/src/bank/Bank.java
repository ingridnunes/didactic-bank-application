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
 * This class provides the implementation of the main method used for 
 * initializing the Bank application and specifies the methods required to be
 * implemented in concrete interfaces. 
 * 
 * @author Ingrid Nunes
 * 
 */
public abstract class Bank {

	public static final String PROPERTIES_FILE_LOG4J = "log4j.properties";
	public static final String TEXT_FLAG = "-t";

	/**
	 * This method starts the Bank application. It instantiates the user
	 * interfaces according to the provided parameter and the displays it.
	 * 
	 * @param args	-t in the first position of the array of Strings to start 
	 * 				the application with the textual interface. Otherwise, the 
	 * 				graphical interface is used.
	 * @throws Exception 
	 */
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

	/**
	 * This method instantiates a Bank class. First, it instantiates the 
	 * database and the facade of the business module of the application. The 
	 * interface of each operation location of the Bank (branches and ATMs) are 
	 * also instantiated. 
	 */
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

	/**
	 * This method instantiates an ATM interface.
	 * 
	 * @param atm	the corresponding ATM associated with the interface to be
	 * 				created.
	 * @param accountOperationService	the facade to the services associated 
	 * 									with the account operation.
	 * 							
	 * @return the instantiated ATM interface
	 */
	public abstract BankInterface createATMInterface(ATM atm,
			AccountOperationServiceImpl accountOperationService);

	/**
	 * This method instantiates a Branch interface.
	 * 
	 * @param branch	the corresponding Branch associated with the interface 
	 * 					to be created.
	 * @param accountManagementService	the facade to the services associated 
	 * 									with the account management.	
	 * @param accountOperationService	the facade to the services associated 
	 * 									with the account operation.
	 * 							
	 * @return the instantiated ATM interface
	 */
	public abstract BankInterface createBranchInterface(Branch branch,
			AccountManagementService accountManagementService,
			AccountOperationServiceImpl accountOperationService);

	/**
	 * This method display the user interface of the Bank application.
	 */
	public abstract void showUI();

}
