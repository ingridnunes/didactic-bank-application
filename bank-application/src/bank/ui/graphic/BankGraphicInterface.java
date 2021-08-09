package bank.ui.graphic;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bank.business.domain.OperationLocation;
import bank.ui.BankInterface;
import bank.ui.TextManager;
import bank.ui.graphic.action.BankAction;
import bank.ui.graphic.action.ExitAction;

/**
 * @author Ingrid Nunes
 * 
 */
public abstract class BankGraphicInterface extends BankInterface {

	public class WindowHandler extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			exitAction.actionPerformed(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, exitAction.getCommandKey()));
		}

	}

	private ExitAction exitAction;
	private final JFrame frame;
	private final JMenu mainMenu;
	private final JMenuBar menu;
	private final JPanel panel;
	private final TextManager textManager;

	public BankGraphicInterface(OperationLocation location,
			TextManager textManager) {
		super(location);
		this.frame = new JFrame();
		this.textManager = textManager;

		this.menu = new JMenuBar();
		this.mainMenu = new JMenu(textManager.getText("menu.main"));
		this.menu.add(mainMenu);

		this.panel = new JPanel(getLayoutManager());
		panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
	}

	protected void addAction(BankAction action) {
		this.addAction(action.getCommandKey(), action);
	}

	protected void addMenuAction(BankAction action) {
		mainMenu.add(new JMenuItem(action));
		addAction(action);
	}

	protected void addPanelAction(BankAction action) {
		panel.add(new JButton(action));
		addAction(action);
	}

	public void center() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((screen.width - frame.getSize().width) / 2,
				(screen.height - frame.getSize().height) / 2);
	}

	public void createAndShowUI() {
		frame.setTitle(textManager.getText("application.title") + " - "
				+ location);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowHandler());
		frame.setVisible(false);

		frame.setJMenuBar(menu);
		frame.setContentPane(panel);

		frame.pack();
		center();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
	}

	public ExitAction getExitAction() {
		return exitAction;
	}

	public JFrame getFrame() {
		return frame;
	}

	protected abstract GridLayout getLayoutManager();

	protected void setExitAction(ExitAction exitAction) {
		this.exitAction = exitAction;
	}

}
