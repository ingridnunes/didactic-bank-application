/*
 * Created on 7 Jan 2014 21:21:55 
 */
package bank;

import bank.business.AccountManagementService;
import bank.business.domain.ATM;
import bank.business.domain.Branch;
import bank.business.impl.AccountOperationServiceImpl;
import bank.ui.BankInterface;
import bank.ui.graphic.ATMGUIInterface;
import bank.ui.graphic.BranchGUIInterface;
import bank.ui.text.UIUtils;

/**
 * @author ingrid
 * 
 */
public class BankGraphic extends Bank {

	@Override
	public BankInterface createATMInterface(ATM atm,
			AccountOperationServiceImpl accountOperationService) {
		return new ATMGUIInterface(atm, UIUtils.INSTANCE.getTextManager(),
				accountOperationService);
	}

	@Override
	public BankInterface createBranchInterface(Branch branch,
			AccountManagementService accountManagementService,
			AccountOperationServiceImpl accountOperationService) {
		return new BranchGUIInterface(branch,
				UIUtils.INSTANCE.getTextManager(), accountManagementService,
				accountOperationService);
	}

	public void showUI() {
		for (BankInterface bankInterface : bankInterfaces) {
			bankInterface.createAndShowUI();
		}
	}

}
