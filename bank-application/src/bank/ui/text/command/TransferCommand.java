package bank.ui.text.command;

import bank.business.AccountOperationService;
import bank.business.domain.Transfer;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public class TransferCommand extends Command {

	private final AccountOperationService accountOperationService;

	public TransferCommand(BankTextInterface bankInterface,
			AccountOperationService accountOperationService) {
		super(bankInterface);
		this.accountOperationService = accountOperationService;
	}

	@Override
	public void execute() throws Exception {
		Long srcBranch = bankInterface.readBranchId();
		Long srcAccountNumber = bankInterface.readCurrentAccountNumber();

		Long dstBranch = UIUtils.INSTANCE.readLong("destination.branch");
		Long dstAccountNumber = UIUtils.INSTANCE
				.readLong("destination.account.number");

		Double amount = UIUtils.INSTANCE.readDouble("amount");

		Transfer transfer = accountOperationService.transfer(bankInterface
				.getOperationLocation().getNumber(), srcBranch,
				srcAccountNumber, dstBranch, dstAccountNumber, amount);

		System.out.println(getTextManager().getText(
				"message.operation.succesfull"));
		System.out.println(getTextManager().getText("transfer") + ": "
				+ transfer.getAmount());
	}

}