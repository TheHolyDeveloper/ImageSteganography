package com.stegano.encode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 * EncodeMessage.java - class which demonstrates the encoding.
 * 
 * @author Melchiz	
 * @version 7.0
 */

public class EncodeMessage extends JFrame implements ActionListener {

	JButton open = new JButton("Open");
	JButton embed = new JButton("Embed");
	JButton save = new JButton("Save into new file");
	JButton reset = new JButton("Reset");
	JTextArea message = new JTextArea(10, 3);
	BufferedImage sourceImage = null;
	BufferedImage embeddedImage = null;
	JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	JScrollPane originalPane = new JScrollPane();
	JScrollPane embeddedPane = new JScrollPane();
	public String fileName;
	private static String PNGFILEEXTENSION = "png";
	private static String DOTOPERATOR = ".";

	public EncodeMessage() {
		super("Encrypt a message in the image");
		assembleInterface();
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		splitPane.setDividerLocation(0.5);
		this.validate();
	}

	/**
	 * assembles the GUI layout for the Encryption part
	 * 
	 */

	private void assembleInterface() {
		JPanel panel = new JPanel(new FlowLayout());
		message.setBackground(Color.BLACK);
		panel.add(open);
		panel.add(embed);
		panel.add(save);
		panel.add(reset);
		this.getContentPane().add(panel, BorderLayout.SOUTH); // add this to the bottom of the panel
		open.addActionListener(this);
		embed.addActionListener(this);
		save.addActionListener(this);
		reset.addActionListener(this);
		panel = new JPanel(new GridLayout(1, 1));
		panel.setVisible(true);

		panel.add(new JScrollPane(message));
		message.setFont(new Font("Arial", Font.ITALIC, 16));
		message.setForeground(Color.GREEN);
		message.setCaretColor(Color.GREEN);
		panel.setBorder(BorderFactory.createTitledBorder("Message to be embedded"));
		this.getContentPane().add(panel, BorderLayout.NORTH);
		splitPane.setLeftComponent(originalPane);
		splitPane.setRightComponent(embeddedPane);
		originalPane.setBorder(BorderFactory.createTitledBorder("Original Image"));
		embeddedPane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));
		this.getContentPane().add(splitPane, BorderLayout.CENTER);
	}

	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object == open)
			openImage();
		else if (object == embed)
			embedMessage();
		else if (object == save)
			saveImage();
		else if (object == reset)
			resetInterface();
	}

	/**
	 * Opens the file dialog box
	 */
	private java.io.File showFileDialog(final boolean open) {
		JFileChooser fileChooseFilter = new JFileChooser("Open an image");
		javax.swing.filechooser.FileFilter fileFilter = new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File file) {
				String name = file.getName().toLowerCase();
				if (open)
					return file.isDirectory() || name.endsWith(".jpg") || name.endsWith(".jpeg")
							|| name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".tiff")
							|| name.endsWith(".bmp") || name.endsWith(".dib");
				return file.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
			}

			public String getDescription() {
				if (open)
					return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
				return "Image (*.jpg, *.jpeg, *.png, *.gif, *.tiff, *.bmp, *.dib)";
			}
		};
		fileChooseFilter.setAcceptAllFileFilterUsed(false);
		fileChooseFilter.addChoosableFileFilter(fileFilter);

		java.io.File file = null;
		if (open && fileChooseFilter.showOpenDialog(this) == fileChooseFilter.APPROVE_OPTION)
			file = fileChooseFilter.getSelectedFile();
		else if (!open && fileChooseFilter.showSaveDialog(this) == fileChooseFilter.APPROVE_OPTION)
			file = fileChooseFilter.getSelectedFile();
		return file;
	}

	private void openImage() {
		java.io.File file = showFileDialog(true);
		try {
			if (file != null) {
				sourceImage = ImageIO.read(file);
				JLabel l = new JLabel(new ImageIcon(sourceImage));
				originalPane.getViewport().add(l);
				this.validate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void embedMessage() {
		String text = message.getText();
		if (text.length() == 0) {
			JOptionPane.showMessageDialog(this, "Please type your message!", "Warning", JOptionPane.ERROR_MESSAGE);
		} else {
			embeddedImage = sourceImage.getSubimage(0, 0, sourceImage.getWidth(), sourceImage.getHeight());
			embedMessage(embeddedImage, text);
			JLabel l = new JLabel(new ImageIcon(embeddedImage));
			embeddedPane.getViewport().add(l);
			this.validate();
		}

	}

	/**
	 * Embed the message inside an image
	 * 
	 * @param An Image and the message which needs to be embedded
	 */
	private void embedMessage(BufferedImage img, String mess) {
		int messageLength = mess.length();
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		int imageSize = imageWidth * imageHeight;
		if (messageLength * 8 + 32 > imageSize) {
			JOptionPane.showMessageDialog(this, "Message is too long for the chosen image", "Message too long!",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		embedInteger(img, messageLength, 0, 0);
		byte b[] = mess.getBytes();
		for (int i = 0; i < b.length; i++)
			embedByte(img, b[i], i * 8 + 32, 0);
	}

	/**
	 * Embed the Message length inside an image
	 * 
	 * @param An  Image, start -
	 *                an integer which represents the start position
	 * @param storage bit- an integer which represent the bit storing position
	 */
	private void embedInteger(BufferedImage img, int msgLength, int start, int storageBit) {
		int maxX = img.getWidth();
		int maxY = img.getHeight();
		int startX = start / maxY;
		int startY = start - startX * maxY;
		int count = 0;
		for (int i = startX; i < maxX && count < 32; i++) {
			for (int j = startY; j < maxY && count < 32; j++) {
				int rgb = img.getRGB(i, j);
				int bit = getBitValue(msgLength, count);
				rgb = setBitValue(rgb, storageBit, bit);
				img.setRGB(i, j, rgb);
				count++;
			}
		}
	}

	/**
	 * Embed the Message inside an image
	 * 
	 * @param An      Image ,b- > a byte which represents the byte value of a single
	 *                character of a message, start - an integer which represents
	 *                the start position
	 * @param storage bit- an integer which represent the bit storing position
	 */
	private void embedByte(BufferedImage img, byte b, int start, int storageBit) {
		int maxX = img.getWidth();
		int maxY = img.getHeight();
		int startX = start / maxY;
		int startY = start - startX * maxY;
		int bit = 0;
		int count = 0;
		for (int i = startX; i < maxX && count < 8; i++) {
			for (int j = startY; j < maxY && count < 8; j++) {
				int rgb = img.getRGB(i, j);
				bit = getBitValue(b, count);
				rgb = setBitValue(rgb, storageBit, bit);
				img.setRGB(i, j, rgb);
				count++;
			}
		}
	}

	/**
	 * Save the image after encryption
	 */
	private void saveImage() {
		if (embeddedImage == null) {
			JOptionPane.showMessageDialog(this, "No message has been embedded!", "Nothing to save",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		java.io.File file = showFileDialog(false);
		String name = file.getName();
		file = new java.io.File(file.getAbsolutePath() + DOTOPERATOR + PNGFILEEXTENSION);

		try {
			if (file.exists())
				file.delete();
			ImageIO.write(embeddedImage, PNGFILEEXTENSION.toUpperCase(), file);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void resetInterface() {
		message.setText("");
		originalPane.getViewport().removeAll();
		embeddedPane.getViewport().removeAll();
		sourceImage = null;
		embeddedImage = null;
		splitPane.setDividerLocation(0.5);
		this.validate();
	}

	/**
	 * get the bit value based on the integer and the location
	 * 
	 * @param number- > an integer for which bit needs to be found , location -> the
	 *                position of the bit
	 * @return value- > the value of the bit at the mentioned location
	 */
	private int getBitValue(int number, int location) {
		int value = number & (int) Math.round(Math.pow(2, location));
		return value == 0 ? 0 : 1;
	}

	/**
	 * set the bit value based on the integer and the location
	 * 
	 * @param number- > an integer for which bit needs to be found , location -> the
	 *                position of the bit,bit ->a bit integer either 0/1 which needs
	 *                to be set in the number
	 * @return value- > the value of the bit at the mentioned location
	 */
	private int setBitValue(int number, int location, int bit) {
		int toggle = (int) Math.pow(2, location), bitValue = getBitValue(number, location);
		if (bitValue == bit)
			return number;
		if (bitValue == 0 && bit == 1)
			number |= toggle; // if the last bit is 0 ,this or operation will change it to 1
		else if (bitValue == 1 && bit == 0)
			number ^= toggle; // if the last bit is 1 ,it will change to 0 using ex-or
		return number;
	}

}
