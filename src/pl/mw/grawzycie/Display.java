package pl.mw.grawzycie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


public class Display extends JFrame {

	private static final long serialVersionUID = 1L;
	private Plansza panel_1 = null;
	private JPanel contentPane;
	private String[] pedzle = new String[] {"Pixel", "Niezmiennik", "Oscylator", "Glider"};
	private String[] bcs = new String[] { "Łączenie", "Zanikanie", "Odbijanie" };

	public Display() {
		System.out.println("Display::Display();");
		setForeground(UIManager.getColor("Button.highlight"));
		setBackground(Color.WHITE);
		setTitle("Modelowanie Wielkoskalowe - Gra w życie");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 657, 648);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

			// =================================	pasek	================================
		
		JLabel lblWarunkiBrzegowe = new JLabel("BC:");
		panel.add(lblWarunkiBrzegowe);

		final JComboBox comboBox = new JComboBox();
		comboBox.setToolTipText("Warunki Brzegowe");
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Core.Config.bc = comboBox.getSelectedIndex();
				//System.out.println("Wybrano bc: "+Core.Config.bc);
			}
		});
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setModel(new DefaultComboBoxModel(this.bcs));
		panel.add(comboBox);
		
		JLabel lblPdzel = new JLabel("    Pędzel: ");
		panel.add(lblPdzel);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent argP) {
				Core.Config.pedzel = comboBox_1.getSelectedIndex();
				//System.out.println("Wybrano pedzel: "+Core.Config.pedzel+" ");
			}
		});
		comboBox_1.setBackground(new Color(255, 255, 255));
		comboBox_1.setModel(new DefaultComboBoxModel(this.pedzle));
		panel.add(comboBox_1);
		
		// =================================	generuj grafike 	================================

		panel_1 = new Plansza();
		panel_1.setBackground(SystemColor.text);
		contentPane.add(panel_1, BorderLayout.CENTER);

	    new Thread(panel_1).start();  

		// =================================	akcja generowania 	================================
	    this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	panel_1.refresh();
            }
        });
	    
		JButton btnGeneruj = new JButton("START");
		btnGeneruj.setBackground(new Color(173, 255, 47));
		btnGeneruj.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Core.Config.StatusStart = 1;
			}
		});
		
		JLabel label = new JLabel("      ");
		panel.add(label);
		panel.add(btnGeneruj);
		
		JButton btnKoniec = new JButton("STOP");
		btnKoniec.setBackground(new Color(255, 69, 0));
		btnKoniec.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Core.Config.StatusStart = 0;
			}
		});
		panel.add(btnKoniec);
		
		JButton btnReset = new JButton("RESET");
		btnReset.setBackground(new Color(255, 255, 255));
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Display.this.panel_1.clean();
				Display.this.panel_1.refresh();
			}
		});
		panel.add(btnReset);
	}

}
