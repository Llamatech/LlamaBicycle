/*
 * MapMarker.java
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

package com.llama.tech.bicycle.gui.components;

import java.awt.Color;

public class MapMarker
{
	 public final double longitude;
	 public final double latitude;
	 private boolean visibility = true;
	 public Color color;
	 public static final String IMG_ROUTE = "./res/img/marker.png";
	
     public MapMarker(double lat, double lon)
     {
    	 longitude = lon;
    	 latitude = lat;
     }
     
     public void notDisplay()
     {
    	 visibility = false;
     }
     
     public void display()
     {
    	 visibility = true;
     }
     
     public boolean isVisible()
     {
    	 return visibility;
     }
     
     @Override
     public String toString()
     {
    	 return "Lat: "+latitude+"; Long: "+longitude;
     }
     
     
}
