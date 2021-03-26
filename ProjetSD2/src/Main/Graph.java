package Main;

import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
	
	//Breadth-first search
	public void calculerItineraireMinimisantNombreDeFrontieres(String startCca3, String destinationCca3, String file) {
		
		Deque<Country> fifo = new LinkedList<Country>();	//Used for breadth-first search
		Deque<Country> itinerary = new LinkedList<Country>();	//Final itinerary countries
		Map<Country, Country> originCountry = new HashMap<Country, Country>();
		Set<Country> alreadyMet = new HashSet<Country>();	//Countries already met during run
		Country start = cca3Country.get(startCca3);
		Country destination = cca3Country.get(destinationCca3);
		boolean destinationFound = false;
		
		alreadyMet.add(start);
		
		Set<String> outgoingArches = this.outgoingArches(start);
		Country nextCountry = start;
		
		//If destination has no border countries
		if (this.outgoingArches(destination) == null) {
			throw new UnreachableException("Le pays d'arrivée n'est pas accessible");
		}
		
		while(!destinationFound) {
			if(outgoingArches == null) {
				throw new UnreachableException("Le pays d'arrivée n'est pas accessible");
			}
			for (String cca3 : outgoingArches) {
				Country countryTemp = cca3Country.get(cca3);
				if(!alreadyMet.contains(countryTemp)){
					fifo.add(countryTemp);
					originCountry.put(countryTemp, nextCountry);
					alreadyMet.add(countryTemp);
				}
				if(countryTemp.equals(destination))
					destinationFound = true;
			}
			nextCountry = fifo.poll();
			outgoingArches = this.outgoingArches(nextCountry);			
		}
		
		
		Country previousCountry = destination;
		itinerary.add(destination);
		while(!previousCountry.equals(start)) {
			Country previousCountryTemp = originCountry.get(previousCountry);
			itinerary.addFirst(previousCountryTemp);
			previousCountry = previousCountryTemp;
		}
				
		XmlBuilder.build(itinerary, file);		
		
	}
	
	//Dijkstra
	public void calculerItineraireMinimisantPopulationTotale(String startCca3, String destinationCca3, String file) {
		Map<String, Long> temporaryLabel = new HashMap<String, Long>();
		Map<String, Long> definitiveLabel = new HashMap<String, Long>();
		Map<Country, Country> originCountry = new HashMap<Country, Country>();
		Deque<Country> itinerary = new LinkedList<Country>();	//Final itinerary countries

		Country start = cca3Country.get(startCca3);
		Country destination = cca3Country.get(destinationCca3);
		
		//If destination has no border countries
		if (this.outgoingArches(destination) == null) {
			throw new UnreachableException("Le pays d'arrivée n'est pas accessible");
		}
		
		Country nextCountry = start;
		temporaryLabel.put(startCca3, null);
		definitiveLabel.put(startCca3, (long) start.getPopulation());
		
		
		while (!definitiveLabel.containsKey(destination.getCca3())) {
			
			if(nextCountry == null) throw new UnreachableException("Le pays d'arrivée n'est pas accessible");
			String nextCountryCca3 = nextCountry.getCca3();
			long nextCountryPop = definitiveLabel.get(nextCountryCca3);	//Population met to reach nextCountry
			
			//Recalculating temporaryLabel entries and replacing them if necessary
			for (String cca3 : this.outgoingArches(nextCountry)) {
				if(!definitiveLabel.containsKey(cca3)) {
					if(!originCountry.containsKey(cca3Country.get(cca3)) ||
							temporaryLabel.get(cca3) > (nextCountryPop + cca3Country.get(cca3).getPopulation())) {
						originCountry.put(cca3Country.get(cca3), nextCountry);
						temporaryLabel.put(cca3, nextCountryPop + cca3Country.get(cca3).getPopulation());
					}
				}
			}
			
			//Selecting next country to use as nextCountry
			String cca3Temp = findMinPopulation(temporaryLabel);
			definitiveLabel.put(cca3Temp, temporaryLabel.get(cca3Temp));	
			temporaryLabel.put(cca3Temp, null);			
			nextCountry = cca3Country.get(cca3Temp);
		}
		
		//Create itinerary in order to build xml
		Country previousCountry = destination;
		itinerary.add(destination);
		while(!previousCountry.equals(start)) {
			Country previousCountryTemp = originCountry.get(previousCountry);
			itinerary.addFirst(previousCountryTemp);
			previousCountry = previousCountryTemp;
		}
		
		XmlBuilder.build(itinerary, file);		
		
	}
	
	private String findMinPopulation(Map<String, Long> map) {
		
		String minPopulationCca3 = null;
		long smallestPopulation = Long.MAX_VALUE;
		
		for (String cca3 : map.keySet()) {
			if (map.get(cca3) == null) continue;
			long population = map.get(cca3);
			
			if(population < smallestPopulation) {
				smallestPopulation = population;
				minPopulationCca3 = cca3;
			}
				
		}
		
		return minPopulationCca3;
	}

}
