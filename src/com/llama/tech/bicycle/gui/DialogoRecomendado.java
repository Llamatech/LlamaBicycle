package com.llama.tech.bicycle.gui;

import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class DialogoRecomendado extends JDialog implements ActionListener {
	private JTextField textField;
	private JTextField textField_1;
	private String estaciones;
	private String origen;
	
	public DialogoRecomendado() {
		setSize(new Dimension(290, 416));
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("<HTML><center>LlamaBicycle le ofrece un sistema de recomendación de caminos, según las estaciones que desee visitar. De esta manera, puede vsitarlas en el menor tiempo posible. Para lograrlo, introduzca los id's de las estaciones que desea visitar separados por espacios y la estación desde donde desea partir. Esperamos que le sea de ayuda!</center></HTML>");
		lblNewLabel.setBounds(16, 18, 256, 184);
		getContentPane().add(lblNewLabel);
		
		JLabel lblSecuenciaDeEstaciones = new JLabel("Secuencia de estaciones:");
		lblSecuenciaDeEstaciones.setBounds(16, 214, 165, 16);
		getContentPane().add(lblSecuenciaDeEstaciones);
		
		textField = new JTextField();
		textField.setBounds(16, 242, 256, 28);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblIdDeLa = new JLabel("ID de la estación origen:");
		lblIdDeLa.setBounds(16, 282, 177, 16);
		getContentPane().add(lblIdDeLa);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(16, 310, 256, 28);
		getContentPane().add(textField_1);
		
		JButton btnIr = new JButton("Enviar");
		btnIr.setBounds(56, 350, 177, 28);
		btnIr.setActionCommand("ENVIAR");
		btnIr.addActionListener(this);
		getContentPane().add(btnIr);
		
		setModalityType(DEFAULT_MODALITY_TYPE);
		setVisible(true);
	}

	
	public String getEstaciones() {
		return estaciones;
	}


	public String getOrigen() {
		return origen;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ENVIAR"))
		{
			estaciones = textField.getText();
			origen = textField_1.getText();
			setVisible(false);
			dispose();
		}
		
	}
	


}
