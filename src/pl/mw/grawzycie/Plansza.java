package pl.mw.grawzycie;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class Plansza extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	private int pixelSize = 20;
	private int width = 570;
	private int height = 510;
	private int tabSizeX = this.width / this.pixelSize;
	private int tabSizeY = this.height / this.pixelSize;
	private P[][] P = new P[this.tabSizeX][this.tabSizeY];

	public Plansza() {

		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				this.P[i][j] = new P();
			}
		}

		// this.setBackground(SystemColor.text);
		// setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		setPreferredSize(new Dimension(500, 500));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int x = arg0.getX();
				int y = arg0.getY();
				Plansza.this.clickPixel(x, y);
				Plansza.this.refresh();
			}
		});

	}

	public void run() {
		System.out.println("Odpalam Plansza! ");
		while (true) {
			if (Core.Config.StatusStart == 1) {
				this.reverse(); // P: prev = act
				this.refresh();
				System.out.print(".");
			}
			try {
				Thread.sleep(Core.Config.delay);
			} catch (Exception ex) {
			}
		}
	}

	public void refresh() {
		int w = this.width;
		int h = this.height;

		if (this.getHeight() > 0)
			h = this.getHeight();
		if (this.getWidth() > 0)
			w = this.getWidth();

		this.tabSizeX = this.width / this.pixelSize;
		this.tabSizeY = this.height / this.pixelSize;

		if (h != this.height || w != this.width) { // zmiana rozmiary planszy -
													// przebuduj tab
			System.out.print("Zmieniam rozmiar planszy z: " + this.tabSizeX
					+ " x " + this.tabSizeY + ";"); // --------

			P[][] P_new = new P[w / this.pixelSize][h / this.pixelSize];
			for (int i = 0; i < w / this.pixelSize; i++) {
				for (int j = 0; j < h / this.pixelSize; j++) {
					P_new[i][j] = new P();
				}
			}
			// foreach old array
			for (int i = 0; i < this.tabSizeX; i++) {
				for (int j = 0; j < this.tabSizeY; j++) {
					if (i >= (w / this.pixelSize) || j >= (h / this.pixelSize))
						continue;
					P_new[i][j] = this.P[i][j];
				}
			}

			// set new size
			this.width = w;
			this.height = h;
			this.tabSizeX = this.width / this.pixelSize;
			this.tabSizeY = this.height / this.pixelSize;

			System.out.print("na: " + this.tabSizeX + " x " + this.tabSizeY
					+ "; \n"); // -------------------------
			// odswiez tab
			this.P = P_new;
		}

		// ===== iteracja =========

		if (Core.Config.StatusStart == 1) { // run
			for (int i = 0; i <= this.tabSizeX - 1; i++) {
				for (int j = 0; j <= this.tabSizeY - 1; j++) {
					this.P[i][j].act = this.makeAct(i, j);
				}
			}
		} // run

		// ===== rysuj =========
		repaint();
	}

	private int makeAct(int x, int y) {
		int[] s = { 0, 0, 0, 0, 0, 0, 0, 0 };
		/*
		 * 0 - lewa górna; 1 - centralna górna; 2 - prawa górna; 3 - prawa
		 * środkowa 4 - prawa dolna; 5 - centralna dolna; 6 - lewa dolna; 7 -
		 * lewa środkowa
		 */
		int left_x = x - 1;
		int right_x = x + 1;
		int top_y = y - 1;
		int bottom_y = y + 1;

		int s_ile = 0;
		
		int bc = Core.Config.bc-1;

		// warunki brzegowe - skoryguj X i Y [periodycznosc lub wygasania/odbijanie]
		if (Core.Config.bc == 0) {
			if (left_x < 0)
				left_x = this.tabSizeX - 1;
			if (right_x > this.tabSizeX - 1)
				right_x = 0;

			if (top_y < 0)
				top_y = this.tabSizeY - 1;
			else if (bottom_y > this.tabSizeY - 1)
				bottom_y = 0;
		} else {
			if (left_x < 0) {
				if (top_y < 0)
					s[0] = bc;
				else if (bottom_y > this.tabSizeY - 1)
					s[6] = bc;
				s[7] = bc;
			}
			if (right_x > this.tabSizeX - 1) {
				if (top_y < 0)
					s[2] = bc;
				else if (bottom_y > this.tabSizeY - 1)
					s[4] = bc;
				s[3] = bc;
			}
			if (top_y < 0)
				s[1] = bc;
			if (bottom_y > this.tabSizeY - 1)
				s[5] = bc;
		}

		// uzupełnij tabele sasiadów
		if (left_x >= 0 && top_y >= 0)
			s[0] = this.P[left_x][top_y].prev;
		if (top_y >= 0)
			s[1] = this.P[x][top_y].prev;
		if (top_y >= 0 && right_x <= this.tabSizeX - 1)
			s[2] = this.P[right_x][top_y].prev;
		if (right_x <= this.tabSizeX - 1)
			s[3] = this.P[right_x][y].prev;
		if (bottom_y <= this.tabSizeY - 1 && right_x <= this.tabSizeX - 1)
			s[4] = this.P[right_x][bottom_y].prev;
		if (bottom_y <= this.tabSizeY - 1)
			s[5] = this.P[x][bottom_y].prev;
		if (left_x >= 0 && bottom_y <= this.tabSizeY - 1)
			s[6] = this.P[left_x][bottom_y].prev;
		if (left_x >= 0)
			s[7] = this.P[left_x][y].prev;

		// policz zywych sasiadów
		s_ile = s[0] + s[1] + s[2] + s[3] + s[4] + s[5] + s[6] + s[7];
		
		if (this.P[x][y].prev == 0 && s_ile == 3)
			return 1;
		else if (this.P[x][y].prev == 1 && (s_ile == 2 || s_ile == 3))
			return 1;
		else
			return 0;
	}

	private void clickPixel(int x, int y) {
		int pixel_x = x / this.pixelSize;
		int pixel_y = y / this.pixelSize;
		int tmp_s = 0;

		if (pixel_x >= this.tabSizeX || pixel_y >= this.tabSizeY)
			return;

		tmp_s = (this.P[pixel_x][pixel_y].act == 1) ? 0 : 1;

		if (pixel_x < 1)
			pixel_x++;
		if (pixel_x >= this.tabSizeX - 1)
			pixel_x--;
		if (pixel_y < 1)
			pixel_y++;
		if (pixel_y >= this.tabSizeY - 1)
			pixel_y--;

		switch (Core.Config.pedzel) {
		case 1: // niezmiennik
			this.P[pixel_x][pixel_y - 1].act = tmp_s;
			this.P[pixel_x + 1][pixel_y - 1].act = tmp_s;
			this.P[pixel_x + 2][pixel_y].act = tmp_s;
			this.P[pixel_x + 1][pixel_y + 1].act = tmp_s;
			this.P[pixel_x][pixel_y + 1].act = tmp_s;
			this.P[pixel_x - 1][pixel_y].act = tmp_s;
			break;
		case 2:
			this.P[pixel_x][pixel_y - 1].act = tmp_s;
			this.P[pixel_x][pixel_y].act = tmp_s;
			this.P[pixel_x][pixel_y + 1].act = tmp_s;
			break;
		case 3:
			this.P[pixel_x][pixel_y - 1].act = tmp_s;
			this.P[pixel_x][pixel_y].act = tmp_s;
			this.P[pixel_x][pixel_y + 1].act = tmp_s;
			this.P[pixel_x + 1][pixel_y + 1].act = tmp_s;
			this.P[pixel_x + 2][pixel_y].act = tmp_s;
			break;
		default:
			this.P[pixel_x][pixel_y].act = tmp_s;
			break;
		}
		// System.out.println("Click this.P["+pixel_x+"]["+pixel_y+"] to: "+this.P[pixel_x][pixel_y].prev);

		this.reverse();
	}

	public void clean() {
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				this.P[i][j].set(0);
			}
		}
	}

	private void reverse() {
		for (int i = 0; i < this.tabSizeX; i++) {
			for (int j = 0; j < this.tabSizeY; j++) {
				this.P[i][j].prev = this.P[i][j].act;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		// super.paintComponent(g);

		for (int x = 0; x < this.tabSizeX; x++) {
			for (int y = 0; y < this.tabSizeY; y++) {

				if (this.P[x][y].act == 1) { // 0=>1
					g.setColor(new Color(10, 10, 10)); // BLACK
				} else { // 1=>0
					g.setColor(new Color(250, 250, 250)); // WHITE
				}

				g.fillRect(x * this.pixelSize, // position X
						y * this.pixelSize, // position Y
						this.pixelSize, // width
						this.pixelSize // height
				);
			}
		}
	}// g

}
