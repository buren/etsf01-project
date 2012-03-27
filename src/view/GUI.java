package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import model.JSONDatabase;

import controller.EffortEstimation;

public class GUI {

	public GUI() {
		Frame frm = new Frame("Effort Estimation Calculator");
		// Label lbl = new Label("Please fill this blank:");
		// frm.add(lbl);
		frm.setSize(300, 300);
		frm.setVisible(true);
		frm.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		createLayout(frm);
		
	}

	public void createLayout(Frame frm) {
		ArrayList<Label> labelList = new ArrayList<Label>();
		ArrayList<TextField> textFieldList = new ArrayList<TextField>();
		Panel p = new Panel();
		Panel p1 = new Panel();
		p.setLayout(new GridLayout(4, 4));
		for (String textLabel : JSONDatabase.TYPES){
			p.add(new Label(textLabel));
			p.add(new TextField(5));
		}
		Button Submit = new Button("Submit");
		p.add(Submit);
		p1.add(p);
		frm.add(p1, BorderLayout.WEST);
		frm.setSize(800, 300);
	}
}
