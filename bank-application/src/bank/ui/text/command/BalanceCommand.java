/*
 * Created on 6 Jan 2014 14:30:10 
 */
package bank.ui.text.command;

import bank.business.AccountOperationService;
import bank.ui.text.BankTextInterface;

/**
 * @author ingrid
 *
 */
public class BalanceCommand  extends Command {

	private final AccountOperationService accountOperationService;

	public BalanceCommand(BankTextInterface bankInterface,
			AccountOperationService accountOperationService) {
		super(bankInterface);
		this.accountOperationService = accountOperationService;
	}

	@Override
	public void execute() throws Exception {
		Long branch = bankInterface.readBranchId();
		Long accountNumber = bankInterface.readCurrentAccountNumber();
		
		Double balance = accountOperationService.getBalance(branch, accountNumber);
		
		System.out
				.println(getTextManager().getText("balance") + ": "
				+ balance);
	}

}