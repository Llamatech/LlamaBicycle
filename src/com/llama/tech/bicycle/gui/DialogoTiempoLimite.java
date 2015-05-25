/*
 * DialogoTiempoLimite.java
 * This file is part of LlamaBicycle
 *
 * Copyright (C) 2015 - LlamaTech Team 
 *
 * LlamaUtils is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LlamaUtils is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LlamaUtils. If not, see <http://www.gnu.org/licenses/>.
 */

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
