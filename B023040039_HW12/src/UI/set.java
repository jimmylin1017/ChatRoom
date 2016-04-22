package UI;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import javax.swing.*;

public class set extends JFrame {
	private final int WIDTH = 300;
	private final int HEIGHT = 300;
	private JFrame setdata;
	private JLabel group;
	private JLabel username;
	private JLabel groupname;
	private JTextField inputname;
	private JTextField inputgroup;
	private JRadioButton join;
	private JRadioButton add;
	private ButtonGroup buttongroup;
	private JButton nextstep;
	private JTextArea grouplist;
	private Font font1 = new Font(Font.DIALOG, Font.BOLD, 20);

	public set(final client newclient) {
		setdata = new JFrame();
		group = new JLabel("current groups");
		grouplist = new JTextArea(5, 20);
		username = new JLabel("username");
		inputname = new JTextField(14);
		groupname = new JLabel("groupname");
		inputgroup = new JTextField(13);
		add = new JRadioButton("add");
		join = new JRadioButton("join");
		nextstep = new JButton("Continue");
		group.setFont(font1);
		add.setFont(font1);
		join.setFont(font1);
		buttongroup = new ButtonGroup();
		buttongroup.add(join);
		buttongroup.add(add);
		setdata.getContentPane().add(group);
		setdata.getContentPane().add(grouplist);
		setdata.getContentPane().add(username);
		setdata.getContentPane().add(inputname);
		setdata.getContentPane().add(groupname);
		setdata.getContentPane().add(inputgroup);
		setdata.getContentPane().add(add);
		setdata.getContentPane().add(join);
		setdata.getContentPane().add(nextstep);
		setdata.setSize(WIDTH, HEIGHT);
		setdata.setVisible(true);
		setdata.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setdata.setLayout(new FlowLayout());
		newclient.getgroup(grouplist);
		nextstep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((add.isSelected() || join.isSelected())
						&& inputname.getText() != ""
						&& inputgroup.getText() != "") {
					setdata.dispose();
					if (add.isSelected())
						newclient.setdata("1", inputname.getText(),
								inputgroup.getText());
					else if (join.isSelected())
						newclient.setdata("2", inputname.getText(),
								inputgroup.getText());
					newclient.chatroom.setVisible(true);
				}
			}
		});
	}
}

