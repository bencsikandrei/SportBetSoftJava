package dev4a.graphicalview;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.*;

import dev4a.competition.Competition;
import dev4a.competitor.Competitor;
import dev4a.db.SubscribersManager;
import dev4a.subscriber.Subscriber;
import dev4a.system.BettingSystem;

public class GUIBettingSoft extends JFrame {
	/* create a passDialog object to launch the gui */
	private PassWordDialog passDialog;
	private AddDialog addDialog;
	final BettingSystem bettingSystem;
	/* constructor */
	public GUIBettingSoft(BettingSystem bettingSystem) {
		this.bettingSystem = bettingSystem;

		passDialog = new PassWordDialog(this, true, bettingSystem.getPassword());

		JPanel theContainer = new JPanel(new GridBagLayout());
		theContainer.add(new SubscribersInterface(bettingSystem));
		theContainer.add(new CompetitionsInterface(bettingSystem));
		/* make it visible */

		setContentPane(theContainer);
		passDialog.setVisible(true);


	}

}

class SubscribersInterface extends JPanel {
	/* */
	private final BettingSystem bettingSystem;
	private java.util.List<java.util.List<String>> subs;

	JList subscribers;

	public SubscribersInterface(BettingSystem bettingSys) {
		this.bettingSystem = bettingSys;
		try {
			subs = bettingSystem.listSubscribers(bettingSystem.getPassword());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		setLayout(new BorderLayout());
		
		JButton buttonAdd = new JButton("Add");
		
		buttonAdd.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AddDialog addSub = new AddDialog(true, bettingSystem);
				addSub.setVisible(true);
				
			}
		});
		
		JButton buttonDelete = new JButton("Delete");


		subscribers = new JList<>(subs.toArray());
		subscribers.setCellRenderer(new SubscriberCellRender());
		subscribers.setVisibleRowCount(12);
		JScrollPane pane = new JScrollPane(subscribers);

		JPanel buttonsContainer = new JPanel(new GridLayout(1,2));

		buttonsContainer.add(buttonAdd);
		buttonsContainer.add(buttonDelete);

		add(pane, BorderLayout.NORTH);
		add(buttonsContainer, BorderLayout.SOUTH);
		//add(buttonDelete, BorderLayout.SOUTH);
	}


	class SubscriberCellRender extends JLabel implements ListCellRenderer {
		private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

		public SubscriberCellRender() {
			setOpaque(true);
			setIconTextGap(12);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			java.util.List entry = (java.util.List<String>)value;
			setText((String)entry.get(0));

			if (isSelected) {
				setBackground(HIGHLIGHT_COLOR);
				setForeground(Color.white);
			} else {
				setBackground(Color.white);
				setForeground(Color.black);
			}
			return this;
		}
	}

}

class CompetitionsInterface extends JPanel {
	/* */
	private BettingSystem bettingSystem;
	private java.util.List<java.util.List<String>> comps;

	JList competitions;
	public CompetitionsInterface(BettingSystem bettingSystem) {
		this.bettingSystem = bettingSystem;
		try {
			comps = bettingSystem.listCompetitions();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		setLayout(new BorderLayout());
		JButton buttonAdd = new JButton("Add");
		JButton buttonDelete = new JButton("Delete");
		JButton buttonShowCompetitors = new JButton("Show competitors");

		competitions = new JList(comps.toArray());
		competitions.setCellRenderer(new CompetitionsCellRender());
		competitions.setVisibleRowCount(12);
		JScrollPane pane = new JScrollPane(competitions);
		
		JPanel buttonsContainer = new JPanel(new GridLayout(1,3));
		
		buttonsContainer.add(buttonAdd);
		buttonsContainer.add(buttonDelete);
		buttonsContainer.add(buttonShowCompetitors);
		
		
		add(pane, BorderLayout.NORTH);
		add(buttonsContainer, BorderLayout.SOUTH);
		
	}


	class CompetitionsCellRender extends JLabel implements ListCellRenderer {
		private final Color HIGHLIGHT_COLOR = new Color(0, 0, 128);

		public CompetitionsCellRender() {
			setOpaque(true);
			setIconTextGap(12);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			java.util.List entry = (java.util.List<String>)value;
			setText((String)entry.get(0));

			if (isSelected) {
				setBackground(HIGHLIGHT_COLOR);
				setForeground(Color.white);
			} else {
				setBackground(Color.white);
				setForeground(Color.black);
			}
			return this;
		}
	}

}

class AddDialog extends JDialog {
	/* the two labels */
	private final JLabel jlblUsername = new JLabel("Username");
	private final JLabel jlblFirstName = new JLabel("First Name");
	private final JLabel jlblLastName = new JLabel("Last Name");
	private final JLabel jlblBornDate = new JLabel("Born Date");
	
	/* the betting system */
	private final BettingSystem bettingSystem;
	/* the user name */
	private final JTextField jtfUsername = new JTextField(15);
	/* the user name */
	private final JTextField jtfFirstName = new JTextField(15);
	/* the user name */
	private final JTextField jtfLastName = new JTextField(15);
	/* the user name */
	private final JTextField jtfBornDate = new JTextField(15);
	/* login button */
	private final JButton jbtOk = new JButton("Login");
	/* for the outcome of the login */
	private final JLabel jlblStatus = new JLabel(" ");
	/* default constructor */
	public AddDialog() {
		this(true, null);
	}
	/* the proper constructor */
	public AddDialog(boolean modal, BettingSystem bettingSys) {
		
		//this.expectedPassword = password;
		this.bettingSystem = bettingSys;
		/* first part */
		JPanel labels = new JPanel(new GridLayout(4, 1));
		labels.add(jlblUsername);
		labels.add(jlblFirstName);
		labels.add(jlblLastName);
		labels.add(jlblBornDate);
				

		JPanel textFields = new JPanel(new GridLayout(4, 1));
		textFields.add(jtfUsername);
		textFields.add(jtfFirstName);
		textFields.add(jtfLastName);
		textFields.add(jtfBornDate);
		

		JPanel containerForText = new JPanel();
		containerForText.add(labels);
		containerForText.add(textFields);

		JPanel theButtons = new JPanel();
		theButtons.add(jbtOk);
		//p2.add(jbtCancel);

		JPanel theWhole = new JPanel(new BorderLayout());
		theWhole.add(theButtons, BorderLayout.CENTER);
		theWhole.add(jlblStatus, BorderLayout.NORTH);

		jlblStatus.setForeground(Color.RED);
		jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);

		setLayout(new BorderLayout());
		this.setTitle("Add");

		add(containerForText, BorderLayout.CENTER);
		add(theWhole, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		/* listen on close */
		addWindowListener(new WindowAdapter() {  
			@Override
			public void windowClosing(WindowEvent e) {  
				
				setVisible(false);  
			}  
		});
		/* press OK and try to login */
		jbtOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					bettingSystem.subscribe(jtfLastName.getText(), 
							jtfFirstName.getText(), 
							jtfUsername.getText(), 
							jtfBornDate.getText(), 
							bettingSystem.getPassword());
					setVisible(false);
				} catch(Exception ex ) {
					jlblStatus.setText("Something went wrong!");
					ex.printStackTrace();
				}
			}
		});
		
	}
}
class PassWordDialog extends JDialog {
	/* the pass to log in */
	private final String expectedPassword;
	/* the two labels */
	private final JLabel jlblUsername = new JLabel("Username");
	private final JLabel jlblPassword = new JLabel("Password");
	/* the user name */
	private final JTextField jtfUsername = new JTextField(15);
	/* and the password */
	private final JPasswordField jpfPassword = new JPasswordField();
	/* login button */
	private final JButton jbtOk = new JButton("Login");
	private final JButton jbtCancel = new JButton("Cancel");
	/* for the outcome of the login */
	private final JLabel jlblStatus = new JLabel(" ");
	/* default constructor */
	public PassWordDialog() {
		this(null, true, "");
	}
	/* the proper constructor */
	public PassWordDialog(final JFrame parent, boolean modal, String password) {
		super(parent, modal);
		this.expectedPassword = password;

		/* first part */
		JPanel labels = new JPanel(new GridLayout(2, 1));
		labels.add(jlblUsername);
		labels.add(jlblPassword);

		JPanel textFields = new JPanel(new GridLayout(2, 1));
		textFields.add(jtfUsername);
		textFields.add(jpfPassword);

		JPanel containerForText = new JPanel();
		containerForText.add(labels);
		containerForText.add(textFields);

		JPanel theButtons = new JPanel();
		theButtons.add(jbtOk);
		//p2.add(jbtCancel);

		JPanel theWhole = new JPanel(new BorderLayout());
		theWhole.add(theButtons, BorderLayout.CENTER);
		theWhole.add(jlblStatus, BorderLayout.NORTH);

		jlblStatus.setForeground(Color.RED);
		jlblStatus.setHorizontalAlignment(SwingConstants.CENTER);

		setLayout(new BorderLayout());
		this.setTitle("Log in");

		add(containerForText, BorderLayout.CENTER);
		add(theWhole, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		/* listen on close */
		addWindowListener(new WindowAdapter() {  
			@Override
			public void windowClosing(WindowEvent e) {  
				java.lang.System.exit(0);  
			}  
		});
		/* press OK and try to login */
		jbtOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (expectedPassword.equals(jpfPassword.getText())
						&& "root".equals(jtfUsername.getText())) {
					parent.setVisible(true);
					setVisible(false);
				} else {
					jlblStatus.setText("Invalid username or password");
				}
			}
		});
		/* if cancel is pressed, get rid of everything */
		jbtCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				parent.dispose();
				java.lang.System.exit(0);
			}
		});
	}
}