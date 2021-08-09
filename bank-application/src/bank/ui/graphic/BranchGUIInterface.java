/*
 * Created on 6 Jan 2014 21:59:56 
 */
package bank.ui.graphic;

import java.awt.GridLayout;

import bank.business.AccountManagementService;
import bank.business.AccountOperationService;
import bank.business.domain.Branch;
import bank.ui.TextManager;
import bank.ui.graphic.action.BalanceAction;
import bank.ui.graphic.action.CreateAccountAction;
import bank.ui.graphic.action.DepositAction;
import bank.ui.graphic.action.EmployeeLoginAction;
import bank.ui.graphic.action.ExitAction;
import bank.ui.graphic.action.LogoutAction;
import bank.ui.graphic.action.StatementAction;
import bank.ui.graphic.action.TransferAction;
import bank.ui.graphic.action.WithdrawalAction;

/**
 * @author Ingrid Nunes
 * 
 */
public class BranchGUIInterface extends BankGraphicInterface {

	public BranchGUIInterface(Branch location, TextManager textManager,
			AccountManagementService accountManagementService,
			AccountOperationService accountOperationService) {
		super(location, textManager);

		LogoutAction logoutAction = new LogoutAction(this, textManager);
		ExitAction exitAction = new ExitAction(this, textManager, logoutAction);
		this.setExitAction(exitAction);

		addMenuAction(new EmployeeLoginAction(this, textManager,
				accountManagementService));
		addMenuAction(logoutAction);
		addMenuAction(exitAction);

		addPanelAction(new CreateAccountAction(this, textManager,
				accountManagementService));
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
		return new GridLayout(2, 3, 5, 5);
	}

	@Override
	public Long readBranchId() {
		return null;
	}

	@Override
	public Long readCurrentAccountNumber() {
		return null;
	}

}
