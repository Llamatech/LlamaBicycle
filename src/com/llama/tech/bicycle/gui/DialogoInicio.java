/*
 * DialogoInicio.java
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

import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DialogoInicio extends JDialog implements ActionListener{
	
	public final static int CAMINOS = 1;
	public final static int CONEXIONES = 2;
	private int estado;
	
	public DialogoInicio() {
		setSize(new Dimension(300, 500));
		getContentPane().setLayout(null);
		
		JLabel lblFibo = new JLabel("Fibo");
		lblFibo.setBounds(41, 18, 212, 199);
		getContentPane().add(lblFibo);
		
		JButton btnNewButton = new JButton("Caminos");
		btnNewButton.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
		btnNewButton.setBounds(35, 414, 233, 29);
		btnNewButton.setActionCommand("CAM");
		btnNewButton.addActionListener(this);
		getContentPane().add(btnNewButton);
		
		JButton btnConex = new JButton("Conexiones y Estaciones");
		btnConex.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
		btnConex.setActionCommand("CONEX");
		btnConex.addActionListener(this);
		btnConex.setBounds(35, 373, 233, 29);
		getContentPane().add(btnConex);
		
		JTextArea txtrBienvenidoALlamabicyle = new JTextArea();
		txtrBienvenidoALlamabicyle.setWrapStyleWord(true);
		txtrBienvenidoALlamabicyle.setLineWrap(true);
		txtrBienvenidoALlamabicyle.setFont(new Font("Apple Color Emoji", Font.PLAIN, 12));
		txtrBienvenidoALlamabicyle.setEditable(false);
		txtrBienvenidoALlamabicyle.setText("Bienvenido a LlamaBicyle, donde puede encontrar las mejores rutas para sus paseos por Boston. Abajo, puede elegir entre la vista de búsqueda de estaciones y conexiones, o la búsqueda de caminos.");
		txtrBienvenidoALlamabicyle.setBackground(SystemColor.window);
		txtrBienvenidoALlamabicyle.setBounds(24, 235, 251, 126);
		getContentPane().add(txtrBienvenidoALlamabicyle);
		
		setModalityType(DEFAULT_MODALITY_TYPE);
		setVisible(true);

	}
	
	public int getEstado()
	{
		return estado;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("CONEX"))
		{
			estado = CONEXIONES;
			setVisible(false);
			dispose();
			
		}
		else if(e.getActionCommand().equals("CAM"))
		{
			estado = CAMINOS;
			setVisible(false);
			dispose();
		}
		
	}
	
	
}
