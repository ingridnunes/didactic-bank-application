/*
 * Created on 6 Jan 2014 14:29:43 
 */
package bank.ui.text.command;

import java.util.Date;

import bank.business.AccountManagementService;
import bank.business.domain.CurrentAccount;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author ingrid
 * 
 */
public class CreateAccountCommand extends Command {

	private final AccountManagementService accountManagementService;

	public CreateAccountCommand(BankTextInterface bankInterface,
			AccountManagementService accountManagementService) {
		super(bankInterface);
		this.accountManagementService = accountManagementService;
	}

	@Override
	public void execute() throws Exception {
		UIUtils uiUtils = UIUtils.INSTANCE;

		Long branch = bankInterface.getOperationLocation().getNumber();
		String name = uiUtils.readString("firstName");
		String lastName = uiUtils.readString("lastName");
		Integer cpf = uiUtils.readInteger("cpf");
		Date birthday = uiUtils.readDate("birthday");
		Double balance = uiUtils.readDouble("initial.balance");

		CurrentAccount currentAccount = accountManagementService
				.createCurrentAccount(branch, name, lastName, cpf, birthday,
						balance);

		System.out.println(getTextManager().getText(
				"message.operation.succesfull"));
		System.out.println(getTextManager().getText("client"));
		System.out.println(uiUtils.propertyToString("firstName", currentAccount
				.getClient().getFirstName()));
		System.out.println(uiUtils.propertyToString("lastName", currentAccount
				.getClient().getLastName()));
		System.out.println(uiUtils.propertyToString("cpf", currentAccount
				.getClient().getCpf()));
		System.out.println(uiUtils.propertyToString("birthday", currentAccount
				.getClient().getBirthday()));
		System.out.println(uiUtils.propertyToString("password", currentAccount
				.getClient().getPassword()));
		System.out.println(getTextManager().getText("current.account"));
		System.out.println(uiUtils.propertyToString("branch", currentAccount
				.getId().getBranch().toString()));
		System.out.println(uiUtils.propertyToString("number", currentAccount
				.getId().getNumber()));
		System.out.println(uiUtils.propertyToString("balance",
				currentAccount.getBalance()));
	}

}
