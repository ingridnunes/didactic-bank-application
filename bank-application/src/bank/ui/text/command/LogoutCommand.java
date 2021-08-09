package bank.ui.text.command;

import bank.ui.text.BankTextInterface;

/**
 * @author Ingrid Nunes
 * 
 */
public class LogoutCommand extends Command {

	public LogoutCommand(BankTextInterface bankInterface) {
		super(bankInterface);
	}

	@Override
	public void execute() throws Exception {
		bankInterface.logout();
	}

}
