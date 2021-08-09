/*
 * Created on 6 Jan 2014 20:17:52 
 */
package bank.ui.graphic.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;

/**
 * @author Ingrid Nunes
 * 
 */
public class LogoutAction extends BankAction {

	private static final long serialVersionUID = 5090183202921964451L;

	public LogoutAction(BankGraphicInterface bankInterface,
			TextManager textManager) {
		super(bankInterface, textManager);

		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		putValue(Action.NAME, textManager.getText("action.logout"));
	}

	@Override
	public void execute() throws Exception {
		int answer = JOptionPane.showConfirmDialog(bankInterface.getFrame()
				.getContentPane(), textManager
				.getText("message.confirm.cancel"), bankInterface.getFrame()
				.getTitle(), JOptionPane.WARNING_MESSAGE);

		if (JOptionPane.YES_OPTION == answer) {
			bankInterface.logout();
		}
	}

}
