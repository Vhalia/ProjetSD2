package Main;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class SAXHandler extends DefaultHandler {
	
	private Country currentCountry = null;	//the country SAX is actually reading
	boolean insideBorder = false;	//verify if inside border tag
	
	Graph graph = new Graph();
	
	public Graph getGraph() {
		return this.graph;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equalsIgnoreCase("country")) {
			Country country = new Country(attributes.getValue("cca3"), attributes.getValue("name"), Integer.parseInt(attributes.getValue("population")));
			currentCountry = country;
			graph.addNode(currentCountry);
		} else if(qName.equalsIgnoreCase("border")) {
			insideBorder = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(qName.equalsIgnoreCase("country")) {
			currentCountry = null;
		} else if(qName.equalsIgnoreCase("border")) {
			insideBorder = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if(insideBorder) {
			String cca3 = new String(ch, start, length);
			graph.addArch(currentCountry, cca3);
		}
	}

}
