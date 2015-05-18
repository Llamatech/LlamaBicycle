package com.llama.tech.bicycle.gui.components;

import java.awt.geom.Ellipse2D;

public class MapMarker
{
	 public final double longitude;
	 public final double latitude;
	 private boolean visibility = true;
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
