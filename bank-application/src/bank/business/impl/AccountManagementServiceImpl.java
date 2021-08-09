/*
 * Created on 5 Jan 2014 00:51:19 
 */
package bank.business.impl;

import java.util.Date;

import bank.business.AccountManagementService;
import bank.business.BusinessException;
import bank.business.domain.Branch;
import bank.business.domain.Client;
import bank.business.domain.CurrentAccount;
import bank.business.domain.Employee;
import bank.business.domain.OperationLocation;
import bank.data.Database;
import bank.util.RandomString;

/**
 * @author Ingrid Nunes
 * 
 */
public class AccountManagementServiceImpl implements AccountManagementService {

	private final Database database;
	private RandomString random;

	public AccountManagementServiceImpl(Database database) {
		this.database = database;
		this.random = new RandomString(8);
	}

	@Override
	public CurrentAccount createCurrentAccount(long branch, String name,
			String lastName, int cpf, Date birthday, double balance)
			throws BusinessException {
		OperationLocation operationLocation = database
				.getOperationLocation(branch);
		if (operationLocation == null || !(operationLocation instanceof Branch)) {
			throw new BusinessException("exception.invalid.branch");
		}

		Client client = new Client(name, lastName, cpf, random.nextString(),
				birthday);
		CurrentAccount currentAccount = new CurrentAccount(
				(Branch) operationLocation,
				database.getNextCurrentAccountNumber(), client, balance);

		database.save(currentAccount);

		return currentAccount;
	}

	@Override
	public Employee login(String username, String password)
			throws BusinessException {
		Employee employee = database.getEmployee(username);

		if (employee == null) {
			throw new BusinessException("exception.inexistent.employee");
		}
		if (!employee.getPassword().equals(password)) {
			throw new BusinessException("exception.invalid.password");
		}

		return employee;
	}

}
