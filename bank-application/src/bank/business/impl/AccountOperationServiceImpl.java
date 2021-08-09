/*
 * Created on 5 Jan 2014 00:51:50 
 */
package bank.business.impl;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import bank.business.AccountOperationService;
import bank.business.BusinessException;
import bank.business.domain.Branch;
import bank.business.domain.CurrentAccount;
import bank.business.domain.CurrentAccountId;
import bank.business.domain.Deposit;
import bank.business.domain.OperationLocation;
import bank.business.domain.Transaction;
import bank.business.domain.Transfer;
import bank.business.domain.Withdrawal;
import bank.data.Database;

/**
 * @author Ingrid Nunes
 * 
 */
public class AccountOperationServiceImpl implements AccountOperationService {

	private final Database database;

	public AccountOperationServiceImpl(Database database) {
		this.database = database;
	}

	@Override
	public Deposit deposit(long operationLocation, long branch,
			long accountNumber, long envelope, double amount)
			throws BusinessException {
		CurrentAccount currentAccount = readCurrentAccount(branch,
				accountNumber);
		Deposit deposit = currentAccount.deposit(
				getOperationLocation(operationLocation), envelope, amount);
		return deposit;
	}

	@Override
	public double getBalance(long branch, long accountNumber)
			throws BusinessException {
		return readCurrentAccount(branch, accountNumber).getBalance();
	}

	private OperationLocation getOperationLocation(long operationLocationNumber)
			throws BusinessException {
		OperationLocation operationLocation = database
				.getOperationLocation(operationLocationNumber);
		if (operationLocation == null) {
			throw new BusinessException("exception.invalid.operation.location");
		}
		return operationLocation;
	}

	private List<Transaction> getStatementByDate(CurrentAccount currentAccount,
			Date begin, Date end) {
		List<Transaction> selectedTransactions = new LinkedList<>();

		for (Transaction transaction : currentAccount.getTransactions()) {
			if (transaction.getDate().before(begin)
					|| transaction.getDate().after(end))
				continue;
			else
				selectedTransactions.add(transaction);
		}

		Collections.sort(selectedTransactions, new Comparator<Transaction>() {
			@Override
			public int compare(Transaction o1, Transaction o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		return selectedTransactions;
	}

	@Override
	public List<Transaction> getStatementByDate(long branch,
			long accountNumber, Date begin, Date end) throws BusinessException {
		return getStatementByDate(readCurrentAccount(branch, accountNumber),
				begin, end);
	}

	@Override
	public List<Transaction> getStatementByMonth(long branch,
			long accountNumber, int month, int year) throws BusinessException {
		// First Day
		Calendar firstDay = Calendar.getInstance();
		firstDay.set(Calendar.DAY_OF_MONTH, 1);
		firstDay.set(Calendar.MONTH, month);
		firstDay.set(Calendar.YEAR, year);
		firstDay.set(Calendar.HOUR_OF_DAY,
				firstDay.getActualMinimum(Calendar.HOUR_OF_DAY));
		firstDay.set(Calendar.MINUTE,
				firstDay.getActualMinimum(Calendar.MINUTE));
		firstDay.set(Calendar.SECOND,
				firstDay.getActualMinimum(Calendar.SECOND));
		firstDay.set(Calendar.MILLISECOND,
				firstDay.getActualMinimum(Calendar.MILLISECOND));

		// Last Day
		Calendar lastDay = Calendar.getInstance();
		lastDay.setTime(firstDay.getTime());
		lastDay.add(Calendar.MONTH, 1);
		lastDay.add(Calendar.MILLISECOND, -1);

		return getStatementByDate(readCurrentAccount(branch, accountNumber),
				firstDay.getTime(), lastDay.getTime());
	}

	@Override
	public CurrentAccount login(long branch, long accountNumber, String password)
			throws BusinessException {
		CurrentAccount currentAccount = readCurrentAccount(branch,
				accountNumber);
		if (!currentAccount.getClient().getPassword().equals(password)) {
			throw new BusinessException("exception.invalid.password");
		}

		return currentAccount;
	}

	private CurrentAccount readCurrentAccount(long branch, long accountNumber)
			throws BusinessException {
		CurrentAccountId id = new CurrentAccountId(new Branch(branch),
				accountNumber);
		CurrentAccount currentAccount = database.getCurrentAccount(id);

		if (currentAccount == null) {
			throw new BusinessException("exception.inexistent.account");
		}

		return currentAccount;
	}

	@Override
	public Transfer transfer(long operationLocation, long srcBranch,
			long srcAccountNumber, long dstBranch, long dstAccountNumber,
			double amount) throws BusinessException {
		CurrentAccount source = readCurrentAccount(srcBranch, srcAccountNumber);
		CurrentAccount destination = readCurrentAccount(dstBranch,
				dstAccountNumber);
		Transfer transfer = source.transfer(
				getOperationLocation(operationLocation), destination, amount);
		return transfer;
	}

	@Override
	public Withdrawal withdrawal(long operationLocation, long branch,
			long accountNumber, double amount) throws BusinessException {
		CurrentAccount currentAccount = readCurrentAccount(branch,
				accountNumber);
		Withdrawal withdrawal = currentAccount.withdrawal(
				getOperationLocation(operationLocation), amount);
		return withdrawal;
	}

}
