package bank.ui.text.command;

import bank.business.AccountOperationService;
import bank.business.domain.Withdrawal;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public class WithdrawalCommand extends Command {

	private final AccountOperationService accountOperationService;

	public WithdrawalCommand(BankTextInterface bankInterface,
			AccountOperationService accountOperationService) {
		super(bankInterface);
		this.accountOperationService = accountOperationService;
	}

	@Override
	public void execute() throws Exception {
		Long branch = bankInterface.readBranchId();
		Long accountNumber = bankInterface.readCurrentAccountNumber();
		Double amount = UIUtils.INSTANCE.readDouble("amount");

		Withdrawal withdrawal = accountOperationService.withdrawal(
				bankInterface.getOperationLocation().getNumber(), branch,
				accountNumber, amount);

		System.out.println(getTextManager().getText(
				"message.operation.succesfull"));
		System.out.println(getTextManager().getText("withdrawal") + ": "
				+ withdrawal.getAmount());
	}

}
