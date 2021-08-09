/*
 * Created on 6 Jan 2014 20:12:43 
 */
package bank.ui.graphic;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bank.ui.TextManager;

/**
 * @author ingrid
 * 
 */
public class GUIUtils {

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy");
	public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");
	public static final GUIUtils INSTANCE = new GUIUtils();
	public static final String PROPERTY_RESOURCE_BUNDLE = "bank.resources.globalMessages";

	private final Log log;
	private final TextManager textManager;

	private GUIUtils() {
		this.log = LogFactory.getLog(getClass());
		this.textManager = new TextManager(PROPERTY_RESOURCE_BUNDLE);
	}

	public boolean checkMandatory(JFrame owner, Object obj, String fieldName) {
		if (obj == null) {
			showMessage(owner, "exception.field.mandatory",
					new String[] { fieldName }, JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public boolean checkMandatoryString(JFrame owner, String str,
			String fieldName) {
		if (str == null || "".equals(str)) {
			showMessage(owner, "exception.field.mandatory",
					new String[] { fieldName }, JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	public JDialog createDialog(Frame owner, String title, JPanel panel) {
		JDialog dialog = new JDialog(owner, textManager.getText(title), true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		dialog.setContentPane(panel);
		dialog.pack();
		Dimension locRef = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setLocation((locRef.width - dialog.getSize().width) / 2,
				(locRef.height - dialog.getSize().height) / 2);
		return dialog;
	}

	public void handleUnexceptedError(JFrame owner, Exception e) {
		showMessage(owner, "exception.unexpected", JOptionPane.ERROR_MESSAGE);
		log.error(e);
		e.printStackTrace();
		System.exit(-1);
	}

	public void showMessage(JFrame owner, String key, int type) {
		showMessage(owner, key, new String[0], type);
	}

	public void showMessage(JFrame owner, String key, String arg, int type) {
		showMessage(owner, key, new String[] { arg }, type);
	}

	public void showMessage(JFrame owner, String key, String[] args, int type) {
		showMessage("application.title", owner, key, args, type);
	}

	public void showMessage(String title, JFrame owner, String key, int type) {
		showMessage(title, owner, key, new String[0], type);
	}

	public void showMessage(String title, JFrame owner, String key, String arg,
			int type) {
		showMessage(title, owner, key, new String[] { arg }, type);
	}

	public void showMessage(String title, JFrame owner, String key,
			String[] args, int type) {
		JOptionPane.showMessageDialog(owner.getContentPane(),
				textManager.getText(key, args), textManager.getText(title),
				type);
	}

}
