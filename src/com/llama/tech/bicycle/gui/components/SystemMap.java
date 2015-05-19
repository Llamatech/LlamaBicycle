package com.llama.tech.bicycle.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.llama.tech.bicycle.gui.Interfaz;
import com.llama.tech.bicycle.gui.VistaCaminos;
import com.llama.tech.bicycle.gui.VistaConexiones;
import com.llama.tech.misc.LlamaTuple;
import com.llama.tech.utils.dict.LlamaDict;
import com.llama.tech.utils.list.LlamaArrayList;

import javax.swing.JButton;

public class SystemMap extends JPanel
{
	private final static double LONG = 42.35753;
	private final static double LAT = -71.06163;
	private final static int ZOOM = 12;
	
	private Interfaz main; 
	
	
	private MapPanel mapPanel;
    
    public SystemMap(Interfaz p)
    {
    	main = p;
    	mapPanel = new MapPanel(this);
    	mapPanel.setBounds(0, 0, 370, 347);
    	mapPanel.setZoom(ZOOM);
    	Point position = mapPanel.computePosition(new Point2D.Double(LAT, LONG));
    	
    	mapPanel.getOverlayPanel().setVisible(false);
        ((JComponent)mapPanel.getControlPanel()).setVisible(false);
    	
    	mapPanel.setCenterPosition(position); 
    	//mapPanel.getOverlayPanel().setVisible(!mapPanel.getOverlayPanel().isVisible());
    	//mapPanel.setMarkers(marks);
    	setLayout(null);
    	add(mapPanel);
    	repaint();
    }
    
    public void displayRoutes(LlamaArrayList<LlamaTuple<MapMarker, MapMarker>> routes)
    {
    	mapPanel.setRoutes(routes);
    	mapPanel.repaint();
    }
    
    public void clearRoutes()
    {
    	mapPanel.clearRoutes();
    }
    
    public void setMarkers(LlamaArrayList<MapMarker> markers)
    {
    	mapPanel.setMarkers(markers);
    	//mapPanel.clearRoutes();
    	mapPanel.repaint();
    }

	public void notifyClick(double lat, double lon) 
	{
		System.out.println("Pos: "+lat+","+lon);
	    LlamaTuple<Double, Double> pos = new LlamaTuple<Double, Double>(lat, lon);
	    main.propagateClick(pos);
	    
	    try
	    {
	    	VistaConexiones vc = ((VistaConexiones) getParent());
	    	vc.notifyStation();
	    }
	    catch(ClassCastException e)
	    {
	    	
	    }
	}

    
    
    
//    @Override
//    public void paintComponent(Graphics g)
//    {
//    	super.paintComponent(g);
//    	Graphics2D g2 = (Graphics2D) g;
//    	g2.setColor(new Color(58, 58, 58));
//    	MapMarker m = new MapMarker(getWidth()/2.0, getHeight()/2.0, 4, 4); 
//    	g2.draw(m);
//    	g2.fill(m);
//    }
    
}
