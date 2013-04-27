package pl.mw.grawzycie;

import javax.swing.UIManager;


public class grawzycie {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			Display frame = new Display();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
