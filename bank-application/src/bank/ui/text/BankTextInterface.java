package bank.ui.text;

import bank.business.BusinessException;
import bank.business.domain.OperationLocation;
import bank.ui.BankInterface;
import bank.ui.TextManager;
import bank.ui.UIAction;
import bank.ui.text.command.Command;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class BankTextInterface extends BankInterface {

	public static final String EXIT_CODE = "E";

	protected BankTextInterface(OperationLocation location) {
		super(location);
	}

	public void createAndShowUI() {
		UIUtils uiUtils = UIUtils.INSTANCE;
		String commandKey = null;
		do {
			System.out.println();
			System.out.print(getMenu(uiUtils.getTextManager()));
			commandKey = uiUtils.readString(null);
			Command command = (Command) actions.get(commandKey);
			if (command != null) {
				try {
					command.execute();
				} catch (BusinessException be) {
					System.out.println(uiUtils.getTextManager().getText(
							be.getMessage(), be.getArgs()));
					log.warn(be);
				} catch (Exception e) {
					uiUtils.handleUnexceptedError(e);
				}
			}
		} while (!EXIT_CODE.equals(commandKey));
		if (isLoggedIn()) {
			logout();
		}
	}

	protected String getMenu(TextManager textManager) {
		StringBuffer sb = new StringBuffer();
		sb.append(textManager.getText("message.options", EXIT_CODE, false))
				.append(":\n");
		for (String key : actions.keySet()) {
			UIAction action = actions.get(key);
			if (action.isEnabled()) {
				sb.append(key)
						.append(" - ")
						.append(textManager.getText(action.getClass()
								.getSimpleName())).append("\n");
			}
		}
		sb.append(textManager.getText("message.choose.option")).append(": ");

		return sb.toString();
	}

}
