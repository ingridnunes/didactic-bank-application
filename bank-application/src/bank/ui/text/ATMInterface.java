package bank.ui.text;

import bank.business.AccountOperationService;
import bank.business.domain.ATM;
import bank.business.domain.CurrentAccount;
import bank.ui.text.command.BalanceCommand;
import bank.ui.text.command.ClientLoginCommand;
import bank.ui.text.command.DepositCommand;
import bank.ui.text.command.LogoutCommand;
import bank.ui.text.command.StatementCommand;
import bank.ui.text.command.TransferCommand;
import bank.ui.text.command.WithdrawalCommand;

/**
 * @author Ingrid Nunes
 * 
 */
public class ATMInterface extends BankTextInterface {

	public ATMInterface(ATM atm, AccountOperationService accountOperationService) {
		super(atm);
		this.addAction("L", new ClientLoginCommand(this,
				accountOperationService));
		this.addAction("B", new BalanceCommand(this, accountOperationService));
		this.addAction("S", new StatementCommand(this, accountOperationService));
		this.addAction("D", new DepositCommand(this, accountOperationService));
		this.addAction("W",
				new WithdrawalCommand(this, accountOperationService));
		this.addAction("T", new TransferCommand(this, accountOperationService));
		this.addAction("O", new LogoutCommand(this));
	}

	@Override
	public Long readBranchId() {
		return isLoggedIn() ? ((CurrentAccount) getCredentials()).getId()
				.getBranch().getNumber() : 0;
	}

	@Override
	public Long readCurrentAccountNumber() {
		return isLoggedIn() ? ((CurrentAccount) getCredentials()).getId()
				.getNumber() : 0;
	}

}
