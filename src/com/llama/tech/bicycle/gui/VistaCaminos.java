package com.llama.tech.bicycle.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import com.llama.tech.bicycle.backend.Conexion;
import com.llama.tech.bicycle.backend.Estacion;
import com.llama.tech.bicycle.gui.components.MapMarker;
import com.llama.tech.bicycle.gui.components.SystemMap;
import com.llama.tech.misc.LlamaTuple;
import com.llama.tech.utils.list.Lista;
import com.llama.tech.utils.list.LlamaArrayList;

public class VistaCaminos extends JPanel implements ActionListener
{
	private JList<Estacion> listConexiones;
	private DefaultListModel<Estacion> model;
	private SystemMap mapa;
	private JTextField textField;
	private Interfaz main;
	
	public VistaCaminos(Interfaz principal) 
	{
		main = principal;
		setMinimumSize(new Dimension(500, 500));
		setLayout(null);
		
		mapa = new SystemMap(main);	
		mapa.setBounds(22, 22, 370, 347);
		add(mapa);
		
		JButton btnMostrarConexionesDesde = new JButton("<HTML>Camino más corto\n <br><center>\nde A a B\n</center></HTML>");
		btnMostrarConexionesDesde.setActionCommand("CAMINOAB");
		btnMostrarConexionesDesde.addActionListener(this);
		btnMostrarConexionesDesde.setBounds(404, 22, 171, 47);
		add(btnMostrarConexionesDesde);
		
		//JButton btnHabilitardeshabilitarConexin = new JButton("Habilitar/Deshabilitar Estación\n\n");
		//btnHabilitardeshabilitarConexin.setBounds(169, 501, 240, 29);
		//add(btnHabilitardeshabilitarConexin);
		
		JButton btnmostrarConexionesHacia = new JButton("<HTML><center>Caminos más<br> cortos desde origen</center></HTML>");
		btnmostrarConexionesHacia.setBounds(404, 81, 171, 47);
		btnmostrarConexionesHacia.setActionCommand("CAMDESDE");
		btnmostrarConexionesHacia.addActionListener(this);
		add(btnmostrarConexionesHacia);
		
		
		JButton btnmostrarEstaciones = new JButton("<HTML><center>Estaciones por<br> tiempo límite</center></HTML>");
		btnmostrarEstaciones.setBounds(404, 140, 171, 47);
		btnmostrarEstaciones.setActionCommand("TIEMPO");
		btnmostrarEstaciones.addActionListener(this);
		add(btnmostrarEstaciones);
		
		JButton btndeshabilitarEstacion = new JButton("<HTML><center>Viaje de mayor <br>longitud</center></HTML>");
		btndeshabilitarEstacion.setBounds(404, 199, 171, 47);
		btndeshabilitarEstacion.setActionCommand("MAS");
		btndeshabilitarEstacion.addActionListener(this);
		add(btndeshabilitarEstacion);
		
		JButton btnhabilitarEstacion = new JButton("<HTML>Recomendar Viaje</HTML>");
		btnhabilitarEstacion.addActionListener(this);
		btnhabilitarEstacion.setActionCommand("REC");
		btnhabilitarEstacion.setBounds(404, 258, 171, 47);
		add(btnhabilitarEstacion);
		
		JLabel lblConexiones = new JLabel("Estaciones");
		lblConexiones.setBounds(22, 381, 92, 16);
		add(lblConexiones);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(22, 409, 536, 89);
		add(scrollPane);
		
		model = new DefaultListModel<Estacion>();
		listConexiones = new JList<Estacion>(model);
		listConexiones.setBackground(UIManager.getColor("Button.background"));
		scrollPane.setViewportView(listConexiones);
		
		JLabel lblTiempoPromedio = new JLabel("Tiempo promedio:");
		lblTiempoPromedio.setBounds(404, 320, 134, 16);
		add(lblTiempoPromedio);
		
		textField = new JTextField();
		textField.setOpaque(false);
		textField.setDisabledTextColor(Color.DARK_GRAY);
		textField.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		textField.setBackground(UIManager.getColor("Button.background"));
		textField.setEnabled(false);
		textField.setBounds(404, 348, 171, 28);
		add(textField);
		textField.setColumns(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		model.clear();
		textField.setText("");
		
		if(e.getActionCommand().equals("CAMINOAB"))
		{
			DialogoCaminoAB di = new DialogoCaminoAB();
			String origen = di.getOrigen();
			String destino = di.getDestino();
			
			try
			{
				int origin = Integer.parseInt(origen);
				int dest = Integer.parseInt(destino);				
				
				Lista<Estacion> list = main.getShortestPathTwoWay(origin, dest);
				if(list != null)
				{
					displayRoutes(list);
				}
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
			
			//Lista<Camino> routes = main.getRouteFromAB()
		}
		else if(e.getActionCommand().equals("CAMDESDE"))
		{
			String origen = JOptionPane.showInputDialog("Introduzca el id de la estación origen:");
			try
			{
				int origin = Integer.parseInt(origen);
				Lista<Conexion> list = main.pathFromStation(origin);
				if(list != null)
				{
					displayRoutesC(list, origin);
				}
				
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("TIEMPO"))
		{
			DialogoTiempoLimite di = new DialogoTiempoLimite();
			String origen = di.getOrigen();
			System.out.println(origen);
			int tiempo = di.getTiempo();
			try
			{
				int o = Integer.parseInt(origen);
				Lista<Conexion> list = main.pathWithinTimeLimit(o, tiempo);
				if(list != null)
				{
					displayRoutesC(list, o);
				}
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("MAS"))
		{
			String origen = JOptionPane.showInputDialog("Introduzca el id de la estación origen:");
			try
			{
				int o = Integer.parseInt(origen);
				Lista<Estacion> lista = main.longestPath(o);
			
				if(lista != null)
				{
					double tiempo = main.getTime();
					displayRoutes(lista);
					textField.setText(""+tiempo);
				}
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		else if(e.getActionCommand().equals("REC"))
		{
			DialogoRecomendado di = new DialogoRecomendado();
			String estaciones = di.getEstaciones();
			String origen = di.getOrigen();
			
			try
			{
				int o = Integer.parseInt(origen);
				Lista<Estacion> lista = main.pathRecommendation(o, estaciones);
			
				if(lista != null)
				{
					double tiempo = main.getTime();
					displayRoutes(lista);
					textField.setText(""+tiempo);
				}
			}
			catch(NumberFormatException npe)
			{
				JOptionPane.showMessageDialog(this, "Debe ingresar un id válido", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		
	}
	
	
	public void displayRoutes(Lista<Estacion> list)
	{
		model.clear();
		LlamaArrayList<MapMarker> markers = new LlamaArrayList<MapMarker>(list.size());
		LlamaArrayList<LlamaTuple<MapMarker, MapMarker>> routes = new LlamaArrayList<LlamaTuple<MapMarker, MapMarker>>(list.size());
		
		Random r = new Random();
		Color c = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), 128);
		
		MapMarker last = null;
		for(Estacion st: list)
		{
			model.addElement(st);
			MapMarker m = new MapMarker(st.getLatitud(), st.getLongitud());
			m.color = c;
			markers.addAlFinal(m);
			
			if(last == null)
			{
				last = m;
			}
			else
			{
				routes.addAlFinal(new LlamaTuple<MapMarker, MapMarker>(last, m));
				last = m;
			}
		}
		
		mapa.setMarkers(markers);
		mapa.displayRoutes(routes);
	}
	
	
	public void displayRoutesC(Lista<Conexion> list, int origin)
	{
		model.clear();
		LlamaArrayList<MapMarker> markers = new LlamaArrayList<MapMarker>(list.size());
		LlamaArrayList<LlamaTuple<MapMarker, MapMarker>> routes = new LlamaArrayList<LlamaTuple<MapMarker, MapMarker>>(list.size());
		
		Random r = new Random();
		Color col = null; 
		Conexion c1 = list.get(0);
		Estacion o = c1.getOrigen();
		markers.addAlFinal(new MapMarker(o.getLatitud(), o.getLongitud()));
		for(Conexion c: list)
		{
			o = c.getOrigen();
			if(o.getId() == origin)
			{
				col = new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256), 128);
			}
			Estacion d = c.getDestino();
			MapMarker m1 = new MapMarker(o.getLatitud(), o.getLongitud());
			m1.color = col;
			MapMarker m2 = new MapMarker(d.getLatitud(), d.getLongitud());
			m2.color = col;
			
			markers.addAlFinal(m2);
			routes.addAlFinal(new LlamaTuple<MapMarker,MapMarker>(m1, m2));
			
		}
		
		mapa.setMarkers(markers);
		mapa.displayRoutes(routes);
	}
}
