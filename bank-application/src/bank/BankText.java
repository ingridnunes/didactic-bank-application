/*
 * Created on 7 Jan 2014 21:21:45 
 */
package bank;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import bank.business.AccountManagementService;
import bank.business.domain.ATM;
import bank.business.domain.Branch;
import bank.business.impl.AccountOperationServiceImpl;
import bank.ui.BankInterface;
import bank.ui.text.ATMInterface;
import bank.ui.text.BranchInterface;
import bank.ui.text.UIUtils;

/**
 * @author ingrid
 * 
 */
public class BankText extends Bank {

	protected final BufferedReader reader;

	public BankText() {
		this.reader = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public BankInterface createATMInterface(ATM atm,
			AccountOperationServiceImpl accountOperationService) {
		return new ATMInterface(atm, accountOperationService);
	}

	@Override
	public BankInterface createBranchInterface(Branch branch,
			AccountManagementService accountManagementService,
			AccountOperationServiceImpl accountOperationService) {
		return new BranchInterface(branch, accountManagementService,
				accountOperationService);
	}

	private String getMenu() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bankInterfaces.size(); i++) {
			BankInterface bi = bankInterfaces.get(i);
			sb.append(i + 1).append(" - ");
			if (bi instanceof BranchInterface) {
				sb.append(((Branch) bi.getOperationLocation()).getName());
			} else {
				assert bi instanceof ATMInterface;
				sb.append("ATM ").append(bi.getOperationLocation().getNumber());
			}
			sb.append("\n");
		}
		sb.append(UIUtils.INSTANCE.getTextManager().getText(
				"message.choose.bank.interface")
				+ ": ");
		return sb.toString();
	}

	public void showUI() {
		UIUtils uiUtils = UIUtils.INSTANCE;
		System.out.print(getMenu());
		Integer option = uiUtils.readInteger(null);
		while (option != 0) {
			if (option > 0 && option <= bankInterfaces.size()) {
				this.bankInterfaces.get(option - 1).createAndShowUI();
			}
			System.out.print(getMenu());
			option = uiUtils.readInteger(null);
		}
	}

}
