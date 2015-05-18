package com.llama.tech.bicycle.gui;

import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class DialogoTiempoLimite extends JDialog implements ActionListener{

	private JTextField textField;
	private JTextField textField_1;
	private int tiempo;
	private String origen;

	public DialogoTiempoLimite() {
		setSize(new Dimension(330, 255));
		getContentPane().setLayout(null);

		JLabel lblIndiqueElId = new JLabel("Indique el ID de la estación origen:");
		lblIndiqueElId.setBounds(49, 20, 219, 22);
		getContentPane().add(lblIndiqueElId);

		textField = new JTextField();
		textField.setBounds(19, 54, 293, 28);
		getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblIndiqueElTiempo = new JLabel("Indique el tiempo límite:");
		lblIndiqueElTiempo.setBounds(82, 94, 154, 22);
		getContentPane().add(lblIndiqueElTiempo);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(19, 128, 293, 28);
		getContentPane().add(textField_1);

		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.setBounds(94, 187, 133, 22);
		btnNewButton.addActionListener(this);
		btnNewButton.setActionCommand("ENVIAR");
		getContentPane().add(btnNewButton);

		setModalityType(DEFAULT_MODALITY_TYPE);

		setVisible(true);
	}

	public int getTiempo()
	{
		return tiempo;
	}

	public String getOrigen()
	{
		return origen;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ENVIAR"))
		{
			try
			{
				origen=textField.getText();

				tiempo = Integer.parseInt(textField_1.getText());

				setVisible(false);
				dispose();
			}
			catch(NumberFormatException ex)
			{
				JOptionPane.showMessageDialog(this, "Debe introducir un número","Error", JOptionPane.WARNING_MESSAGE);
			}
		}

	}

}
