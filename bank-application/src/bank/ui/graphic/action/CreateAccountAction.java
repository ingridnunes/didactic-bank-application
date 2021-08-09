/*
 * Created on 6 Jan 2014 21:13:34 
 */
package bank.ui.graphic.action;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Date;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import bank.business.AccountManagementService;
import bank.business.BusinessException;
import bank.business.domain.CurrentAccount;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public class CreateAccountAction extends BankAction {

	private static final long serialVersionUID = 5090183202921964451L;

	private AccountManagementService accountManagementService;

	private JFormattedTextField balance;
	protected JFormattedTextField birthday;
	protected JFormattedTextField cpf;
	private JDialog dialog;
	protected JTextField lastName;
	protected JTextField name;

	public CreateAccountAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountManagementService accountManagementService) {
		super(bankInterface, textManager);
		this.accountManagementService = accountManagementService;

		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		putValue(Action.NAME,
				textManager.getText("action.create.account"));
	}

	private void createAccount() {
		try {
			if (!GUIUtils.INSTANCE.checkMandatoryString(
					bankInterface.getFrame(), name.getText(), "firstName"))
				return;
			if (!GUIUtils.INSTANCE.checkMandatoryString(
					bankInterface.getFrame(), lastName.getText(), "lastName"))
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					cpf.getValue(), "cpf"))
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					birthday.getValue(), "birthday"))
				return;
			if (!GUIUtils.INSTANCE.checkMandatory(bankInterface.getFrame(),
					balance.getValue(), "initial.balance"))
				return;

			CurrentAccount currentAccount = accountManagementService
					.createCurrentAccount(bankInterface.getOperationLocation()
							.getNumber(), name.getText(), lastName.getText(),
							((Number) cpf.getValue()).intValue(),
							(Date) birthday.getValue(), ((Number) balance
									.getValue()).doubleValue());

			StringBuffer sb = new StringBuffer();
			sb.append(textManager.getText("message.operation.succesfull"))
					.append("\n");

			sb.append("- ").append(textManager.getText("client")).append("\n");
			sb.append(textManager.getText("firstName")).append(": ")
					.append(currentAccount.getClient().getFirstName())
					.append("\n");
			sb.append(textManager.getText("lastName")).append(": ")
					.append(currentAccount.getClient().getLastName())
					.append("\n");
			sb.append(textManager.getText("cpf")).append(": ")
					.append(currentAccount.getClient().getCpf()).append("\n");
			sb.append(textManager.getText("birthday"))
					.append(": ")
					.append(GUIUtils.DATE_FORMAT.format(currentAccount
							.getClient().getBirthday())).append("\n");
			sb.append(textManager.getText("password")).append(": ")
					.append(currentAccount.getClient().getPassword())
					.append("\n");

			sb.append("- ").append(textManager.getText("current.account"))
					.append("\n");
			sb.append(textManager.getText("branch")).append(": ")
					.append(currentAccount.getId().getBranch()).append("\n");
			sb.append(textManager.getText("number")).append(": ")
					.append(currentAccount.getId().getNumber()).append("\n");
			sb.append(textManager.getText("balance")).append(": ")
					.append(currentAccount.getBalance());

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

	@Override
	public void execute() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel subpanel = new JPanel(new GridLayout(5, 2, 5, 5));

		this.name = new JTextField();
		name.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("firstName") + ":"));
		subpanel.add(name);

		this.lastName = new JTextField();
		lastName.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("lastName") + ":"));
		subpanel.add(lastName);

		this.cpf = new JFormattedTextField(NumberFormat.getIntegerInstance());
		cpf.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("cpf") + ":"));
		subpanel.add(cpf);

		this.birthday = new JFormattedTextField(GUIUtils.DATE_FORMAT);
		birthday.setColumns(10);
		birthday.setToolTipText(GUIUtils.DATE_FORMAT.toPattern());
		subpanel.add(new JLabel(textManager.getText("birthday") + ":"));
		subpanel.add(birthday);

		this.balance = new JFormattedTextField(NumberFormat.getNumberInstance());
		balance.setColumns(10);
		subpanel.add(new JLabel(textManager.getText("initial.balance") + ":"));
		subpanel.add(balance);

		panel.add(subpanel, BorderLayout.CENTER);

		subpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton ok = new JButton(textManager.getText("button.ok"));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				createAccount();
			}
		});
		subpanel.add(ok);
		panel.add(subpanel, BorderLayout.SOUTH);

		this.dialog = GUIUtils.INSTANCE.createDialog(bankInterface.getFrame(),
				"action.transfer", panel);
		this.dialog.setVisible(true);
	}
}
