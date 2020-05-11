package com.stegano.decode;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
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
import javax.swing.JTextArea;

public class DecodeMessage extends JFrame implements ActionListener {

	/**
	 * DecodeMessage.java - class which demonstrates the decrypting part.
	 * 
	 * @author Melchiz 
	 * @version 7.0
	 */

	private static final long serialVersionUID = 3284641008684858064L;
	JButton open = new JButton("Open");
	JButton decode = new JButton("Decode");
	JButton reset = new JButton("Reset");
	JTextArea message = new JTextArea(10, 3);
	BufferedImage image = null;
	JScrollPane imagePane = new JScrollPane();

	public DecodeMessage() {
		super("Decode stegonographic message in image");
		assembleInterface();
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void assembleInterface() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.add(open);
		panel.add(decode);
		panel.add(reset);
		this.getContentPane().add(panel, BorderLayout.NORTH);
		open.addActionListener(this);
		decode.addActionListener(this);
		reset.addActionListener(this);

		panel = new JPanel(new GridLayout(1, 1));
		panel.add(new JScrollPane(message));
		message.setFont(new Font("Arial", Font.ITALIC, 16));
		message.setBackground(Color.BLACK);
		message.setForeground(Color.GREEN);
		panel.setBorder(BorderFactory.createTitledBorder("Decrypted message"));
		message.setEditable(false);
		this.getContentPane().add(panel, BorderLayout.SOUTH);

		imagePane.setBorder(BorderFactory.createTitledBorder("Steganographed Image"));
		this.getContentPane().add(imagePane, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object object = event.getSource();
		if (object == open)
			openImage();
		else if (object == decode)
			decodeMessage();
		else if (object == reset)
			resetInterface();
	}

	private java.io.File showFileDialog(boolean open) {
		JFileChooser fileChooseFilter = new JFileChooser("Open an image");
		javax.swing.filechooser.FileFilter fileFilter = new javax.swing.filechooser.FileFilter() {
			public boolean accept(java.io.File file) {
				String name = file.getName().toLowerCase();
				return file.isDirectory() || name.endsWith(".png") || name.endsWith(".bmp");
			}

			public String getDescription() {
				return "Image (*.png, *.bmp)";
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
				image = ImageIO.read(file);
				JLabel label = new JLabel(new ImageIcon(image));
				imagePane.getViewport().add(label);
				this.validate();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void decodeMessage() {
		if (image == null) {
			JOptionPane.showMessageDialog(this, "Please select an image!", "Warning", JOptionPane.ERROR_MESSAGE);
		} else {
			int length = extractInteger(image, 0, 0);
			byte byteLength[] = new byte[length];
			for (int i = 0; i < length; i++) {
				byteLength[i] = extractByte(image, i * 8 + 32, 0);
			}
			message.setText(new String(byteLength));
		}

	}

	/**
	 * Embed the Message length inside an image
	 * 
	 * @param An Image , start - an integer which represents the start position
	 * @param storage bit- an integer which represent the bit storing position
	 * 
	 * @return -> an integer which represent the length of the message stored
	 */
	private int extractInteger(BufferedImage img, int start, int storageBit) {
		int count = 32;
		String binary = "";
		for (int i = 0; i < count; i++) {
			int rgb = img.getRGB(0, i);
			String rgbStr = Integer.toBinaryString(rgb);
			binary = binary + rgbStr.substring(rgbStr.length() - 1);
		}
		StringBuilder sbr = new StringBuilder(binary);
		String sbrTOstr = sbr.reverse().toString();
		int length = (int) Long.parseLong(sbrTOstr, 2);
		return length;
	}

	/**
	 * Extract the Message from inside an image
	 * 
	 * @param Image ,storage Bit- >an integer which represent the bit storing
	 *              position, start - an integer which represents the start position
	 * @return byte extracted from the image
	 */
	private byte extractByte(BufferedImage img, int start, int storageBit) {
		int maxX = img.getWidth();
		int maxY = img.getHeight();
		int startX = start / maxY;
		int startY = start - startX * maxY;
		int count = 0;
		String bin = "";
		byte asciiValue = 0;
		for (int i = startX; i < maxX && count < 8; i++) {
			for (int j = startY; j < maxY && count < 8; j++) {
				int rgb = img.getRGB(i, j);
				String rgbStr = Integer.toBinaryString(rgb);
				bin = bin + rgbStr.substring(rgbStr.length() - 1);
				count++;
			}
			StringBuilder sbr = new StringBuilder(bin);
			asciiValue = (byte) Long.parseLong(sbr.reverse().toString(), 2);
		}
		return asciiValue;

	}

	private void resetInterface() {
		message.setText("");
		imagePane.getViewport().removeAll();
		image = null;
		this.validate();
	}
}
