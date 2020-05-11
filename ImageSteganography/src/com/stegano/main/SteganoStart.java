package com.stegano.main;

import com.stegano.gui.StegJFrame;

	/**
	 * SteganoStart.java -  main method which runs our application.  
	* @author Melchiz 
	* @version 7.0 
	**/

public class SteganoStart {
	public static void main(String args[]) {
		StegJFrame frame=new StegJFrame();
		frame.setVisible(true); // visibility of the window
		frame.setLocationRelativeTo(null); // position of the gui on the screen
	}
}
