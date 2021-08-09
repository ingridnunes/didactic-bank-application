package bank.ui.text.command;

import bank.business.AccountManagementService;
import bank.business.domain.Employee;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public class EmployeeLoginCommand extends Command {

	private final AccountManagementService accountManagementService;

	public EmployeeLoginCommand(BankTextInterface bankInterface,
			AccountManagementService accountManagementService) {
		super(bankInterface, true);
		this.accountManagementService = accountManagementService;
	}

	@Override
	public void execute() throws Exception {
		String username = UIUtils.INSTANCE.readString("username");
		String password = UIUtils.INSTANCE.readString("password");

		Employee employee = accountManagementService.login(username, password);
		bankInterface.login(employee);
	}

}
