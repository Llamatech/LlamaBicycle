/*
 * Interfaz.java
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import jxl.read.biff.BiffException;

import com.llama.tech.bicycle.backend.BiciException;
import com.llama.tech.bicycle.backend.BicycleManager;
import com.llama.tech.bicycle.backend.Conexion;
import com.llama.tech.bicycle.backend.Estacion;
import com.llama.tech.misc.LlamaTuple;
import com.llama.tech.utils.list.Lista;

public class Interfaz extends JFrame implements ActionListener
{

	public final static String FILE_PATH = "./data/state.llama";
	
	public final static int CAMINOS = 1;
	public final static int CONEXIONES = 2;


	private int estado;
	private VistaConexiones vcon;
	private VistaCaminos vcam;
	private BicycleManager bm;

	public Interfaz()
	{
		File f = new File(FILE_PATH);
		if(!f.exists())
		{
			boolean limitar = JOptionPane.showConfirmDialog(this, "Desea limitar las conexiones por número de viajes?")==JOptionPane.YES_OPTION;
			int limite=0;
			if(limitar)
			{
				while(limitar)
				{
					try
					{
						limitar=false;
						limite = Integer.parseInt(JOptionPane.showInputDialog("Introduzca el limite"));
					}
					catch(NumberFormatException e)
					{
						limitar=true;
						JOptionPane.showMessageDialog(this, "Debe introducir un número","Error", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			
			try 
			{
				bm = new BicycleManager(limite);
			} 
			catch (BiffException | IOException e) 
			{
				JOptionPane.showMessageDialog(this, "Hubo un error durante la carga de los archivos", "Error Fatal", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(-1);//
			}
		}
		else
		{
			try(FileInputStream fis = new FileInputStream(f))
			{
				ObjectInputStream ois = new ObjectInputStream(fis);
				bm = ((BicycleManager) ois.readObject());
				bm.reloadInstance();
			} 
			catch (IOException e) 
			{
				JOptionPane.showMessageDialog(this, "Hubo un error durante la carga de los archivos", "Error Fatal", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(-1);
			} 
			catch (ClassNotFoundException e) 
			{
				JOptionPane.showMessageDialog(this, "Hubo un error durante la carga de los archivos", "Error Fatal", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
				System.exit(-1);
			}
			
		}

		DialogoInicio di = new DialogoInicio();
		estado = di.getEstado();

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(new Dimension(600,580));

		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);

		JMenu vista = new JMenu("Vista");
		mb.add(vista);

		JMenuItem conex = new JMenuItem("Conexiones");
		JMenuItem cam = new JMenuItem("Caminos");

		vista.add(cam);
		vista.add(conex);

		conex.addActionListener(this);
		conex.setActionCommand("CONEX");
		cam.addActionListener(this);
		cam.setActionCommand("CAM");

		inicializar();

	}

	public void inicializar()
	{
		if(estado==CONEXIONES)
		{
			if (vcon==null)
			{
				vcon = new VistaConexiones(this);
				add(vcon, BorderLayout.CENTER);
			}
			else
				vcon.setVisible(true);
			if(vcam!=null)
				vcam.setVisible(false);

		}
		else
		{
			if (vcam==null)
			{
				vcam = new VistaCaminos(this);
				add(vcam, BorderLayout.CENTER);
			}
			else
				vcam.setVisible(true);
			if(vcon!=null)
				vcon.setVisible(false);
		}

		validate();
		repaint();
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("CONEX"))
		{
			estado = CONEXIONES;
			inicializar();
		}
		else if(e.getActionCommand().equals("CAM"))
		{
			estado = CAMINOS;
			inicializar();
		}

	}

	public Lista<Conexion> showConnectionsByOrigin(int id) 
	{
		try 
		{
			 return bm.conexionesDesdeConex(id);
		} 
		catch (BiciException e) 
		{
			JOptionPane.showMessageDialog(this, e.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		
		return null;
	}

	public Lista<Conexion> showConnectionsByDestination(int id) 
	{
		try 
		{
			 return bm.conexionesHaciaConex(id);
		} 
		catch (BiciException e) 
		{
			JOptionPane.showMessageDialog(this, e.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		
		return null;
	}

	public Lista<Estacion> getStationList() 
	{
		return bm.todosEst();
	}

	public void propagateClick(LlamaTuple<Double, Double> pos) 
	{
		bm.selectStation(pos);
	}

	public void disableStation() 
	{
		try 
		{
			bm.deshabilitarEstacion();
		} 
		catch (BiciException e) 
		{
			JOptionPane.showMessageDialog(this, e.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void enableStation(int id) 
	{
		bm.habilitarEstacion(id);	
	}

	public Lista<Conexion> getDisabledConnections() 
	{
		return bm.getCaminosDes();
	}

	public void disableConnection(Conexion selected) 
	{
		bm.deshabilitarCamino(selected.getNumero());
		
	}

	public void enableConnection(Conexion selected) 
	{
		bm.habilitarCamino(selected.getNumero());
		
	}

	public Lista<Estacion> getShortestPathTwoWay(int origin, int dest) 
	{
		try
		{
			return bm.caminoMasCortoABEst(origin, dest);
		}
		catch(BiciException be)
		{
			JOptionPane.showMessageDialog(this, be.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		return null;
	}
	
	
	public static void main(String[] args) 
	{
		Interfaz i = new Interfaz();
		i.setVisible(true);
	}

	public Lista<Conexion> pathFromStation(int origin) 
	{
		try
		{
			return bm.caminosMasCortosConex(origin);
		}
		catch(BiciException be)
		{
			JOptionPane.showMessageDialog(this, be.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		return null;
	}

	public Lista<Conexion> pathWithinTimeLimit(int origin, int tiempo) 
	{
		try
		{
			return bm.esTiempoLimiteConex(origin, tiempo);
		}
		catch(BiciException be)
		{
			JOptionPane.showMessageDialog(this, be.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		return null;
	}

	public Lista<Estacion> pathRecommendation(int origen, String estaciones) 
	{
		try
		{
			return bm.recomendarViajeEst(estaciones, origen);
		}
		catch(BiciException be)
		{
			JOptionPane.showMessageDialog(this, be.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		return null;
	}

	public double getTime() 
	{
		return bm.getTiempo();
	}

	public Lista<Estacion> longestPath(int o) 
	{
		try
		{
			return bm.mayorViajeEst(o);
		}
		catch(BiciException be)
		{
			JOptionPane.showMessageDialog(this, be.getMessage(),"Información", JOptionPane.INFORMATION_MESSAGE);
		}
		return null;
	}

	public Estacion getSelectedStation() 
	{
		return bm.getSeleccionada();
	}
	
	public void serialize()
	{
		File f = new File(FILE_PATH);
		
		try(FileOutputStream fos = new FileOutputStream(f))
		{
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(bm);
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void dispose()
	{
		serialize();
		System.exit(0);
	}

}
