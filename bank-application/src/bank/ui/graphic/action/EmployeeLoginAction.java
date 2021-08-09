/*
 * Created on 6 Jan 2014 21:05:54 
 */
package bank.ui.graphic.action;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import bank.business.AccountManagementService;
import bank.business.BusinessException;
import bank.business.domain.Employee;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author Ingrid Nunes
 * 
 */
public class EmployeeLoginAction extends BankAction {

	private static final long serialVersionUID = 5090183202921964451L;

	private AccountManagementService accountManagementService;
	private JDialog dialog;
	private JPasswordField password;
	private JTextField username;

	public EmployeeLoginAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountManagementService accountManagementService) {
		super(bankInterface, textManager);
		this.accountManagementService = accountManagementService;

		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		putValue(Action.NAME, textManager.getText("action.login"));
		setEnabled(true);
	}

	@Override
	public void execute() throws Exception {
		this.username = new JTextField();
		username.setColumns(10);
		this.password = new JPasswordField();
		password.setColumns(10);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel subpanel = new JPanel(new GridLayout(2, 2, 5, 5));
		subpanel.add(new JLabel(textManager.getText("username") + ":"));
		subpanel.add(username);
		subpanel.add(new JLabel(textManager.getText("password") + ":"));
		subpanel.add(password);
		panel.add(subpanel, BorderLayout.CENTER);

		subpanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton ok = new JButton(textManager.getText("button.ok"));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				executeLogin();
			}
		});
		subpanel.add(ok);
		panel.add(subpanel, BorderLayout.SOUTH);

		this.dialog = GUIUtils.INSTANCE.createDialog(bankInterface.getFrame(),
				"action.login", panel);
		this.dialog.setVisible(true);
	}

	private void executeLogin() {
		try {
			Employee employee = accountManagementService.login(
					username.getText(), new String(password.getPassword()));
			bankInterface.login(employee);
			dialog.dispose();
			dialog = null;
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
