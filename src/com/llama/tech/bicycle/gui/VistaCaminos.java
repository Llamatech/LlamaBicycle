package com.llama.tech.bicycle.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class VistaCaminos extends JPanel implements ActionListener
{
	private JTextArea txtrConexiones;
	private JLabel lblMapa;
	private JTextField textField;
	private Interfaz main;
	
	public VistaCaminos(Interfaz principal) 
	{
		main = principal;
		setMinimumSize(new Dimension(500, 500));
		setLayout(null);
		
		lblMapa = new JLabel("mapa");	
		lblMapa.setBounds(22, 22, 370, 347);
		add(lblMapa);
		
		JButton btnMostrarConexionesDesde = new JButton("<HTML>Camino más corto\n <br><center>\nde A a B\n</center></HTML>");
		btnMostrarConexionesDesde.setActionCommand("CAMINOAB");
		btnMostrarConexionesDesde.addActionListener(this);
		btnMostrarConexionesDesde.setBounds(404, 22, 171, 47);
		add(btnMostrarConexionesDesde);
		
		JButton btnHabilitardeshabilitarConexin = new JButton("Habilitar/Deshabilitar Estación\n\n");
		btnHabilitardeshabilitarConexin.setBounds(169, 501, 240, 29);
		add(btnHabilitardeshabilitarConexin);
		
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
		
		txtrConexiones = new JTextArea();
		scrollPane.setViewportView(txtrConexiones);
		
		JLabel lblTiempoPromedio = new JLabel("Tiempo promedio:");
		lblTiempoPromedio.setBounds(404, 320, 134, 16);
		add(lblTiempoPromedio);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setBounds(404, 348, 171, 28);
		add(textField);
		textField.setColumns(10);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("CAMINOAB"))
		{
			DialogoCaminoAB di = new DialogoCaminoAB();
			String origen = di.getOrigen();
			String destino = di.getDestino();
		}
		else if(e.getActionCommand().equals("CAMDESDE"))
		{
			String origen = JOptionPane.showInputDialog("Introduzca el id de la estación origen:");
		}
		else if(e.getActionCommand().equals("TIEMPO"))
		{
			DialogoTiempoLimite di = new DialogoTiempoLimite();
			String origen = di.getOrigen();
			System.out.println(origen);
			int tiempo = di.getTiempo();
		}
		else if(e.getActionCommand().equals("MAS"))
		{
			String origen = JOptionPane.showInputDialog("Introduzca el id de la estación origen:");
		}
		else if(e.getActionCommand().equals("REC"))
		{
			DialogoRecomendado di = new DialogoRecomendado();
			String estaciones = di.getEstaciones();
			String origen = di.getOrigen();
		}
		
		
	}
}
