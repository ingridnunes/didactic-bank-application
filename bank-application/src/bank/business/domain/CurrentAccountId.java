/*
 * Created on 5 Jan 2014 01:27:41 
 */
package bank.business.domain;

import java.io.Serializable;

/**
 * @author Ingrid Nunes
 * 
 */
public class CurrentAccountId implements Serializable {

	private static final long serialVersionUID = -8191000613135929256L;

	private Branch branch;
	private long number;

	public CurrentAccountId(Branch branch, long number) {
		this.branch = branch;
		this.number = number;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CurrentAccountId other = (CurrentAccountId) obj;
		if (branch == null) {
			if (other.branch != null)
				return false;
		} else if (!branch.equals(other.branch))
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	/**
	 * @return the branch
	 */
	public Branch getBranch() {
		return branch;
	}

	/**
	 * @return the number
	 */
	public long getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((branch == null) ? 0 : branch.hashCode());
		result = prime * result + (int) (number ^ (number >>> 32));
		return result;
	}

}
