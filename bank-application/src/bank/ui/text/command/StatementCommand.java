/*
 * Created on 6 Jan 2014 14:30:21 
 */
package bank.ui.text.command;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bank.business.AccountOperationService;
import bank.business.domain.ATM;
import bank.business.domain.Branch;
import bank.business.domain.CurrentAccountId;
import bank.business.domain.Deposit;
import bank.business.domain.Transaction;
import bank.business.domain.Transfer;
import bank.business.domain.Withdrawal;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

/**
 * @author ingrid
 * 
 */
public class StatementCommand extends Command {

	private class MonthYear {
		int month;
		int year;

		@Override
		public String toString() {
			return getTextManager().getText("month." + month) + "/" + year;
		}
	}

	public enum StatementType {
		MONTHLY(1), PERIOD(2);

		private int number;

		private StatementType(int number) {
			this.number = number;
		}

		public int getNumber() {
			return number;
		}
	}

	private static final int NUMBER_OF_POSSIBLE_MONTHS = 6;

	private final AccountOperationService accountOperationService;

	public StatementCommand(BankTextInterface bankInterface,
			AccountOperationService accountOperationService) {
		super(bankInterface);
		this.accountOperationService = accountOperationService;
	}

	@Override
	public void execute() throws Exception {
		Long branch = bankInterface.readBranchId();
		Long accountNumber = bankInterface.readCurrentAccountNumber();
		StatementType statementType = readStatementType();
		switch (statementType) {
		case MONTHLY:
			showMonthlyStatement(branch, accountNumber);
			break;
		case PERIOD:
			showStatementByPeriod(branch, accountNumber);
			break;
		}
	}

	private void printStatement(CurrentAccountId caId,
			List<Transaction> transactions) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTextManager().getText("date")).append("\t\t\t");
		sb.append(getTextManager().getText("location")).append("\t");
		sb.append(getTextManager().getText("operation.type")).append("\t");
		sb.append(getTextManager().getText("details")).append("\t");
		sb.append(getTextManager().getText("amount")).append("\n");
		sb.append("---------------------------------------------------------------------------------\n");
		for (Transaction transaction : transactions) {
			sb.append(UIUtils.INSTANCE.formatDateTime(transaction.getDate()))
					.append("\t");
			sb.append(transaction.getLocation()).append("\t");
			if (transaction.getLocation() instanceof ATM)
				sb.append("\t");
			sb.append(
					getTextManager().getText(
							"operation."
									+ transaction.getClass().getSimpleName()))
					.append("\t\t");
			if (transaction instanceof Deposit) {
				sb.append(((Deposit) transaction).getEnvelope()).append("\t\t");
				sb.append("+ ").append(transaction.getAmount());
			} else if (transaction instanceof Transfer) {
				Transfer transfer = (Transfer) transaction;
				if (transfer.getAccount().getId().equals(caId)) {
					CurrentAccountId dstId = transfer.getDestinationAccount()
							.getId();
					sb.append("AG ").append(dstId.getBranch().getNumber())
							.append(" C/C ").append(dstId.getNumber())
							.append("\t");
					sb.append("- ");
				} else {
					CurrentAccountId srcId = transfer.getAccount().getId();
					sb.append("AG ").append(srcId.getBranch().getNumber())
							.append(" C/C ").append(srcId.getNumber())
							.append("\t");
					sb.append("+ ");
				}
				sb.append(transaction.getAmount());
			} else if (transaction instanceof Withdrawal) {
				sb.append("\t\t\t");
				sb.append("- ").append(transaction.getAmount());
			}
			sb.append("\n");
		}
		System.out.println(sb);
	}

	private StatementType readStatementType() {
		StatementType type = null;
		while (type == null) {
			for (StatementType sType : StatementType.values()) {
				System.out.println(sType.number + " - "
						+ getTextManager().getText(sType.name()));
			}
			Integer option = UIUtils.INSTANCE.readInteger("statement.type");
			for (StatementType sType : StatementType.values()) {
				if (sType.number == option) {
					type = sType;
					break;
				}
			}
		}
		return type;
	}

	private void showMonthlyStatement(Long branch, Long accountNumber)
			throws Exception {
		Calendar cal = Calendar.getInstance();
		MonthYear[] possibilities = new MonthYear[NUMBER_OF_POSSIBLE_MONTHS];

		for (int i = 0; i < possibilities.length; i++) {
			cal.add(Calendar.MONTH, -1);
			MonthYear my = new MonthYear();
			my.month = cal.get(Calendar.MONTH);
			my.year = cal.get(Calendar.YEAR);
			possibilities[i] = my;
			System.out.println(i + 1 + " - " + my);
		}

		Integer option = UIUtils.INSTANCE.readInteger("message.select.month",
				1, possibilities.length);
		MonthYear chosenMY = possibilities[option - 1];
		List<Transaction> transactions = accountOperationService
				.getStatementByMonth(branch, accountNumber, chosenMY.month,
						chosenMY.year);
		printStatement(new CurrentAccountId(new Branch(branch), accountNumber),
				transactions);
	}

	private void showStatementByPeriod(Long branch, Long accountNumber)
			throws Exception {
		Date begin = UIUtils.INSTANCE.readDate("date.initial", true);
		Date end = UIUtils.INSTANCE.readDate("date.end", true);

		if (begin == null || end == null) {
			System.out.println(getTextManager().getText(
					"message.consider.last.30.days"));
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY,
					cal.getActualMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND,
					cal.getActualMaximum(Calendar.MILLISECOND));
			end = cal.getTime();

			cal.add(Calendar.DAY_OF_MONTH, -30);
			cal.set(Calendar.HOUR_OF_DAY,
					cal.getActualMinimum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getActualMinimum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getActualMinimum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND,
					cal.getActualMinimum(Calendar.MILLISECOND));
			begin = cal.getTime();
		}

		List<Transaction> transactions = accountOperationService
				.getStatementByDate(branch, accountNumber, begin, end);
		printStatement(new CurrentAccountId(new Branch(branch), accountNumber),
				transactions);
	}

}