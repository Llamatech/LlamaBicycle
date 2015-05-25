/*
 * BiciException.java
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
 * Clase que describe una excepción genérica, la cual sería enviada si algún error
 * ocurriese.
 * @author Llamatech
 * 
 */
public class BiciException extends Exception implements Serializable{
	
	/**
	 * Constructor de la excepción.
	 * @param message Mensaje de alerta al usuario, o a la ejecución del programa.
	 */
	public BiciException(String message)
	{
		super(message);
	}

}
