/*
 * Estacion.java
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import com.llama.tech.misc.LlamaTuple;

/**
 * Clase que describe una estación del sistema.
 * @author Llamatech
 */
public class Estacion implements Comparable<Estacion> ,Serializable
{
	
	/**
	 * Atributos de la clase.
	 */
	private boolean habilitada;
	private boolean seleccionada;
	private String nombre;
	private int id;
	private String terminal;
	private String municipio;
	private double latitud;
	private double longitud;
	private int docks;
	private LocalDate instalacion;
	private LocalDate ultimoDia;
	
	/**
	 * Constructor principal de la clase.
	 * @param nombre Nombre de la estación. nombre != null.
	 * @param id Número de identificación de la estación dentro del sistema.
	 * @param terminal Establece si la estación es terminal.
	 * @param municipio Establece la localidad en la cual se encuentra la estación. municipio != null.
	 * @param latitud Latitud geográfica de la ubicación de la estación. 
	 * @param longitud Longitud geográfica de la ubicación de la estación
	 * @param docks Total de bicicletas disponibles en la estación.
	 * @param instalacion Fecha de instalación de la estación.
	 * @param ultimoDia Fecha de desinstalación de la estación, si la estación no existe en el presente.
	 */
	public Estacion(String nombre, int id, String terminal, String municipio,
			double latitud, double longitud, int docks, LocalDate instalacion,
			LocalDate ultimoDia) 
	{
		this.nombre = nombre;
		this.id = id;
		this.terminal = terminal;
		this.municipio = municipio;
		this.latitud = latitud;
		this.longitud = longitud;
		this.docks = docks;
		this.instalacion = instalacion;
		this.ultimoDia = ultimoDia;
		habilitada=true;
		seleccionada=false;
	}

	/**
	 * Retorna el estado actual de funcionamiento de la estación.
	 * @return true si se encuentra habilitada, false de lo contrario.
	 */
	public boolean isHabilitada() 
	{
		return habilitada;
	}

	/**
	 * Retorna si la estación se encuentra seleccionada
	 * @return true si se encuentra seleccionada, false, de lo contrario.
	 */
	public boolean isSeleccionada() 
	{
		return seleccionada;
	}

	/**
	 * Retorna el nombre de la estación.
	 * @return El nombre de la estación. != null.
	 */
	public String getNombre() 
	{
		return nombre;
	}

    /**
     * Retorna el número de identificación de la estación al interior del sistema.
     * @return El número de identificación de la estación.
     */
	public int getId() 
	{
		return id;
	}

	/**
	 * Retorna el terminal de la estación.
	 * @return El terminal de la estación.
	 */
	public String getTerminal() 
	{
		return terminal;
	}

	/**
	 * Retorna la localidad en la cual la estación se encuentra situada.
	 * @return El municipio en el cual la estación se encuentra instalada.
	 */
	public String getMunicipio() 
	{
		return municipio;
	}

	/**
	 * Retorna la latitud geográfica de la posición de la estación.
	 * @return La latitud geográfica de la posición de la estación.
	 */
	public double getLatitud() 
	{
		return latitud;
	}

	/**
	 * Retorna la longitud geográfica de la posición de la estación.
	 * @return La longitud geográfica de la posición de la estación.
	 */
	public double getLongitud() 
	{
		return longitud;
	}

	/**
	 * Retorna el número total de bicicletas disponibles en la estación.
	 * @return El número total de bicicletas disponibles en la estación.
	 */
	public int getDocks() 
	{
		return docks;
	}

	/**
	 * Retorna la fecha de instalación de la estación.
	 * @return Fecha de instalación.
	 */
	public LocalDate getInstalacion() 
	{
		return instalacion;
	}

	/**
	 * Retorna la fecha de último servicio de la estación.
	 * @return Fecha de desinstalación.
	 */
	public LocalDate getUltimoDia() 
	{
		return ultimoDia;
	}

	/**
	 * Establece el estado de servicio de la estación
	 * @param habilitada. true si se desea habilitar la estación, false de lo contrario.
	 */
	public void setHabilitada(boolean habilitada) 
	{
		this.habilitada = habilitada;
	}

	/**
	 * Establece si la estación se encuentra seleccionada.
	 * @param seleccionada true si se desea seleccionar la estación, false de lo contrario.
	 */
	public void setSeleccionada(boolean seleccionada) 
	{
		this.seleccionada = seleccionada;
	}
	
	/**
	 * Retorna la posición geográfica de la estación
	 * @return Una tupla (Lat, Long) que contiene la ubicación de la estación.
	 */
	public LlamaTuple<Double, Double> darUbicacion()
	{
		return new LlamaTuple<Double, Double>(latitud, longitud);
	}

	@Override
	public int compareTo(Estacion o) 
	{
		return id-o.getId();
	}
	
	@Override
	public String toString()
	{
		return id + " " + nombre + " " + terminal + " " + municipio + " "+ latitud + " "+ longitud + " "+ docks+ " " + instalacion + " "+ ultimoDia;
	}
	

}
