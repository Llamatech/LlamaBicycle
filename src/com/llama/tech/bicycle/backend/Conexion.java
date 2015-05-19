package com.llama.tech.bicycle.backend;

import java.io.Serializable;

public class Conexion implements Comparable<Conexion>, Serializable{
	
	private boolean habilitado;
	
	private int numero;
	private Estacion origen; //Utilizar id o estacion?
	private Estacion destino;
	private int viajes;
	private int segsPromedio;
	private int segsMax;
	
	public Conexion(int numero, Estacion origen, Estacion destino, int viajes,
			int segsPromedio, int segsMax) {
		super();
		this.numero = numero;
		this.origen = origen;
		this.destino = destino;
		this.viajes = viajes;
		this.segsPromedio = segsPromedio;
		this.segsMax = segsMax;
		habilitado=true;
	}
	
	public void invert()
	{
		Estacion temp = origen;
		origen = destino;
		destino = temp;
	}

	public boolean isHabilitado() {
		return habilitado;
	}

	public int getNumero() {
		return numero;
	}

	public Estacion getOrigen() {
		return origen;
	}

	public Estacion getDestino() {
		return destino;
	}

	public int getViajes() {
		return viajes;
	}

	public int getSegsPromedio() {
		return segsPromedio;
	}

	public int getSegsMax() {
		return segsMax;
	}

	public void setHabilitado(boolean habilitado) {
		this.habilitado = habilitado;
	}
	
	@Override
	public String toString()
	{
		return "Habilitada: "+habilitado+" | Número: "+ numero + " | Origen: "+"(Id: "+origen.getId()+") "+ origen.getNombre() + " - Destino: (Id:"+destino.getId()+")"+ destino.getNombre() + " | Viajes:" + viajes;//+segsPromedio+" "+segsMax;
	}

	@Override
	public int compareTo(Conexion o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public Conexion clone()
	{
		return new Conexion(numero, origen, destino, viajes, segsPromedio, segsMax);
	}
	
	

}
