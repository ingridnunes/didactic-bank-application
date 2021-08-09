/*
 * Created on 16 Dec 2013 16:15:46
 */
package bank.business;

import java.util.Date;
import java.util.List;

import bank.business.domain.CurrentAccount;
import bank.business.domain.Deposit;
import bank.business.domain.Transaction;
import bank.business.domain.Transfer;
import bank.business.domain.Withdrawal;

/**
 * @author Ingrid Nunes
 * 
 */
public interface AccountOperationService {

	public Deposit deposit(long operationLocation, long branch,
			long accountNumber, long envelope, double amount)
			throws BusinessException;

	public double getBalance(long branch, long accountNumber)
			throws BusinessException;

	public List<Transaction> getStatementByDate(long branch,
			long accountNumber, Date begin, Date end) throws BusinessException;

	public List<Transaction> getStatementByMonth(long branch,
			long accountNumber, int month, int year) throws BusinessException;

	public CurrentAccount login(long branch, long accountNumber, String password)
			throws BusinessException;

	public Transfer transfer(long operationLocation, long srcBranch,
			long srcAccountNumber, long dstBranch, long dstAccountNumber,
			double amount) throws BusinessException;

	public Withdrawal withdrawal(long operationLocation, long branch,
			long accountNumber, double amount) throws BusinessException;

}
