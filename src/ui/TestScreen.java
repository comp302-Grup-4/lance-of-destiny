package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TestScreen extends JPanel {

	private static final long serialVersionUID = 99L;
	private JButton exit_button = new JButton();
	private JButton running_screen = new JButton();
	/**
	 * Create the panel.
	 */
	public TestScreen() {

		exit_button.setText("Exit");
		exit_button.setSize(150, 50);
		
		running_screen.setText("Running Screen");
		exit_button.setSize(150, 50);
		
		
		exit_button.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				System.exit(1);
			}
		});
		
		running_screen.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			
				((GameApp) TestScreen.this.getRootPane().getParent()).openRunningScreen();
			}
					
		});
		
		this.add(running_screen);
		this.add(exit_button);
		this.add(running_screen);
		
	}

}
