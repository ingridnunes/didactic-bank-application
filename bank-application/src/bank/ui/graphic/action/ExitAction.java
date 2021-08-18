/*
 * Created on 6 Jan 2014 20:16:55 
 */
package bank.ui.graphic.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;

/**
 * @author Ingrid Nunes
 * 
 */
public class ExitAction extends BankAction {

	private static final long serialVersionUID = 5090183202921964451L;

	private LogoutAction logoutAction;

	public ExitAction(BankGraphicInterface bankInterface,
			TextManager textManager, LogoutAction logoutAction) {
		super(bankInterface, textManager);
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
		putValue(Action.NAME, textManager.getText("action.exit"));
		super.setEnabled(true);
		this.logoutAction = logoutAction;
	}

	public void execute() throws Exception {
		if (bankInterface.isLoggedIn()) {
			logoutAction
					.actionPerformed(new ActionEvent(this,
							ActionEvent.ACTION_PERFORMED, logoutAction
									.getCommandKey()));
			if (bankInterface.isLoggedIn()) {
				return;
			}
		}
		System.exit(0);
	}

	@Override
	public void setEnabled(boolean newValue) {
		// Do nothing
	}

}
