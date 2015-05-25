/*
 * DialogoCaminoAB.java
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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DialogoCaminoAB extends JDialog implements ActionListener {
	private JTextField textField;
	private JTextField textField_1;
	private String origen;
	private String destino;
	
	
	public DialogoCaminoAB() {
		setSize(new Dimension(330, 255));
		getContentPane().setLayout(null);
		
		JLabel lblIndiqueElId = new JLabel("Indique el ID de la estación origen:");
		lblIndiqueElId.setBounds(49, 20, 219, 22);
		getContentPane().add(lblIndiqueElId);
		
		textField = new JTextField();
		textField.setBounds(19, 54, 293, 28);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(19, 128, 293, 28);
		getContentPane().add(textField_1);
		
		JButton btnNewButton = new JButton("Enviar");
		btnNewButton.setBounds(94, 187, 133, 22);
		btnNewButton.addActionListener(this);
		btnNewButton.setActionCommand("ENVIAR");
		getContentPane().add(btnNewButton);
		
		JLabel lblIndiqueElId_1 = new JLabel("Indique el ID de la estación destino:");
		lblIndiqueElId_1.setBounds(49, 94, 229, 22);
		getContentPane().add(lblIndiqueElId_1);
		setModalityType(DEFAULT_MODALITY_TYPE);
		setVisible(true);
	}


	public String getOrigen()
	{
		return origen;
	}
	
	public String getDestino()
	{
		return destino;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ENVIAR"))
		{
			if(textField.getText().equals("")||textField_1.getText().equals(""))
			{
				JOptionPane.showMessageDialog(this, "Debe introducir valores");
			}
			else
			{
				origen = textField.getText();
				destino = textField_1.getText();
				
				setVisible(false);
				dispose();
			}
		}
		
	}

}
