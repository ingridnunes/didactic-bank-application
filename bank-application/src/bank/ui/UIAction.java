/*
 * Created on 6 Jan 2014 21:28:25 
 */
package bank.ui;

/**
 * @author ingrid
 * 
 */
public interface UIAction {

	public abstract void execute() throws Exception;

	public boolean isEnabled();

	public void setEnabled(boolean isEnabled);

}
