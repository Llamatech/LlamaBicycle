/*
 * LlamaMapsAPI.java
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

package com.llama.tech.bicycle.gis;

import java.awt.Point;

import com.llama.tech.utils.dict.LlamaDict;

public class LlamaMapsAPI 
{
	private static double MERCATOR_RANGE = 256.0;
	
	public static Double bound(Double value, Double opt_min, Double opt_max)
	{
		if (opt_min != null)
		{
		    value = Math.max(value, opt_min);
		}
		if(opt_max != null)
		{
			value = Math.min(value, opt_max);
		}
		
		return value;
	}
	
	public static double distanciaEntreGrados(double lat1, double lon1, double lat2, double lon2)
	{
		double dlon = (lon2 - lon1)*Math.PI/180;
		double dlat = (lat2 - lat1)*Math.PI/180 ;
		double lat1R = lat1*Math.PI/180;
		double lat2R=lat2*Math.PI/180;
		double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1R) * Math.cos(lat2R) * Math.pow(Math.sin(dlon/2),2);
		double ca = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a) ) ;
		double cuantaDist = 6371.000 * ca;
		return cuantaDist;

	}
	
	public static double degreesToRadians(double deg)
	{
		return deg * (Math.PI / 180);
	}
	
	public static double radiansToDegrees(double rad)
	{
		return rad * (180/Math.PI);
	}
	
	public static Point.Double fromLatLngToPoint(double latitude, double longitude, int zoom)
	{
		G_LatLng latLong = new G_LatLng(latitude, longitude);
		MercatorProjection proj = new MercatorProjection();
		G_Point p = proj.fromLatLngToPoint(latLong, zoom);
		return new Point.Double(p.x, p.y);
	}
	
	public static double map(double x, double in_min, double in_max, double out_min, double out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	
	
	public static double lon_to_pos(double longitude, double mapWidth)
	{
		return (longitude+180)*(mapWidth/360);
	}
	
	public static double lon_to_tileX(double longitude, int zoom)
	{
		return (longitude + 180.0) / 360.0 * (1 << zoom);
	}
	
	public static double lat_to_tileY(double latitude, int zoom)
	{
		return (1 - Math.log(Math.tan(Math.toRadians(latitude)) + 1 / Math.cos(Math.toRadians(latitude))) / Math.PI) / 2 * (1 << zoom);
	}
	
	public static double lat_to_pos(double latitude, double mapWidth, double mapHeight)
	{
		double latRad = degreesToRadians(latitude);
		double mercN = Math.log(Math.tan((Math.PI/4)+(latRad/2)));
		return (mapHeight/2)-(mapWidth*mercN/(2*Math.PI));
		
	}
	
	public static LlamaDict<String, Double> getCorners(double latitude, double longitude, double zoom, double mapWidth, double mapHeight)
	{
		G_LatLng center = new G_LatLng(latitude, longitude);
		double scale = Math.pow(2, zoom);
		MercatorProjection proj = new MercatorProjection();
		G_Point centerPx = proj.fromLatLngToPoint(center, null);
		G_Point SWPoint = new G_Point(centerPx.x-(mapWidth/2)/scale, centerPx.y+(mapHeight/2)/scale);
		G_LatLng SWLatLon = proj.fromPointToLatLng(SWPoint);
		G_Point NEPoint = new G_Point(centerPx.x+(mapWidth/2)/scale, centerPx.y-(mapHeight/2)/scale);
		G_LatLng NELatLon = proj.fromPointToLatLng(NEPoint);
		
		LlamaDict<String, Double> corners = new LlamaDict<String, Double>(5);
		corners.addEntry("N", NELatLon.lat);
		corners.addEntry("E", NELatLon.lng);
		corners.addEntry("S", SWLatLon.lat);
		corners.addEntry("W", SWLatLon.lng);
		
		return corners;
	}
	
	
	
	public static class G_Point
	{
		public double x;
		public double y;
		
		public G_Point()
		{
			this.x = 0;
			this.y = 0;
		}
		
		public G_Point(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
	}
	
	public static class G_LatLng
	{
		public double lat;
		public double lng;
		
		public G_LatLng(double lat, double lng)
		{
			this.lat = lat;
			this.lng = lng;
		}
	}
	
	public static class MercatorProjection
	{
	    public G_Point pixelOrigin;
	    public double pixelsPerLonDegree;
	    public double pixelsPerLonRadian;
		
		public MercatorProjection()
		{
			pixelOrigin = new G_Point(MERCATOR_RANGE / 2.0, MERCATOR_RANGE / 2.0);
			pixelsPerLonDegree = MERCATOR_RANGE / 360;
			pixelsPerLonRadian = MERCATOR_RANGE / (2 * Math.PI);
		}
		
		public G_Point fromLatLngToPoint(G_LatLng latLng, G_Point opt_point)
		{
			G_Point point = opt_point != null ? opt_point : new G_Point();
			G_Point origin = this.pixelOrigin;
			point.x = origin.x + latLng.lng * pixelsPerLonDegree;
			double siny = bound(Math.sin(degreesToRadians(latLng.lat)), -0.9999, 0.9999);
			point.y = origin.y + 0.5 * Math.log((1 + siny) / (1 - siny)) * -pixelsPerLonRadian;
			return point;
		}
		
		public G_Point fromLatLngToPoint(G_LatLng latLng, int zoom)
	    {
	        G_Point point = new G_Point();

	        point.x = latLng.lng * pixelsPerLonDegree;       

	        // Truncating to 0.9999 effectively limits latitude to 89.189. This is
	        // about a third of a tile past the edge of the world tile.
	        double siny = bound(Math.sin(degreesToRadians(latLng.lat)), -0.9999,0.9999);
	        point.y = 0.5 * Math.log((1 + siny) / (1 - siny)) *- pixelsPerLonRadian;

	        int numTiles = 1 << zoom;
	        point.x = point.x * numTiles;
	        point.y = point.y * numTiles;
	        return point;
	     }
		
		public G_LatLng fromPointToLatLng(G_Point point)
		{
			G_Point origin = this.pixelOrigin;
			double lng = (point.x - origin.x) / pixelsPerLonDegree;
			double latRadians = (point.y - origin.y) / -pixelsPerLonRadian;
			double lat = radiansToDegrees(2 * Math.atan(Math.exp(latRadians)) - Math.PI / 2);
			return new G_LatLng(lat, lng);
		}
	}
	
	
	public static void main(String... args)
	{
		 double latitude = 12.1208512;
		 double longitude = 15.0511076;
		 double zoom = 15.0;
		 
		 double mapWidth = 500.0;
		 double mapHeight = 380.0;
		 
		 LlamaDict<String, Double> corners = getCorners(latitude, longitude, zoom, mapWidth, mapHeight);
		 
		 System.out.println(corners);
		 
	}
}


