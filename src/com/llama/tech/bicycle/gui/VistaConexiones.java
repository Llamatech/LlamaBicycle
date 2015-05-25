/*
 * VistaConexiones.java
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

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.llama.tech.bicycle.backend.Conexion;
import com.llama.tech.bicycle.backend.Estacion;
import com.llama.tech.bicycle.gui.components.MapMarker;
import com.llama.tech.bicycle.gui.components.SystemMap;
import com.llama.tech.misc.LlamaTuple;
import com.llama.tech.utils.list.Lista;
import com.llama.tech.utils.list.LlamaArrayList;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class VistaConexiones extends JPanel implements ActionListener
{
	
	private SystemMap mapa;
	private Interfaz main;
	private DefaultListModel<Conexion> model;
	private JList<Conexion> list;
	private int lastId;
	private DisplayBy state = DisplayBy.NONE;
	private JTextField textField;
	
	public VistaConexiones(Interfaz principal) 
	{
		main = principal;
		setMinimumSize(new Dimension(500, 500));
		setLayout(null);
		
		mapa = new SystemMap(main);	
		mapa.setBounds(22, 22, 370, 347);
		add(mapa);
		
		JButton btnMostrarConexionesDesde = new JButton("<HTML>Mostrar conexiones\n <br><center>\ndesde origen</center></HTML>");
		btnMostrarConexionesDesde.setBounds(404, 32, 171, 47);
		btnMostrarConexionesDesde.setActionCommand("DESDE");
		btnMostrarConexionesDesde.addActionListener(this);
		add(btnMostrarConexionesDesde);
		
		JButton btnHabilitardeshabilitarConexin = new JButton("Habilitar/Deshabilitar Conexión");
		btnHabilitardeshabilitarConexin.setBounds(169, 501, 240, 29);
		btnHabilitardeshabilitarConexin.addActionListener(this);
		btnHabilitardeshabilitarConexin.setActionCommand("HD");
		add(btnHabilitardeshabilitarConexin);
		
		JButton btnmostrarConexionesHacia = new JButton("<HTML>Mostrar conexiones\n <br><center>\nhacia destino</center></HTML>");
		btnmostrarConexionesHacia.setBounds(404, 91, 171, 47);
		btnmostrarConexionesHacia.setActionCommand("HACIA");
		btnmostrarConexionesHacia.addActionListener(this);
		add(btnmostrarConexionesHacia);
		
		JButton btnmostrarEstaciones = new JButton("<HTML>Mostrar Estaciones</HTML>");
		btnmostrarEstaciones.setBounds(404, 148, 171, 47);
		btnmostrarEstaciones.setActionCommand("ESTACIONES");
		btnmostrarEstaciones.addActionListener(this);
		add(btnmostrarEstaciones);
		
		JButton btndeshabilitarEstacion = new JButton("Deshabilitar Estacion");
		btndeshabilitarEstacion.setBounds(404, 208, 171, 47);
		btndeshabilitarEstacion.addActionListener(this);
		btndeshabilitarEstacion.setActionCommand("DESHABILITAR");
		add(btndeshabilitarEstacion);
		
		JButton btnhabilitarEstacion = new JButton("<HTML>Habilitar Estacion</HTML>");
		btnhabilitarEstacion.setBounds(404, 267, 171, 47);
		btnhabilitarEstacion.setActionCommand("HABILITAR");
		btnhabilitarEstacion.addActionListener(this);
		add(btnhabilitarEstacion);
		
		JLabel lblConexiones = new JLabel("Conexiones:");
		lblConexiones.setBounds(22, 381, 92, 16);
		add(lblConexiones);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 409, 536, 89);
		add(scrollPane);
		
		model = new DefaultListModel<Conexion>();
		list = new JList<Conexion>(model);
		list.setBackground(UIManager.getColor("Button.background"));
		scrollPane.setViewportView(list);
		
		textField = new JTextField();
		textField.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		textField.setEditable(false);
		textField.setBounds(404, 353, 171, 19);
		add(textField);
		textField.setColumns(10);
		textField.setText("<Sin seleccionar>");
		
		JLabel lblEstacinSeleccionada = new JLabel("Estación seleccionada:");
		lblEstacinSeleccionada.setBounds(404, 326, 171, 15);
		add(lblEstacinSeleccionada);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getActionCommand().equals("DESDE"))
		{
			String origen = JOptionPane.showInputDialog("Introduzca el id de la estación origen:");
			try
			{
			     lastId = Integer.parseInt(origen);
			     state = DisplayBy.ORIGIN;
			     displayRoutes();
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if (e.getActionCommand().equals("HACIA"))
		{
			String origen = JOptionPane.showInputDialog("Introduzca el id de la estación destino:");
			try
			{
			     lastId = Integer.parseInt(origen); 
			     state = DisplayBy.DESTINATION;
			     displayRoutes();
			      
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
			
		}
		else if(e.getActionCommand().equals("ESTACIONES"))
		{
			Lista<Estacion> list = main.getStationList();
			LlamaArrayList<MapMarker> st = new LlamaArrayList<MapMarker>(list.size());
			for(Estacion s : list)
			{
				MapMarker m = new MapMarker(s.getLatitud(), s.getLongitud());
				st.addAlFinal(m);
			}
			model.clear();
			mapa.clearRoutes();
			mapa.setMarkers(st);
		}
		else if(e.getActionCommand().equals("DESHABILITAR"))
		{
			main.disableStation();
			Lista<Estacion> list = main.getStationList();
			LlamaArrayList<MapMarker> st = new LlamaArrayList<MapMarker>(list.size());
			for(Estacion s : list)
			{
				MapMarker m = new MapMarker(s.getLatitud(), s.getLongitud());
				st.addAlFinal(m);
			}
			model.clear();
			mapa.clearRoutes();
			mapa.setMarkers(st);
		}
		else if(e.getActionCommand().equals("HABILITAR"))
		{
			String o = JOptionPane.showInputDialog("Introduzca el id de la estación a habilitar:");
			try
			{
			     int id = Integer.parseInt(o);
			     main.enableStation(id);
			     Lista<Estacion> list = main.getStationList();
					LlamaArrayList<MapMarker> st = new LlamaArrayList<MapMarker>(list.size());
					for(Estacion s : list)
					{
						MapMarker m = new MapMarker(s.getLatitud(), s.getLongitud());
						st.addAlFinal(m);
					}
					mapa.clearRoutes();
					mapa.clearRoutes();
					mapa.setMarkers(st);
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("HD"))
		{
			Conexion selected = list.getSelectedValue();
			if(selected != null)
			{
				if(selected.isHabilitado())
				{
					main.disableConnection(selected);
				}
				else
				{
					main.enableConnection(selected);
				}
				
				displayRoutes();
			}
			else
			{
				JOptionPane.showMessageDialog(this, "Debe selecccionar una conexión", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	
	
	public void displayRoutes()
	{
		 model.clear();
	     Lista<Conexion> list = main.showConnectionsByDestination(lastId);
	     if(list != null)
	     {
		     Lista<Conexion> disabled = main.getDisabledConnections();
		     list.addAll(disabled);
		     LlamaArrayList<LlamaTuple<MapMarker, MapMarker>> routes = new LlamaArrayList<LlamaTuple<MapMarker, MapMarker>>(list.size());
		     LlamaArrayList<MapMarker> markers = new LlamaArrayList<MapMarker>(list.size());
		     Conexion c1 = list.getFirst();
		     Estacion initial = c1.getOrigen();
		     markers.addAlFinal(new MapMarker(initial.getLatitud(), initial.getLongitud()));
		     for(Conexion c : list)
		     {
		    	 if(state == DisplayBy.ORIGIN)
		    	 {
		    		 boolean habilitado = c.isHabilitado();
		    		 c = c.clone();
		    		 c.setHabilitado(habilitado);
		    		 c.invert();
		    	 }
		    	 if(c.isHabilitado())
		    	 {
		    		 initial = c.getOrigen();
		    		 Estacion dest = c.getDestino();
		    		 MapMarker m1 = new MapMarker(initial.getLatitud(), initial.getLongitud());
		    		 MapMarker m2 = new MapMarker(dest.getLatitud(), dest.getLongitud());
		    	 
		    		 if(state == DisplayBy.ORIGIN)
			    	 {
		    			 markers.addAlFinal(m2);
			    	 }
		    		 else
		    			 markers.addAlFinal(m1);
		    		 routes.addAlFinal(new LlamaTuple<MapMarker, MapMarker>(m1, m2));
		    	 }
		    	 
		    	 model.addElement(c);
		     }
		     
		     mapa.displayRoutes(routes);
		     mapa.setMarkers(markers);
	     }
	}

	private enum DisplayBy
	{
		ORIGIN, DESTINATION, NONE 
	}

	public void notifyStation() 
	{
		Estacion e = main.getSelectedStation();
		if(e != null)
		{
			textField.setText("Id: "+e.getId()+"; "+e.getNombre());
		}
		else
		{
			textField.setText("<Sin seleccionar>");
		}
		
	}
}
