/*
 * Created on 6 Jan 2014 16:08:22 
 */
package bank.data;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bank.business.domain.ATM;
import bank.business.domain.Branch;
import bank.business.domain.Client;
import bank.business.domain.CurrentAccount;
import bank.business.domain.CurrentAccountId;
import bank.business.domain.Employee;
import bank.business.domain.OperationLocation;
import bank.business.domain.Transaction;

/**
 * @author Ingrid Nunes
 * 
 */
public class Database {

	private final Map<CurrentAccountId, CurrentAccount> currentAccounts;
	private final Map<String, Employee> employees;
	private final Log log;
	private final Map<Long, OperationLocation> operationLocations;

	public Database() {
		this(true);
	}

	public Database(boolean initData) {
		this.log = LogFactory.getLog(getClass());
		this.operationLocations = new HashMap<>();
		this.employees = new HashMap<>();
		this.currentAccounts = new HashMap<>();
		if (initData) {
			initData();
		}
	}

	public Collection<CurrentAccount> getAllCurrentAccounts() {
		return this.currentAccounts.values();
	}

	public Collection<Employee> getAllEmployees() {
		return this.employees.values();
	}

	public Collection<OperationLocation> getAllOperationLocations() {
		return this.operationLocations.values();
	}

	public CurrentAccount getCurrentAccount(CurrentAccountId currentAccountId) {
		return currentAccounts.get(currentAccountId);
	}

	public Employee getEmployee(String username) {
		return employees.get(username);
	}

	public long getNextCurrentAccountNumber() {
		// I'm assuming that numbers are sequential and no deletions are
		// performed.
		return currentAccounts.size() + 1;
	}

	public OperationLocation getOperationLocation(long number) {
		return operationLocations.get(number);
	}

	private void initData() {
		try {
			// Operation Location
			int olId = 0;
			Branch b1 = new Branch(++olId, "Campus Vale");
			save(b1);
			Branch b2 = new Branch(++olId, "Centro");
			save(b2);
			ATM atm1 = new ATM(++olId);
			save(atm1);
			ATM atm2 = new ATM(++olId);
			save(atm2);
			ATM atm3 = new ATM(++olId);
			save(atm3);

			// Employee
			Employee employee = new Employee("Ingrid", "Nunes", "ingrid",
					"123", new Date());
			save(employee);

			// Current Accounts
			Client client1 = new Client("Ingrid", "Nunes", 1234567890, "123",
					new Date());
			CurrentAccount ca1 = new CurrentAccount(b1, 1l, client1, 300);
			save(ca1);
			Client client2 = new Client("Joao", "Silva", 1234567890, "123",
					new Date());
			CurrentAccount ca2 = new CurrentAccount(b2, 2l, client2, 200);
			save(ca2);
			Client client3 = new Client("Richer", "Rich", 1234567890, "123",
					new Date());
			CurrentAccount ca3 = new CurrentAccount(b2, 3l, client3, 10000);
			save(ca3);

			// Transactions
			Random r = new Random(System.currentTimeMillis());
			Calendar cal = Calendar.getInstance();
			for (int i = 0; i < 8; i++) {
				changeDate(
						ca1.deposit(b1, r.nextInt(10000), r.nextDouble() * 150),
						r, cal);
				changeDate(ca1.withdrawal(atm1, r.nextDouble() * 100), r, cal);
				changeDate(ca1.transfer(atm2, ca2, r.nextDouble() * 100), r,
						cal);

				changeDate(
						ca2.deposit(b2, r.nextInt(10000), r.nextDouble() * 150),
						r, cal);
				changeDate(ca2.withdrawal(atm2, r.nextDouble() * 100), r, cal);
				changeDate(ca2.transfer(atm3, ca1, r.nextDouble() * 100), r,
						cal);

				cal.add(Calendar.MONTH, -1);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

	private void changeDate(Transaction t, Random r, Calendar cal) {
		t.setDate(cal.getTime());
		cal.add(Calendar.DAY_OF_MONTH, -r.nextInt(5));
		cal.add(Calendar.HOUR_OF_DAY, r.nextInt(12));
		cal.add(Calendar.MINUTE, r.nextInt(30));
	}

	public void save(CurrentAccount currentAccount) {
		this.currentAccounts.put(currentAccount.getId(), currentAccount);
	}

	public void save(Employee employee) {
		this.employees.put(employee.getUsername(), employee);
	}

	public void save(OperationLocation operationLocation) {
		this.operationLocations.put(operationLocation.getNumber(),
				operationLocation);
	}

}
