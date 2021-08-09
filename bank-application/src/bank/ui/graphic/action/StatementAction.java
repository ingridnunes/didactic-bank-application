/*
 * Created on 6 Jan 2014 21:13:57 
 */
package bank.ui.graphic.action;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;

import bank.business.AccountOperationService;
import bank.business.BusinessException;
import bank.business.domain.Branch;
import bank.business.domain.CurrentAccountId;
import bank.business.domain.Deposit;
import bank.business.domain.Transaction;
import bank.business.domain.Transfer;
import bank.business.domain.Withdrawal;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

/**
 * @author ingrid
 * 
 */
public class StatementAction extends AccountAbstractAction {

	private class MonthYear {
		int month;
		int year;

		@Override
		public String toString() {
			return textManager.getText("month." + month) + "/" + year;
		}
	}

	public enum StatementType {
		MONTHLY, PERIOD;
	}

	private class StatementTypeListner implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.show(cards, e.getActionCommand());
			type = StatementType.valueOf(e.getActionCommand());
		}
	}

	private class TransactionTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 2497950520925208080L;

		private CurrentAccountId id;
		private List<Transaction> transactions;

		public TransactionTableModel(CurrentAccountId id,
				List<Transaction> transactions) {
			this.id = id;
			this.transactions = new ArrayList<>(transactions);
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public String getColumnName(int column) {
			String key = null;
			switch (column) {
			case 0:
				key = "date";
				break;
			case 1:
				key = "location";
				break;
			case 2:
				key = "operation.type";
				break;
			case 3:
				key = "details";
				break;
			case 4:
				key = "amount";
				break;
			default:
				assert false;
				break;
			}
			return textManager.getText(key);
		}

		@Override
		public int getRowCount() {
			return transactions.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Transaction t = transactions.get(rowIndex);
			Object val = null;
			switch (columnIndex) {
			case 0:
				val = GUIUtils.DATE_TIME_FORMAT.format(t.getDate());
				break;
			case 1:
				val = t.getLocation();
				break;
			case 2:
				val = textManager.getText("operation."
						+ t.getClass().getSimpleName());
				break;
			case 3:
				if (t instanceof Deposit) {
					val = ((Deposit) t).getEnvelope();
				} else if (t instanceof Transfer) {
					Transfer transfer = (Transfer) t;
					StringBuffer sb = new StringBuffer();
					CurrentAccountId otherId = transfer.getAccount().getId()
							.equals(id) ? transfer.getDestinationAccount()
							.getId() : transfer.getAccount().getId();
					sb.append("AG ").append(otherId.getBranch().getNumber())
							.append(" C/C ").append(otherId.getNumber());
					val = sb.toString();
				} else if (t instanceof Withdrawal) {
					val = "";
				} else {
					assert false;
				}
				break;
			case 4:
				if (t instanceof Deposit) {
					val = "+ " + t.getAmount();
				} else if (t instanceof Transfer) {
					Transfer transfer = (Transfer) t;
					if (transfer.getAccount().getId().equals(id)) {
						val = "- " + t.getAmount();
					} else {
						val = "+ " + t.getAmount();
					}
				} else if (t instanceof Withdrawal) {
					val = "- " + t.getAmount();
				} else {
					assert false;
				}
				break;

			default:
				assert false;
				break;
			}
			return val;
		}

	}

	private static final int NUMBER_OF_POSSIBLE_MONTHS = 6;

	private static final long serialVersionUID = 5090183202921964451L;

	private JFormattedTextField beginDate;
	private JPanel cards;
	private JDialog dialog;
	private JFormattedTextField endDate;
	private JComboBox<MonthYear> month;
	private JTable transactions;
	private StatementType type;

	public StatementAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountOperationService accountOperationService) {
		super(bankInterface, textManager, accountOperationService);

		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		putValue(Action.NAME, textManager.getText("action.statement"));
	}

	public void close() {
		dialog.dispose();
		dialog = null;
	}

	private JRadioButton createRadioButton(StatementType type,
			ButtonGroup btGroup, ActionListener al) {
		JRadioButton bt = new JRadioButton(textManager.getText(type.name()));
		bt.setActionCommand(type.name());
		bt.addActionListener(al);
		btGroup.add(bt);
		return bt;
	}

	@Override
	public void execute() {
		JPanel accountPanel = new JPanel(new GridLayout(2, 2, 5, 5));
		initAndAddAccountFields(accountPanel);

		// Cards
		JPanel radioBtPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		this.cards = new JPanel(new CardLayout());
		ButtonGroup btGroup = new ButtonGroup();
		ActionListener al = new StatementTypeListner();

		// Monthly Statement Panel
		JRadioButton btM = createRadioButton(StatementType.MONTHLY, btGroup, al);
		radioBtPanel.add(btM);
		JPanel monthlyPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		this.month = new JComboBox<>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < NUMBER_OF_POSSIBLE_MONTHS; i++) {
			cal.add(Calendar.MONTH, -1);
			MonthYear my = new MonthYear();
			my.month = cal.get(Calendar.MONTH);
			my.year = cal.get(Calendar.YEAR);
			month.addItem(my);
		}
		monthlyPanel.add(new JLabel(textManager.getText("month") + ":"));
		monthlyPanel.add(month);
		cards.add(monthlyPanel, StatementType.MONTHLY.name());

		// Statement by Period Panel
		JRadioButton btP = createRadioButton(StatementType.PERIOD, btGroup, al);
		radioBtPanel.add(btP);
		JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		this.beginDate = new JFormattedTextField(GUIUtils.DATE_FORMAT);
		beginDate.setColumns(10);
		beginDate.setToolTipText(GUIUtils.DATE_FORMAT.toPattern());
		periodPanel.add(new JLabel(textManager.getText("date.initial") + ":"));
		periodPanel.add(beginDate);
		this.endDate = new JFormattedTextField(GUIUtils.DATE_FORMAT);
		endDate.setColumns(10);
		endDate.setToolTipText(GUIUtils.DATE_FORMAT.toPattern());
		periodPanel.add(new JLabel(textManager.getText("date.end") + ":"));
		periodPanel.add(endDate);
		cards.add(periodPanel, StatementType.PERIOD.name());

		JPanel cardsPanel = new JPanel();
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.PAGE_AXIS));
		cardsPanel.add(accountPanel);
		cardsPanel.add(radioBtPanel);
		cardsPanel.add(cards);

		// Confirmation Buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cancelButton = new JButton(textManager.getText("button.close"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});
		buttonsPanel.add(cancelButton);
		JButton okButton = new JButton(textManager.getText("button.ok"));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				switch (type) {
				case MONTHLY:
					showMonthlyStatement();
					break;
				case PERIOD:
					showStatementByPeriod();
					break;
				}
			}
		});
		buttonsPanel.add(okButton);

		// Statement result
		JPanel transactionsPanel = new JPanel();
		transactionsPanel
				.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		transactions = new JTable();
		JScrollPane scrollPane = new JScrollPane(transactions,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		transactionsPanel.add(scrollPane);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(cardsPanel, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(mainPanel, BorderLayout.NORTH);
		pane.add(transactionsPanel, BorderLayout.CENTER);

		btM.doClick();

		this.dialog = GUIUtils.INSTANCE.createDialog(bankInterface.getFrame(),
				"action.statement", pane);
		this.dialog.setVisible(true);
	}

	private void showMonthlyStatement() {
		try {
			if (!checkAccountFields())
				return;
			MonthYear my = (MonthYear) month.getSelectedItem();

			List<Transaction> transactions = accountOperationService
					.getStatementByMonth(
							((Number) branch.getValue()).longValue(),
							((Number) accountNumber.getValue()).longValue(),
							my.month, my.year);
			this.transactions.setModel(new TransactionTableModel(
					new CurrentAccountId(new Branch(
							((Number) branch.getValue()).longValue()),
							((Number) accountNumber.getValue()).longValue()),
					transactions));
		} catch (BusinessException be) {
			GUIUtils.INSTANCE.showMessage(bankInterface.getFrame(),
					be.getMessage(), be.getArgs(), JOptionPane.WARNING_MESSAGE);
			log.warn(be);
		} catch (Exception exc) {
			GUIUtils.INSTANCE.handleUnexceptedError(bankInterface.getFrame(),
					exc);
		}
	}

	private void showStatementByPeriod() {

		try {
			if (!checkAccountFields())
				return;

			Date begin = (Date) beginDate.getValue();
			Date end = (Date) endDate.getValue();

			if (begin == null || end == null) {
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
					.getStatementByDate(
							((Number) branch.getValue()).longValue(),
							((Number) accountNumber.getValue()).longValue(),
							begin, end);
			this.transactions.setModel(new TransactionTableModel(
					new CurrentAccountId(new Branch(
							((Number) branch.getValue()).longValue()),
							((Number) accountNumber.getValue()).longValue()),
					transactions));
		} catch (BusinessException be) {
			GUIUtils.INSTANCE.showMessage(bankInterface.getFrame(),
					be.getMessage(), be.getArgs(), JOptionPane.WARNING_MESSAGE);
			log.warn(be);
		} catch (Exception exc) {
			GUIUtils.INSTANCE.handleUnexceptedError(bankInterface.getFrame(),
					exc);
		}
	}

}
