package bank.ui.text.command;

import bank.business.AccountOperationService;
import bank.business.domain.CurrentAccount;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public class ClientLoginCommand extends Command {

	private final AccountOperationService accountOperationService;

	public ClientLoginCommand(BankTextInterface bankInterface,
			AccountOperationService accountOperationService) {
		super(bankInterface);
		setEnabled(true);
		this.accountOperationService = accountOperationService;
	}

	@Override
	public void execute() throws Exception {
		Long branch = UIUtils.INSTANCE.readLong("branch");
		Long accountNumber = UIUtils.INSTANCE.readLong("account.number");
		String password = UIUtils.INSTANCE.readString("password");

		CurrentAccount currentAccount = accountOperationService.login(branch,
				accountNumber, password);
		bankInterface.login(currentAccount);
	}

}
