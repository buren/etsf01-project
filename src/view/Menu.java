package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import conversion.Converter;

import java.util.*;

public class Menu extends JPanel implements ActionListener, MenuListener {
	
	private int unit = Converter.MONTHS;
	private String unitName = "Person-months";
	private JTextArea resultArea;

	public Menu(JFrame frm, JTextArea resultArea) {
		
		this.resultArea = resultArea;
		
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Unit");
		JMenuItem tmp;

		setBackground(Color.lightGray);
		setLayout(new BorderLayout());

		setDoubleBuffered(true);
		menu.addMenuListener(this);

		tmp = new JMenuItem("Person-hours");
		tmp.addActionListener(this);
		tmp.setActionCommand("Person-hours");
		menu.add(tmp);

		tmp = new JMenuItem("Person-days");
		tmp.addActionListener(this);
		tmp.setActionCommand("Person-days");
		menu.add(tmp);

		tmp = new JMenuItem("Person-months");
		tmp.addActionListener(this);
		tmp.setActionCommand("Person-months");
		menu.add(tmp);

		tmp = new JMenuItem("Person-years");
		tmp.addActionListener(this);
		tmp.setActionCommand("Person-years");
		menu.add(tmp);

		bar.add(menu);
		frm.setJMenuBar(bar);

	}

	public void actionPerformed(ActionEvent e) {
		unitName = e.getActionCommand();
		resultArea.setText("Result (in " + unitName + "):\n\n");
		if (unitName.equals("Person-years")) {
			unit = Converter.YEARS;
		} else if (unitName.equals("Person-months")) {
			unit = Converter.MONTHS;
		} else if (unitName.equals("Person-days")) {
			unit = Converter.DAYS;
		} else if (unitName.equals("Person-hours")) {
			unit = Converter.HOURS;
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(200, 100);
	}
	
	public int getUnit() {
		return unit;
	}
	
	public String getUnitName() {
		return unitName;
	}

	@Override
	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuSelected(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}