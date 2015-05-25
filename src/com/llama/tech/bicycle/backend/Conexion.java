/*
 * Conexion.java
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

package com.llama.tech.bicycle.backend;

import java.io.Serializable;

/**
 * Clase que describe una conexión entre dos estaciones del sistema
 * @author Llamatech
 */
public class Conexion implements Comparable<Conexion>, Serializable
{

	/**
	 * Atributos de la clase
	 */
	private boolean habilitado;
	
	private int numero;
	private Estacion origen; //Utilizar id o estacion?
	private Estacion destino;
	private int viajes;
	private int segsPromedio;
	private int segsMax;
	
	/**
	 * Constructor principal de la clase.
	 * @param numero Número de identificación de la conexión dentro del sistema.
	 * @param origen Estación de origen.
	 * @param destino Estación de destino.
	 * @param viajes Número de viajes realizados.
	 * @param segsPromedio Tiempo promedio de viaje (Segundos).
	 * @param segsMax Tiempo máximo reportado de viaje (Segundos):
	 */
	public Conexion(int numero, Estacion origen, Estacion destino, int viajes,
			int segsPromedio, int segsMax) 
	{
		this.numero = numero;
		this.origen = origen;
		this.destino = destino;
		this.viajes = viajes;
		this.segsPromedio = segsPromedio;
		this.segsMax = segsMax;
		habilitado=true;
	}
	
	/**
	 * Invierte el origen y el destino de la conexion.
	 */
	public void invert()
	{
		Estacion temp = origen;
		origen = destino;
		destino = temp;
	}

	/**
	 * Retorna el estado actual de la conexión.
	 * @return false si se encuentra deshabilitada, true de lo contrario.
	 */
	public boolean isHabilitado() 
	{
		return habilitado;
	}

	/**
	 * Retorna el número de identificación de la conexión.
	 * @return El número de identificación de la conexión.
	 */
	public int getNumero() 
	{
		return numero;
	}

	/**
	 * Retorna la estación de origen de la conexión
	 * @return El origen de la conexión != null.
	 */
	public Estacion getOrigen() 
	{
		return origen;
	}

	/**
	 * Retorna la estación de destino de la conexión
	 * @return El destino de la conexión != null.
	 */
	public Estacion getDestino() 
	{
		return destino;
	}

	/**
	 * Retorna el número de viajes realizados a través de la conexión.
	 * @return El número de viajes. 
	 */
	public int getViajes()
	{
		return viajes;
	}

	/**
	 * Retorna el tiempo promedio de recorrido de la conexión.
	 * @return El tiempo promedio de viaje. 
	 */
	public int getSegsPromedio() 
	{
		return segsPromedio;
	}

	/**
	 * Retorna el mayor tiempo de recorrido de la conexión registrado hasta el momento. 
	 * @return El mayor tiempo de recorrido. 
	 */
	public int getSegsMax() 
	{
		return segsMax;
	}

	/**
	 * Establece el estado actual de la estación.
	 * @param habilitado El nuevo estado de la estación. True si se desea habilitar, false, de lo contrario.
	 */
	public void setHabilitado(boolean habilitado) 
	{
		this.habilitado = habilitado;
	}
	
	@Override
	public String toString()
	{
		return "Habilitada: "+habilitado+" | Número: "+ numero + " | Origen: "+"(Id: "+origen.getId()+") "+ origen.getNombre() + " - Destino: (Id:"+destino.getId()+")"+ destino.getNombre() + " | Viajes:" + viajes;//+segsPromedio+" "+segsMax;
	}

	@Override
	public int compareTo(Conexion o)
	{
		return 0;
	}
	
	
	public Conexion clone()
	{
		return new Conexion(numero, origen, destino, viajes, segsPromedio, segsMax);
	}
	
	

}
