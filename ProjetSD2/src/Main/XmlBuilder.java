package Main;

import java.io.File;
import java.util.Deque;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XmlBuilder {
	
	private static Document doc;	
	
	public static void build(Deque<Country> itinerary, String file) {
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.newDocument();
			doc.setXmlStandalone(true);
			
			//rootElement itineraire
			Element rootElement = doc.createElement("itineraire");
			doc.appendChild(rootElement);
			
			long totalPopulation = 0;
			
			//element pays
			for (Country country : itinerary) {
				Element countryElt = createElement(rootElement, "pays");
				//attribut cca3
				createAttribute(countryElt, "cca3", country.getCca3());
				//attribut nom
				createAttribute(countryElt, "nom", country.getName());
				//attribut population
				createAttribute(countryElt, "population", String.valueOf(country.getPopulation()));
				
				totalPopulation += country.getPopulation();				
			}
						
			//attribut arrivee
			createAttribute(rootElement, "arrivee", itinerary.getLast().getName());
			//attribut depart
			createAttribute(rootElement, "depart", itinerary.getFirst().getName());
			//attribut nbPays
			createAttribute(rootElement, "nbPays", String.valueOf(itinerary.size()));
			//attibut sommePopulation
			createAttribute(rootElement, "sommePopulation", String.valueOf(totalPopulation));
			
			//enregistrer dans un fichier
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(file));
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.transform(source, result);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static Element createElement(Element parent, String name) {
		Element elt = doc.createElement(name);
		parent.appendChild(elt);
		return elt;
	}

	private static void createAttribute(Element element, String name, String value) {
		Attr attr = doc.createAttribute(name);
		attr.setValue(value);
		element.setAttributeNode(attr);
	}
}
