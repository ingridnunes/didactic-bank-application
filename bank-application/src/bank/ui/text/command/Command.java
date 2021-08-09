package bank.ui.text.command;

import bank.ui.TextManager;
import bank.ui.UIAction;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class Command implements UIAction {

	protected final BankTextInterface bankInterface;
	private boolean isEnabled;

	protected Command(BankTextInterface bankInterface) {
		this(bankInterface, false);
	}

	protected Command(BankTextInterface bankInterface, boolean isEnabled) {
		this.bankInterface = bankInterface;
		this.isEnabled = isEnabled;
	}

	protected TextManager getTextManager() {
		return UIUtils.INSTANCE.getTextManager();
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

}
