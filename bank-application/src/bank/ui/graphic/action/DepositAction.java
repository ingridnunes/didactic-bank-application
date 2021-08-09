/*
 * Created on 6 Jan 2014 21:13:45 
 */
package bank.ui.graphic.action;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import bank.business.AccountOperationService;
import bank.business.BusinessException;
import bank.business.domain.Deposit;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author ingrid
 * 
 */
public class DepositAction extends AccountAbstractAction {

	private static final long serialVersionUID = 5090183202921964451L;

	private JFormattedTextField amount;
	private JDialog dialog;
	private JFormattedTextField envelope;

	public DepositAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountOperationService accountOperationService) {
		super(bankInterface, textManager, accountOperationService);

		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
		putValue(Action.NAME, textManager.getText("action.deposit"));
	}

	@Override
	public void execute() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel subpanel = new JPanel(new GridLayout(4, 2, 5, 5));

		initAndAddAccountFields(subpanel);

		this.envelope = new JFormattedTextField(
				NumberFormat.getIntegerInstance());
		envelope.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("envelope") + ":"));
		subpanel.add(envelope);

		this.amount = new JFormattedTextField(NumberFormat.getNumberInstance());
		amount.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("amount") + ":"));
		subpanel.add(amount);

		panel.add(subpanel, BorderLayout.CENTER);

		subpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton ok = new JButton(textManager.getText("button.ok"));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				executeDeposit();
			}
		});
		subpanel.add(ok);
		panel.add(subpanel, BorderLayout.SOUTH);

		this.dialog = GUIUtils.INSTANCE.createDialog(bankInterface.getFrame(),
				"action.deposit", panel);
		this.dialog.setVisible(true);
	}

	private void executeDeposit() {
		try {
			if (!checkAccountFields())
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					amount.getValue(), "amount"))
				return;
			Deposit deposit = accountOperationService.deposit(bankInterface
					.getOperationLocation().getNumber(), ((Number) branch
					.getValue()).longValue(), ((Number) accountNumber
					.getValue()).longValue(), ((Number) envelope.getValue())
					.intValue(), ((Number) amount.getValue()).doubleValue());
			StringBuffer sb = new StringBuffer();
			sb.append(textManager.getText("message.operation.succesfull"))
					.append("\n");
			sb.append(textManager.getText("deposit") + ": "
					+ deposit.getAmount());
			GUIUtils.INSTANCE.showMessage(bankInterface.getFrame(),
					sb.toString(), JOptionPane.INFORMATION_MESSAGE);
			dialog.dispose();
		} catch (BusinessException be) {
			GUIUtils.INSTANCE.showMessage(bankInterface.getFrame(),
					be.getMessage(), be.getArgs(), JOptionPane.WARNING_MESSAGE);
			log.warn(be);
		} catch (Exception exc) {
			GUIUtils.INSTANCE.handleUnexceptedError(bankInterface.getFrame(),
					exc);
		}
	}

}
