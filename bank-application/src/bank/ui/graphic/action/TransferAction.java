/*
 * Created on 6 Jan 2014 21:14:05 
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
import bank.business.domain.Transfer;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author ingrid
 * 
 */
public class TransferAction extends AccountAbstractAction {

	private static final long serialVersionUID = 5090183202921964451L;

	private JFormattedTextField amount;
	private JDialog dialog;
	protected JFormattedTextField dstAccountNumber;
	protected JFormattedTextField dstBranch;

	public TransferAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountOperationService accountOperationService) {
		super(bankInterface, textManager, accountOperationService);

		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
		putValue(Action.NAME, textManager.getText("action.transfer"));
	}

	@Override
	public void execute() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel subpanel = new JPanel(new GridLayout(5, 2, 5, 5));

		initAndAddAccountFields(subpanel);

		this.dstBranch = new JFormattedTextField(
				NumberFormat.getIntegerInstance());
		dstBranch.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("destination.branch") + ":"));
		subpanel.add(dstBranch);

		this.dstAccountNumber = new JFormattedTextField(
				NumberFormat.getIntegerInstance());
		dstAccountNumber.setColumns(10);
		subpanel.add(new JLabel(textManager
				.getText("destination.account.number") + ":"));
		subpanel.add(dstAccountNumber);

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
				executeTransfer();
			}
		});
		subpanel.add(ok);
		panel.add(subpanel, BorderLayout.SOUTH);

		this.dialog = GUIUtils.INSTANCE.createDialog(bankInterface.getFrame(),
				"action.transfer", panel);
		this.dialog.setVisible(true);
	}

	private void executeTransfer() {
		try {
			if (!checkAccountFields())
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					dstBranch.getValue(), "destination.branch"))
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					dstAccountNumber.getValue(), "destination.account.number"))
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					amount.getValue(), "amount"))
				return;
			Transfer transfer = accountOperationService.transfer(bankInterface
					.getOperationLocation().getNumber(), ((Number) branch
					.getValue()).longValue(), ((Number) accountNumber
					.getValue()).longValue(), ((Number) dstBranch.getValue())
					.longValue(), ((Number) dstAccountNumber.getValue())
					.longValue(), ((Number) amount.getValue()).doubleValue());
			StringBuffer sb = new StringBuffer();
			sb.append(textManager.getText("message.operation.succesfull"))
					.append("\n");
			sb.append(textManager.getText("transfer") + ": "
					+ transfer.getAmount());
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
