package Main;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Graph {

	private Map<Country, Set<String>> outgoingRoads;
	private Map<String, Country> cca3Country;	//References countries using their cca3

	public Graph()  {
		outgoingRoads = new HashMap<Country, Set<String>>();
		cca3Country = new HashMap<String, Country>();
	}

	protected void addNode(Country c) {
		if(c == null) throw new IllegalArgumentException("Error: country can't be null");
		outgoingRoads.put(c, new HashSet<String>());
		cca3Country.put(c.getCca3(), c);
	}

	protected void addArch(Country c, String cca3) {
		if(c == null) throw new IllegalArgumentException("Error: country can't be null");
		if(cca3 == null) throw new IllegalArgumentException("Error: cca3 can't be null");
		outgoingRoads.get(c).add(cca3);
	}
	
	public Set<String> outgoingArches(Country c){
		return outgoingRoads.get(c);
	}

	public boolean areAdjacent(Country c1, Country c2) {		
		return outgoingRoads.get(c1).contains(c2.getCca3());
	}
	
	public void calculerItineraireMinimisantNombreDeFrontieres(String cca3, String otherCca3, String file) {
		//todo
	}
	
	public void calculerItineraireMinimisantPopulationTotale(String cca3, String otherCca3, String file) {
		//todo
	}	

}
