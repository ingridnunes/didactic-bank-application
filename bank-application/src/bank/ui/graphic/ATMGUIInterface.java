/*
 * Created on 6 Jan 2014 21:59:44 
 */
package bank.ui.graphic;

import java.awt.GridLayout;

import bank.business.AccountOperationService;
import bank.business.domain.ATM;
import bank.business.domain.CurrentAccount;
import bank.ui.TextManager;
import bank.ui.graphic.action.BalanceAction;
import bank.ui.graphic.action.ClientLoginAction;
import bank.ui.graphic.action.DepositAction;
import bank.ui.graphic.action.ExitAction;
import bank.ui.graphic.action.LogoutAction;
import bank.ui.graphic.action.StatementAction;
import bank.ui.graphic.action.TransferAction;
import bank.ui.graphic.action.WithdrawalAction;

/**
 * @author ingrid
 * 
 */
public class ATMGUIInterface extends BankGraphicInterface {

	public ATMGUIInterface(ATM location, TextManager textManager,
			AccountOperationService accountOperationService) {
		super(location, textManager);

		LogoutAction logoutAction = new LogoutAction(this, textManager);
		ExitAction exitAction = new ExitAction(this, textManager, logoutAction);
		this.setExitAction(exitAction);

		addMenuAction(new ClientLoginAction(this, textManager,
				accountOperationService));
		addMenuAction(logoutAction);
		addMenuAction(exitAction);

		addPanelAction(new BalanceAction(this, textManager,
				accountOperationService));
		addPanelAction(new StatementAction(this, textManager,
				accountOperationService));
		addPanelAction(new DepositAction(this, textManager,
				accountOperationService));
		addPanelAction(new WithdrawalAction(this, textManager,
				accountOperationService));
		addPanelAction(new TransferAction(this, textManager,
				accountOperationService));
	}

	@Override
	protected GridLayout getLayoutManager() {
		return new GridLayout(3, 2, 5, 5);
	}

	@Override
	public Long readBranchId() {
		return isLoggedIn() ? ((CurrentAccount) getCredentials()).getId()
				.getBranch().getNumber() : null;
	}

	@Override
	public Long readCurrentAccountNumber() {
		return isLoggedIn() ? ((CurrentAccount) getCredentials()).getId()
				.getNumber() : null;
	}

}
