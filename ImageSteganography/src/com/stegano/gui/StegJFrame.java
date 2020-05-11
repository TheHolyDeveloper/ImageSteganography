package com.stegano.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;

import com.stegano.decode.DecodeMessage;
import com.stegano.encode.EncodeMessage;

/**
 * StegJFrame.java - class which designs the GUI part of the application .
 * 
 * @author Melchiz Raja Nadar
 * @version 7.0
 * @see EncodeMessage , DecodeMessage
 */

public class StegJFrame extends javax.swing.JFrame {

	private javax.swing.JButton encodeButton;
	private javax.swing.JButton decodeButton;

	/**
	 * Creates new form NewJFrame
	 */
	public StegJFrame() {
		super("Image Steganography");
		StegJFrame.setDefaultLookAndFeelDecorated(true);
		initComponents();
	}

	
	@SuppressWarnings("unchecked") // ("unchecked") tells the compiler that the programmer believes the code to be
									// safe and won't cause unexpected exceptions during casting
	private void initComponents() {
		// TODO Auto-generated method stub
		encodeButton = new javax.swing.JButton();
		encodeButton.setBackground(Color.BLACK);
		decodeButton = new javax.swing.JButton();
		decodeButton.setBackground(Color.BLACK);
		encodeButton.setText("ENCRYPT");
		encodeButton.setForeground(Color.WHITE);
		decodeButton.setText("DECRYPT");
		decodeButton.setForeground(Color.WHITE);

		encodeButton.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				encodeActionPerformed(event);
			}

		});
		decodeButton.addActionListener(new java.awt.event.ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				// TODO Auto-generated method stub
				decodeActionPerformed(event);
			}

		});

		// GroupLayout is a LayoutManager that hierarchically group the components and
		// arranges them in a Container.
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(131, 131, 131)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(encodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(decodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 125,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(139, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(52, 52, 52)
						.addComponent(encodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(60, 60, 60).addComponent(decodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(85, Short.MAX_VALUE)));

		getContentPane().setBackground(Color.LIGHT_GRAY);
		pack();
	}

	private void encodeActionPerformed(java.awt.event.ActionEvent evt) {
		EncodeMessage encodeMsg = new EncodeMessage();
		encodeMsg.setVisible(true);
	}

	private void decodeActionPerformed(java.awt.event.ActionEvent evt) {
		DecodeMessage decodeMsg = new DecodeMessage();
		decodeMsg.setVisible(true);
	}

	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(StegJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(StegJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(StegJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(StegJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}

		/*
		 * java.awt.EventQueue.invokeLater(new Runnable() { public void run() { new
		 * StegJFrame().setVisible(true); } });
		 */

	}
}
