package com.llama.tech.bicycle.backend;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import com.llama.tech.misc.LlamaTuple;

public class Estacion implements Comparable<Estacion> ,Serializable{
	
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
	
	public Estacion(String nombre, int id, String terminal, String municipio,
			double latitud, double longitud, int docks, LocalDate instalacion,
			LocalDate ultimoDia) {
		super();
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

	public boolean isHabilitada() {
		return habilitada;
	}

	public boolean isSeleccionada() {
		return seleccionada;
	}

	public String getNombre() {
		return nombre;
	}

	public int getId() {
		return id;
	}

	public String getTerminal() {
		return terminal;
	}

	public String getMunicipio() {
		return municipio;
	}

	public double getLatitud() {
		return latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public int getDocks() {
		return docks;
	}

	public LocalDate getInstalacion() {
		return instalacion;
	}

	public LocalDate getUltimoDia() {
		return ultimoDia;
	}

	public void setHabilitada(boolean habilitada) {
		this.habilitada = habilitada;
	}

	public void setSeleccionada(boolean seleccionada) {
		this.seleccionada = seleccionada;
	}

	@Override
	public int compareTo(Estacion o) {
		// TODO Auto-generated method stub
		return id-o.getId();
	}
	
	@Override
	public String toString()
	{
		return id + " " + nombre + " " + terminal + " " + municipio + " "+ latitud + " "+ longitud + " "+ docks+ " " + instalacion + " "+ ultimoDia;
	}
	
	public LlamaTuple<Double, Double> darUbicacion()
	{
		return new LlamaTuple<Double, Double>(latitud, longitud);
	}
	
	
	
	
	

}
