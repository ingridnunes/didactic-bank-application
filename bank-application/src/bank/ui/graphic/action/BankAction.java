/*
 * Created on 6 Jan 2014 20:19:53 
 */
package bank.ui.graphic.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bank.business.BusinessException;
import bank.ui.TextManager;
import bank.ui.UIAction;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author ingrid
 * 
 */
public abstract class BankAction extends AbstractAction implements UIAction {

	private static final long serialVersionUID = 6251170147656457707L;

	protected final BankGraphicInterface bankInterface;
	protected final Log log;
	protected final TextManager textManager;

	protected BankAction(BankGraphicInterface bankInterface,
			TextManager textManager) {
		super();
		this.log = LogFactory.getLog(this.getClass());
		this.bankInterface = bankInterface;
		this.textManager = textManager;

		putValue(Action.ACCELERATOR_KEY, null);
		putValue(Action.ACTION_COMMAND_KEY, this.getClass()
				.getSimpleName());
		putValue(Action.LONG_DESCRIPTION, null);
		putValue(Action.MNEMONIC_KEY, null);
		putValue(Action.NAME, null);
		putValue(Action.SHORT_DESCRIPTION, null);
		putValue(Action.SMALL_ICON, null);
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			execute();
		} catch (BusinessException be) {
			GUIUtils.INSTANCE.showMessage(bankInterface.getFrame(),
					be.getMessage(), be.getArgs(), JOptionPane.WARNING_MESSAGE);
			log.warn(be);
		} catch (Exception exc) {
			GUIUtils.INSTANCE.handleUnexceptedError(bankInterface.getFrame(),
					exc);
		}
	}

	public String getCommandKey() {
		return (String) getValue(Action.ACTION_COMMAND_KEY);
	}

}
