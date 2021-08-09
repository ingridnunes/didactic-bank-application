package bank.ui.graphic.action;

import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bank.business.AccountOperationService;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class AccountAbstractAction extends BankAction {

	private static final long serialVersionUID = 611127518534392594L;

	protected JFormattedTextField accountNumber;
	protected AccountOperationService accountOperationService;
	protected JFormattedTextField branch;

	protected AccountAbstractAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountOperationService accountOperationService) {
		super(bankInterface, textManager);
		this.accountOperationService = accountOperationService;
	}

	protected boolean checkAccountFields() {
		if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
				branch.getValue(), "branch"))
			return false;
		if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
				accountNumber.getValue(), "account.number"))
			return false;
		return true;
	}

	protected void initAndAddAccountFields(JPanel panel) {
		this.branch = new JFormattedTextField(NumberFormat.getIntegerInstance());
		branch.setColumns(10);
		panel.add(new JLabel(textManager.getText("branch") + ":"));
		panel.add(branch);

		this.accountNumber = new JFormattedTextField(
				NumberFormat.getIntegerInstance());
		accountNumber.setColumns(10);
		panel.add(new JLabel(textManager.getText("account.number") + ":"));
		panel.add(accountNumber);

		Long branchId = bankInterface.readBranchId();
		Long currentAccountNumber = bankInterface.readCurrentAccountNumber();

		if (branchId != null) {
			assert currentAccountNumber != null;
			branch.setValue(branchId);
			branch.setEditable(false);
			accountNumber.setValue(currentAccountNumber);
			accountNumber.setEditable(false);
		}
	}

}
