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

public class VistaConexiones extends JPanel implements ActionListener
{
	
	private SystemMap mapa;
	private Interfaz main;
	private DefaultListModel<Conexion> model;
	private JList<Conexion> list;
	private int lastId;
	private DisplayBy state = DisplayBy.NONE;
	
	public VistaConexiones(Interfaz principal) 
	{
		main = principal;
		setMinimumSize(new Dimension(500, 500));
		setLayout(null);
		
		mapa = new SystemMap(main);	
		mapa.setBounds(22, 22, 370, 347);
		add(mapa);
		
		JButton btnMostrarConexionesDesde = new JButton("<HTML>Mostrar conexiones\n <br><center>\ndesde origen</center></HTML>");
		btnMostrarConexionesDesde.setBounds(404, 57, 171, 47);
		btnMostrarConexionesDesde.setActionCommand("DESDE");
		btnMostrarConexionesDesde.addActionListener(this);
		add(btnMostrarConexionesDesde);
		
		JButton btnHabilitardeshabilitarConexin = new JButton("Habilitar/Deshabilitar Conexión");
		btnHabilitardeshabilitarConexin.setBounds(169, 501, 240, 29);
		btnHabilitardeshabilitarConexin.addActionListener(this);
		btnHabilitardeshabilitarConexin.setActionCommand("HD");
		add(btnHabilitardeshabilitarConexin);
		
		JButton btnmostrarConexionesHacia = new JButton("<HTML>Mostrar conexiones\n <br><center>\nhacia destino</center></HTML>");
		btnmostrarConexionesHacia.setBounds(404, 116, 171, 47);
		btnmostrarConexionesHacia.setActionCommand("HACIA");
		btnmostrarConexionesHacia.addActionListener(this);
		add(btnmostrarConexionesHacia);
		
		JButton btnmostrarEstaciones = new JButton("<HTML>Mostrar Estaciones</HTML>");
		btnmostrarEstaciones.setBounds(404, 173, 171, 47);
		btnmostrarEstaciones.setActionCommand("ESTACIONES");
		btnmostrarEstaciones.addActionListener(this);
		add(btnmostrarEstaciones);
		
		JButton btndeshabilitarEstacion = new JButton("Deshabilitar Estacion");
		btndeshabilitarEstacion.setBounds(404, 233, 171, 47);
		btndeshabilitarEstacion.addActionListener(this);
		btndeshabilitarEstacion.setActionCommand("DESHABILITAR");
		add(btndeshabilitarEstacion);
		
		JButton btnhabilitarEstacion = new JButton("<HTML>Habilitar Estacion</HTML>");
		btnhabilitarEstacion.setBounds(404, 292, 171, 47);
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
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.ERROR_MESSAGE);
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
	    		 c = c.clone();
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

	private enum DisplayBy
	{
		ORIGIN, DESTINATION, NONE 
	}
}
