/*
 * BicycleManager.java
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

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import co.edu.uniandes.cupi2.estructuras.grafoDirigido.IArco;
import co.edu.uniandes.cupi2.estructuras.grafoDirigido.ICamino;
import co.edu.uniandes.cupi2.estructuras.grafoDirigido.ICaminosMinimos;
import co.edu.uniandes.cupi2.estructuras.grafoDirigido.IVertice;

import com.llama.tech.bicycle.gis.LlamaMapsAPI;
import com.llama.tech.misc.LlamaTuple;
import com.llama.tech.utils.dict.Dictionary;
import com.llama.tech.utils.dict.LlamaDict;
import com.llama.tech.utils.graph.Camino;
import com.llama.tech.utils.graph.CaminoMinimo;
import com.llama.tech.utils.graph.GraphEdge;
import com.llama.tech.utils.graph.GraphVertex;
import com.llama.tech.utils.graph.LlamaGraph;
import com.llama.tech.utils.list.Lista;
import com.llama.tech.utils.list.LlamaArrayList;

/**
 * Clase que describe, define y enlaza las funciones principales del funcionamiento del programa.
 * @author Llamatech
 *
 */
public class BicycleManager implements Serializable
{
	/**
	 * Constantes que establecen restricciones sobre la carga de los archivos principales del sistema.
	 */
	public static final String RUTA_ESTACIONES = "data/Estaciones-Hubway.xls";
	public static final String RUTA_CONEXIONES = "data/Conexiones-Hubway.xls";
	public static final int NOLIMIT = 0;

	/**
	 * Atributos de la clase
	 */
	private LlamaGraph<Integer, Estacion, Conexion> grafo;      
	private Estacion seleccionada;
	private Dictionary<Integer, Conexion> caminos;
	private Dictionary<Integer, Estacion> estaciones;
	private Lista<Conexion> caminosDes;
	private Lista<Estacion> estacionesDes;
	private Dictionary<Integer, Lista<GraphEdge<Integer, Estacion, Conexion>>> conexionesDeDes;
	double tiempo;
	int longitud;

	/**
	 * Constructor del sistema de control de rutas.
	 * @param limite Límite del número de conexiones a cargar y visualizar.
	 * @throws BiffException Si ocurre un error al procesar un archivo XLS
	 * @throws IOException Si ocurre un error de Entrada/Salida
	 */
	public BicycleManager(int limite) throws BiffException, IOException
	{
		grafo = new LlamaGraph<Integer, Estacion, Conexion>();
		caminos = new LlamaDict<Integer, Conexion>(20000);
		estaciones = new LlamaDict<Integer,Estacion>(200);
		caminosDes = new LlamaArrayList<>(20);
		estacionesDes= new LlamaArrayList<>(20);
		conexionesDeDes = new LlamaDict<Integer, Lista<GraphEdge<Integer,Estacion,Conexion>>>(20);
		cargarEstaciones();
		cargarConexiones(limite);
	}

	/**
	 * Función principal para la carga de los archivos principales sin restricción sobre
	 * el número de conexiones.
	 * @throws BiffException Si ocurre un error al procesar un archivo XLS
	 * @throws IOException Si ocurre un error de Entrada/Salida
	 */
	public void cargarEstaciones() throws BiffException, IOException
	{
		File f = new File(RUTA_ESTACIONES);

		Workbook wb = Workbook.getWorkbook(f);
		Sheet hoja = wb.getSheet(0);
		Cell[] ids= hoja.getColumn(hoja.findCell("id").getColumn());
		Cell[] terminales = hoja.getColumn(hoja.findCell("terminal").getColumn());
		Cell[] nombres = hoja.getColumn(hoja.findCell("estacion").getColumn());
		Cell[] municipios = hoja.getColumn(hoja.findCell("municipio").getColumn());
		Cell[] latitudes = hoja.getColumn(hoja.findCell("latitud").getColumn());
		Cell[] longitudes = hoja.getColumn(hoja.findCell("longitud").getColumn());
		Cell[] docks = hoja.getColumn(hoja.findCell("nb_docks").getColumn());
		Cell[] instalaciones = hoja.getColumn(hoja.findCell("fecha_instalacion").getColumn());
		Cell[] ultimos = hoja.getColumn(hoja.findCell("ultimo_dia").getColumn());

		for(int i =1; i<ids.length;i++)
		{
			LocalDate ultimo=LocalDate.MIN;
			LocalDate instalacion=LocalDate.MIN;
			int docksi =0;
			try
			{
				System.out.println(instalaciones[i].getContents());
				String[] insta = instalaciones[i].getContents().split("/");
				instalacion = LocalDate.of(Integer.parseInt(insta[2]), Integer.parseInt(insta[0]), Integer.parseInt(insta[1]));
				String[] ulti = ultimos[i].getContents().split("/");
				ultimo = LocalDate.of(Integer.parseInt(ulti[2]), Integer.parseInt(ulti[0]), Integer.parseInt(ulti[1]));
				docksi = Integer.parseInt(docks[i].getContents());
			}
			catch(Exception e)
			{

			}

			Estacion e = new Estacion(nombres[i].getContents(), Integer.parseInt(ids[i].getContents()), terminales[i].getContents(), municipios[i].getContents(), Double.parseDouble(latitudes[i].getContents()), Double.parseDouble(longitudes[i].getContents()), docksi, instalacion, ultimo);
			grafo.agregarVertice(e.getId(), e);
			estaciones.addEntry(e.getId(), e);
			System.out.println(e);

		}		
	}

	/**
	 * Función principal para la carga de los archivos principales con restricción sobre
	 * el número de conexiones.
	 * @param limite Límite de estaciones a cargar.
	 * @throws BiffException Si ocurre un error al procesar un archivo XLS
	 * @throws IOException Si ocurre un error de Entrada/Salida
	 */
	public void cargarConexiones(int limite) throws BiffException, IOException
	{
		File f = new File(RUTA_CONEXIONES);

		Workbook wb = Workbook.getWorkbook(f);
		Sheet hoja = wb.getSheet(0);

		//		Cell[] ids = hoja.getColumn(hoja.findCell("No Arco").getColumn());
		//		Cell[] origenes = hoja.getColumn(hoja.findCell("Estacion Inicial").getColumn());
		//		Cell[] destinos = hoja.getColumn(hoja.findCell("Estacion Final").getColumn());
		//		Cell[] noviajes = hoja.getColumn(hoja.findCell("Número de Viajes").getColumn());
		//		Cell[] tpromedio = hoja.getColumn(hoja.findCell("Duración Promedio (seg)").getColumn());
		//		Cell[] tmax = hoja.getColumn(hoja.findCell("Duración Máxima").getColumn());

		Cell[] ids = hoja.getColumn(0);
		Cell[] origenes = hoja.getColumn(1);
		Cell[] destinos = hoja.getColumn(2);
		Cell[] noviajes = hoja.getColumn(3);
		Cell[] tpromedio = hoja.getColumn(4);
		Cell[] tmax = hoja.getColumn(5);

		for(int i =1; i<ids.length;i++)
		{
			try
			{
				int viajes = Integer.parseInt(noviajes[i].getContents());
				Estacion origen = grafo.darVertice(Integer.parseInt(origenes[i].getContents())).darValor();
				Estacion destino = grafo.darVertice(Integer.parseInt(destinos[i].getContents())).darValor();
				if(viajes>=limite)
				{
					Conexion c = new Conexion(Integer.parseInt(ids[i].getContents()), origen, destino, viajes, Integer.parseInt(tpromedio[i].getContents()), Integer.parseInt(tmax[i].getContents()));
					grafo.agregarArco(origen.getId(), destino.getId(), c, c.getSegsPromedio());
					caminos.addEntry(c.getNumero(), c);
					System.out.println(c.getNumero());
				}
			}
			catch(NullPointerException e)
			{
				System.out.println("paso algo raro");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Habilita una estación del sistema, dado su número de identificación.
	 * @param id El número de identificiación de la estación.
	 */
	public void habilitarEstacion(int id)
	{
		Estacion e = estaciones.getValue(id);
		grafo.agregarVertice(id, e);
		for(GraphEdge<Integer, Estacion, Conexion> ex:conexionesDeDes.getValue(e.getId()))
		{
			grafo.agregarArco(ex.darOrigen().darId(), ex.darDestino().darId(), ex.darInfoArco(), ex.darCosto());
		}
		e.setHabilitada(true);
		estacionesDes.remove(e);

	}

	/**
	 * Deshabilita la estación del sistema actualmente seleccionada por el usuario
	 * @throws BiciException Si no hay una estación seleccionada previamente.
	 */
	public void deshabilitarEstacion() throws BiciException
	{
		if (seleccionada==null)
			throw new BiciException("No hay ninguna estacion seleccionada");
		seleccionada.setHabilitada(false);
		estacionesDes.addAlFinal(seleccionada);
		conexionesDeDes.addEntry(seleccionada.getId(), ((GraphVertex<Integer, Estacion, Conexion>)grafo.darVertice(seleccionada.getId())).getEdgesFrom());
		grafo.eliminarVertice(seleccionada.getId());
		seleccionada=null;
	}

	/**
	 * Habilita una conexión del sistema, dado un número de identificación.
	 * @param numero El número de identificación de la conexión.
	 */
	public void habilitarCamino(int numero)
	{
		Conexion c = caminos.getValue(numero);
		grafo.agregarArco(c.getOrigen().getId(), c.getDestino().getId(), c, c.getSegsPromedio());
		c.setHabilitado(true);
		for (int i = 0; i < caminosDes.size(); i++) {
			if(caminosDes.get(i).getNumero()==c.getNumero())
			{
				caminosDes.remove(i);
				break;
			}
		}
		
	}

	/**
	 * Desabilita una conexión del sistema, dado un número de identificación.
	 * @param cx El número de identificación de la conexión.
	 */
	public void deshabilitarCamino(int cx)
	{	
		Conexion c = caminos.getValue(cx);
		grafo.eliminarArco(c.getOrigen().getId(), c.getDestino().getId());
		c.setHabilitado(false);
		caminosDes.addAlFinal(c);
	}

	/**
	 * Establece la estación actualmente seleccionada por el usuario, sujeto a 
	 * la distancia de la misma al punto seleccionado por el usuario. 
	 * @param es Pareja de coordenadas geográficas (Lat, Long)
	 */
	public void selectStation(LlamaTuple<Double, Double> es)
	{
		if(seleccionada!=null)
			seleccionada.setSeleccionada(false);
		Iterator<IVertice<Integer, Estacion, Conexion>> it = grafo.darVertices();
		seleccionada = null;
		while(it.hasNext())
		{
			Estacion e = it.next().darValor();

			if(LlamaMapsAPI.distanciaEntreGrados(es.x, es.y, e.getLatitud(), e.getLongitud()) <= 0.1)
			{
				seleccionada=e;
				break;
			}
		}
		if(seleccionada != null)
		{
			seleccionada.setSeleccionada(true);
		}
	}

	/**
	 * Retorna una lista de conexiones salientes desde una estación identificada con número id.
	 * @param id El número de identificación de la estación solicitada.
	 * @return Una lista con las Conexiones salientes desde la estación.
	 * @throws BiciException Si no existe una estación con el id solicitado.
	 */
	public Lista<Conexion> conexionesDesdeConex(int id) throws BiciException
	{
		try{
			Estacion origen = grafo.darVertice(id).darValor();
			if(!origen.isHabilitada())
				throw new BiciException("La estación seleccionada no se encuentra habilitada");
			Lista<Conexion> conexiones = new LlamaArrayList<Conexion>(20);
			Iterator<IArco<Integer, Estacion, Conexion>> it=grafo.darVertice(id).darSucesores();
			while(it.hasNext())
			{
				Conexion c = it.next().darInfoArco();
				if(c.getDestino().isHabilitada()&&c.isHabilitado())
				{
					conexiones.addAlFinal(c);
				}
			}
			return conexiones;
		}
		catch(NullPointerException e)
		{
			throw new BiciException("No se encuentra ninguna estación con ese id");
		}
	}

	/**
	 * Retorna una lista de estaciones que conforman una conexión saliente desde una estación 
	 * identificada con número id.
	 * @param id El número de identificación de la estación solicitada.
	 * @return Una lista con las Estaciones que conforman una conexión saliente desde la estación.
	 * @throws BiciException Si no existe una estación con el id solicitado.
	 */
	public Lista<Estacion> conexionesDesdeEst(int id) throws BiciException
	{
		try{
			Estacion origen = grafo.darVertice(id).darValor();
			if(!origen.isHabilitada())
				throw new BiciException("La estación seleccionada no se encuentra habilitada");
			Lista<Estacion> conexiones = new LlamaArrayList<Estacion>(200);
			conexiones.addAlFinal(origen);
			Iterator<IArco<Integer, Estacion, Conexion>> it=grafo.darVertice(id).darSucesores();
			while(it.hasNext())
			{
				Conexion c = it.next().darInfoArco();
				if(c.getDestino().isHabilitada()&&c.isHabilitado())
				{
					conexiones.addAlFinal(c.getDestino());
				}
			}
			return conexiones;
		}
		catch(NullPointerException e)
		{
			throw new BiciException("No se encuentra ninguna estación con ese id");
		}
	}

	/**
	 * Retorna una lista que contiene todas las conexiones del sistema.
	 * @return Una lista que contiene todas las Conexiones del sistema.
	 */
	public Lista<Conexion> todosConex()
	{
		Lista<Conexion> lista = new LlamaArrayList<>(20000);
		Iterator<Conexion> it = caminos.getValues();
		while(it.hasNext())
		{
			Conexion c = it.next();
			if(c.getOrigen().isHabilitada()&&c.getDestino().isHabilitada()&&c.isHabilitado())
				lista.addAlFinal(c);
		}

		return lista;

	}

	/**
	 * Retorna una lista que contiene todas las estaciones del sistema.
	 * @return Una lista que contiene todas las estaciones del sistema.
	 */
	public Lista<Estacion> todosEst()
	{
		Lista<Estacion> lista = new LlamaArrayList<>(200);
		Iterator<IVertice<Integer, Estacion, Conexion>> it = grafo.darVertices();

		while(it.hasNext())
		{
			Estacion e = it.next().darValor();
			if(e.isHabilitada())
				lista.addAlFinal(e);
		}

		return lista;
	}

	/**
	 * Retorna una lista que contiene las conexiones entrantes a una estación 
	 * identificada con número id.
	 * @param id El número de identificación de la estación solicitada.
	 * @return Una lista con las Conexiones entrantes a la estación.
	 * @throws BiciException Si no existe una estación con el id solicitado.
	 */
	public Lista<Conexion> conexionesHaciaConex(int id) throws BiciException 
	{
		try
		{
			Estacion destino = grafo.darVertice(id).darValor();
			Lista<Conexion> lista = new LlamaArrayList<>(20000);
			if(!destino.isHabilitada())
				throw new BiciException("La estación destino no está habilitada");
			GraphVertex<Integer, Estacion, Conexion> dest = (GraphVertex<Integer, Estacion, Conexion>)grafo.darVertice(id);
			for(GraphEdge<Integer, Estacion, Conexion> e:dest.getEdgesFrom())
			{
				Conexion c = e.darInfoArco();
				if(c.isHabilitado()&&c.getOrigen().isHabilitada())
				{
					lista.addAlFinal(c);
				}
			}
			return lista;
		}
		catch (NullPointerException e)
		{
			throw new BiciException("No existe una estación con ese id");
		}


	}

	/**
	 * Retorna una lista que contiene las estaciones que conforman todas las conexiones 
	 * entrantes a una estación identificada con número id.
	 * @param id El número de identificación de la estación solicitada.
	 * @return Una lista que contiene las estaciones que conforman todas las conexiones 
	 * entrantes a la estación.
	 * @throws BiciException Si no existe una estación con el id solicitado.
	 */
	public Lista<Estacion> conexionesHaciaEst(int id) throws BiciException
	{
		try
		{
			Estacion destino = grafo.darVertice(id).darValor();
			Lista<Estacion> lista = new LlamaArrayList<>(200);
			if(!destino.isHabilitada())
				throw new BiciException("La estación destino no está habilitada");
			lista.addAlFinal(destino);
			GraphVertex<Integer, Estacion, Conexion> dest = (GraphVertex<Integer, Estacion, Conexion>)grafo.darVertice(id);
			for(GraphEdge<Integer, Estacion, Conexion> e:dest.getEdgesFrom())
			{
				Conexion c = e.darInfoArco();
				if(c.isHabilitado()&&c.getOrigen().isHabilitada())
				{
					lista.addAlFinal(c.getOrigen());
				}
			}

			return lista;
		}
		catch (NullPointerException e)
		{
			throw new BiciException("No existe una estación con ese id");
		}
	}

	/**
	 * Establece las Estaciones que conforman el camino más corto entre dos estaciones.
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @param idDestino El número de identificación de la estación de destino.
	 * @return Una lista de Estaciones que conforman el camino más corto entre dos estaciones.
	 * @throws BiciException Si el id de la estación de origen/destino, no existe.
	 */
	public Lista<Estacion> caminoMasCortoABEst(int idOrigen, int idDestino) throws BiciException
	{

		IVertice<Integer, Estacion, Conexion> destino = grafo.darVertice(idDestino);
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);

		if(destino==null)
			throw new BiciException("La estacoin destino no existe");
		if(origen==null)
			throw new BiciException("La estacion origen no existe");

		Lista<Estacion> lista = new LlamaArrayList<>(200);

		ICamino<Integer, Estacion, Conexion> camino = grafo.darCaminoMasBarato(idOrigen, idDestino);
		if(camino==null)
			throw new BiciException("No hay camino entre estas estaciones");
		tiempo+=camino.darCosto();
		longitud+=camino.darLongitud();
		Iterator<IVertice<Integer, Estacion, Conexion>> it = camino.darVertices();

		while(it.hasNext())
		{
			Estacion e = it.next().darValor();

			lista.addAlFinal(e);
		}
		return lista;

	}
	
	/**
	 * Establece las Conexiones que conforman el camino más corto entre dos estaciones.
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @param idDestino El número de identificación de la estación de destino.
	 * @return Una lista de Conexiones que conforman el camino más corto entre dos estaciones.
	 * @throws BiciException Si el id de la estación de origen/destino, no existe.
	 */
	public Lista<Conexion> caminoMasCortoABConex(int idOrigen, int idDestino) throws BiciException
	{

		IVertice<Integer, Estacion, Conexion> destino = grafo.darVertice(idDestino);
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idDestino);

		if(destino==null)
			throw new BiciException("La estacion destino no existe");
		if(origen==null)
			throw new BiciException("La estacion origen no existe");

		Lista<Conexion> lista = new LlamaArrayList<>(200);

		ICamino<Integer, Estacion, Conexion> camino = grafo.darCaminoMasBarato(idOrigen, idDestino);
		Iterator<IArco<Integer, Estacion, Conexion>> it = camino.darArcos();

		while(it.hasNext())
		{
			Conexion c= it.next().darInfoArco();

			lista.addAlFinal(c);
		}
		return lista;

	}

	/**
	 * Establece las estaciones que conforman los caminos más cortos desde una estación de origen
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @return Una lista que contiene las estaciones que conforman los caminos más cortos desde la estación
	 * @throws BiciException Si el número de identificación de la estación no existe.
	 */
	public Lista<Estacion> caminosMasCortosEst(int idOrigen) throws BiciException
	{
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);
		if(origen==null)
			throw new BiciException("La estacion origen no existe");
		Lista<Estacion> lista = new LlamaArrayList<>(20);

		CaminoMinimo<Integer, Estacion, Conexion> cams = (CaminoMinimo<Integer, Estacion, Conexion>)grafo.darCaminosMinimos(idOrigen);
		Iterator<Camino<Integer, Estacion, Conexion>> it = cams.darCaminos();
		while(it.hasNext())
		{
			Camino<Integer, Estacion, Conexion> c = it.next();
			Iterator<IVertice<Integer, Estacion, Conexion>> itv = c.darVertices();
			while(itv.hasNext())
			{
				lista.addAlFinal(itv.next().darValor());
			}
		}

		return lista;
	}

	/**
	 * Establece las conexiones que conforman los caminos más cortos desde una estación de origen
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @return Una lista que contiene las conexiones que conforman los caminos más cortos desde la estación
	 * @throws BiciException Si el número de identificación de la estación no existe.
	 */
	public Lista<Conexion> caminosMasCortosConex(int idOrigen) throws BiciException
	{
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);
		if(origen==null)
			throw new BiciException("La estacion origen no existe");
		Lista<Conexion> lista = new LlamaArrayList<>(20);

		CaminoMinimo<Integer, Estacion, Conexion> cams = (CaminoMinimo<Integer, Estacion, Conexion>)grafo.darCaminosMinimos(idOrigen);
		System.out.println();
		Iterator<Camino<Integer, Estacion, Conexion>> it = cams.darCaminos();
		while(it.hasNext())
		{
			Camino<Integer, Estacion, Conexion> c = it.next();
			Iterator<IArco<Integer, Estacion, Conexion>> itv = c.darArcos();
			while(itv.hasNext())
			{
				IArco<Integer,Estacion,Conexion> cx = itv.next();
				System.out.println("Este es la conex: "+cx.darInfoArco()+"Y este es su tiempo: "+cx.darCosto());
				lista.addAlFinal(cx.darInfoArco());
			}
		}

		return lista;
	}

	/**
	 * Retorna las estaciones que pueden ser recorridas, partiendo desde una estación de origen 
	 * dentro de un tiempo límite.
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @param t El tiempo límite de recorrido.
	 * @return Una lista que contiene las estaciones que pueden ser recorridas dentro del tiempo límite a partir
	 * de la estación.
	 * @throws BiciException Si el número de identificación de la estación de origen no existe.
	 */
	public Lista<Estacion> estTiempoLimiteEst(int idOrigen, int t) throws BiciException
	{
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);
		if(origen==null)
			throw new BiciException("La estacion origen no existe");
		Lista<Estacion> lista = new LlamaArrayList<>(20);

		CaminoMinimo<Integer, Estacion, Conexion> cams = (CaminoMinimo<Integer, Estacion, Conexion>)grafo.darCaminosMinimos(idOrigen);
		Iterator<Camino<Integer, Estacion, Conexion>> it = cams.darCaminos();
		while(it.hasNext())
		{
			Camino<Integer, Estacion, Conexion> c = it.next();
			if(c.darCosto()<=t)
			{
				Iterator<IVertice<Integer, Estacion, Conexion>> itv = c.darVertices();
				while(itv.hasNext())
				{
					lista.addAlFinal(itv.next().darValor());
				}
			}
		}

		return lista;
	}

	/**
	 * Retorna las conexiones que pueden ser recorridas, partiendo desde una estación de origen 
	 * dentro de un tiempo límite.
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @param t El tiempo límite de recorrido. t > 0.
	 * @return Una lista que contiene las conexiones que pueden ser recorridas dentro del tiempo límite a partir
	 * de la estación.
	 * @throws BiciException Si el número de identificación de la estación de origen no existe.
	 */
	public Lista<Conexion> esTiempoLimiteConex(int idOrigen, int t) throws BiciException
	{
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);
		if(origen==null)
			throw new BiciException("La estacion origen no existe");
		Lista<Conexion> lista = new LlamaArrayList<>(20);

		CaminoMinimo<Integer, Estacion, Conexion> cams = (CaminoMinimo<Integer, Estacion, Conexion>)grafo.darCaminosMinimos(idOrigen);
		Iterator<Camino<Integer, Estacion, Conexion>> it = cams.darCaminos();
		while(it.hasNext())
		{
			Camino<Integer, Estacion, Conexion> c = it.next();
			if(c.darCosto()<=t)
			{
				Iterator<IArco<Integer, Estacion, Conexion>> itv = c.darArcos();
				while(itv.hasNext())
				{
					lista.addAlFinal(itv.next().darInfoArco());
				}
			}
		}

		return lista;
	}

	/**
	 * Retorna las estaciones que conforman el viaje de mayor lognitud desde una estación de origen.
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @return Una lista que contiene las estaciones que conforman el viaje de mayor lognitud desde una estación de origen.
	 * @throws BiciException Si el número de identificación de la estación de origen, no existe.
	 */
	public Lista<Estacion> mayorViajeEst(int idOrigen) throws BiciException
	{
		longitud=0;
		tiempo=0;
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);
		if(origen==null)
			throw new BiciException("La estacion origen no existe");
		Lista<Estacion> lista = new LlamaArrayList<>(20);

		CaminoMinimo<Integer, Estacion, Conexion> cams = (CaminoMinimo<Integer, Estacion, Conexion>)grafo.darCaminosMinimos(idOrigen);
		Iterator<Camino<Integer, Estacion, Conexion>> it = cams.darCaminos();
		Camino<Integer,Estacion,Conexion> mayor=null;
		if(it.hasNext())
			mayor = it.next();
		while(it.hasNext())
		{
			Camino<Integer, Estacion, Conexion> c = it.next();
			if(c.darLongitud()>mayor.darLongitud())
			{
				mayor=c;
			}
		}
		tiempo = mayor.darCosto()/mayor.darLongitud();
		Iterator<IVertice<Integer, Estacion, Conexion>> itv = mayor.darVertices();
		while(itv.hasNext())
		{
			lista.addAlFinal(itv.next().darValor());
		}

		return lista;
	}

	/**
	 * Retorna el tiempo total de viaje de la última consulta realizada.
	 * @return El tiempo total de viaje de la última consulta realizada.
	 */
	public double getTiempo() 
	{
		return tiempo;
	}

	/**
	 * Retorna la longitud total del último recorrido consultado.
	 * @return La longitud total del último recorrido consultado.
	 */
	public int getLongitud() 
	{
		return longitud;
	}

	/**
	 * Retorna las Conexiones que conforman el viaje de mayor lognitud desde una estación de origen.
	 * @param idOrigen El número de identificación de la estación de origen.
	 * @return Una lista que contiene las conexiones que conforman el viaje de mayor lognitud desde una estación de origen.
	 * @throws BiciException Si el número de identificación de la estación de origen, no existe.
	 */
	public Lista<Conexion> mayorViajeConex(int idOrigen) throws BiciException
	{
		IVertice<Integer, Estacion, Conexion> origen = grafo.darVertice(idOrigen);
		if(origen==null)
			throw new BiciException("La estacion origen no existe");
		Lista<Conexion> lista = new LlamaArrayList<>(20);

		CaminoMinimo<Integer, Estacion, Conexion> cams = (CaminoMinimo<Integer, Estacion, Conexion>)grafo.darCaminosMinimos(idOrigen);
		Iterator<Camino<Integer, Estacion, Conexion>> it = cams.darCaminos();
		Camino<Integer,Estacion,Conexion> mayor=null;
		if(it.hasNext())
			mayor = it.next();
		while(it.hasNext())
		{
			Camino<Integer, Estacion, Conexion> c = it.next();
			if(c.darLongitud()>mayor.darLongitud())
			{
				mayor=c;
			}
		}
		Iterator<IArco<Integer, Estacion, Conexion>> itv = mayor.darArcos();
		while(itv.hasNext())
		{
			lista.addAlFinal(itv.next().darInfoArco());
		}

		return lista;
	}

	/**
	 * Establece una ruta óptima entre dos estaciones, sujeto a una lista de estaciones que
	 * deben ser visitadas.
	 * @param estaciones Enumeración con las estaciones a visitar, separadas por espacio.
	 * @param idOrigen Número de identificación de la estación de origen.
	 * @return Una lista con las estaciones que deben ser recorridas y que satisfacen la condición.
	 * @throws BiciException Si el número de identificación de la estación de origen, no existe.
	 */
	public Lista<Estacion> recomendarViajeEst(String estaciones, int idOrigen) throws BiciException
	{
		tiempo=0;
		longitud=0;
		String[] estacionesR = estaciones.split(" ");
		int anterior = idOrigen;
		Lista<Estacion>lista = new LlamaArrayList<>(20);

		for(int i =0; i<estacionesR.length;i++)
		{
			int actual = Integer.parseInt(estacionesR[i]);
			try {
				lista.addAll(caminoMasCortoABEst(anterior, actual));
			} catch (BiciException e) {
				throw new BiciException("La estación con id: "+actual+"o: "+anterior+"no existe");
			}
			anterior=actual;
		}
		tiempo=tiempo/longitud;

		return lista;
	}

	/**
	 * Establece una ruta óptima entre dos estaciones, sujeto a una lista de estaciones que
	 * deben ser visitadas.
	 * @param estaciones Enumeración con las estaciones a visitar, separadas por espacio.
	 * @param idOrigen Número de identificación de la estación de origen.
	 * @return Una lista con las Conexiones que deben ser recorridas y que satisfacen la condición.
	 * @throws BiciException Si el número de identificación de la estación de origen, no existe.
	 */
	public Lista<Conexion> recomendarViajeConex(String estaciones, int idOrigen) throws BiciException
	{
		String[] estacionesR = estaciones.split(" ");
		int anterior = idOrigen;
		Lista<Conexion>lista = new LlamaArrayList<>(20);

		for(int i =0; i<estacionesR.length;i++)
		{
			int actual = Integer.parseInt(estacionesR[i]);
			try {
				lista.addAll(caminoMasCortoABConex(anterior, actual));
			} catch (BiciException e) {
				throw new BiciException("La estación con id: "+actual+"o: "+anterior+"no existe");
			}
		}

		return lista;
	}

    /**
     * Retorna una lista con las conexiones salientes de una estación de origen.
     * @return La lista con conexiones salientes desde una estación.
     */
	public Lista<Conexion> getCaminosDes() 
	{
		return caminosDes;
	}

	/**
     * Retorna una lista con las estaciones que conforman todas las conexiones salientes de una estación de origen.
     * @return La lista con las estaciones que conforman todas las conexiones salientes de una estación.
     */
	public Lista<Estacion> getEstacionesDes() 
	{
		return estacionesDes;
	}
	
	/**
	 * Retorna la estación actualmente seleccionada.
	 * @return La Estación actualmente seleccionada, null en caso de que no exista.
	 */
	public Estacion getSeleccionada()
	{
		return seleccionada;
	}

	/**
	 * Solicita una instancia del gestor de Caminos Mínimos.
	 */
	public void reloadInstance() 
	{
		CaminoMinimo.initializeInstance(grafo);
		
	}

}
