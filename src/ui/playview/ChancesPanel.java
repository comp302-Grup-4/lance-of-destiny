package ui.playview;

import java.awt.Color;
import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChancesPanel extends JPanel implements Serializable {
	public static ImageIcon heartImage = new ImageIcon("./res/drawable/largeHeart.png");
	int chances;
	JLabel[] heartLabels = new JLabel[3];
	
	public ChancesPanel() {
		this.setLayout(new FlowLayout(FlowLayout.TRAILING));
		
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.setBackground(new Color(0, 0, 0, 0));
		for (int i = 0; i < 3; i++) {
			heartLabels[i] = new JLabel(heartImage);
			heartLabels[i].setVisible(true);
			this.add(heartLabels[i]);
		}
		this.setSize((heartImage.getIconWidth() + 50) * 3, (heartImage.getIconHeight() + 50));
	}
	
	public void setChances(int chances) {
		this.chances = chances;
		for (int i = 0; i < heartLabels.length; i++) {
			if (i < chances)
				heartLabels[i].setVisible(true);
			else
				heartLabels[i].setVisible(false);
		}
	}

	public int getChances() {
		return chances;
	}
}
