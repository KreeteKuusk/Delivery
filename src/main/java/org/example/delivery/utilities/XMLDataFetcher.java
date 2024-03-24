package org.example.delivery.utilities;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class XMLDataFetcher {
// This only fetches data about Tallinn-Harku, Tartu-Tõravere and Pärnu right now
    public void fetchDataFromURL(String urlString) throws Exception {
        // Create an Url object from the urlString
        URL url = new URL(urlString);
        // Open a connection to the URL and get the InputStream
        InputStream is = url.openStream();

        // Create a DocumentBuilder for parsing XML and creating a document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        // Parse the input stream
        Document doc = dBuilder.parse(is);
        // Normalize text representation to make it more processable
        doc.getDocumentElement().normalize();

        // Get the root element (Observation)
        Element root = doc.getDocumentElement();

        // Get a NodeList of all "station" elements
        NodeList stationList = root.getElementsByTagName("station");
        // A list of all the stations we want data from
        ArrayList<String> wantedStations = new ArrayList<>(Arrays.asList("Tallinn-Harku", "Pärnu", "Tartu-Tõravere"));

        for (int i = 0; i < stationList.getLength(); i++) {
            // Checking if we have fetched all the wanted data
            if (wantedStations.isEmpty()) break;

            Node stationNode = stationList.item(i);
            // Check if current node is element type node
            if (stationNode.getNodeType() == Node.ELEMENT_NODE) {
                Element stationElement = (Element) stationNode;

                // Checking if we reached a station we want data from
                String name = stationElement.getElementsByTagName("name").item(0).getTextContent();
                if (wantedStations.contains(name)) {
                    wantedStations.remove(name); // remove it from the list
                    // Get the rest of the text content of the required sub-elements
                    String timestamp = root.getAttribute("timestamp");
                    String wmo = stationElement.getElementsByTagName("wmocode").item(0).getTextContent();
                    String airTemperature = stationElement.getElementsByTagName("airtemperature").item(0).getTextContent();
                    String windSpeed = stationElement.getElementsByTagName("windspeed").item(0).getTextContent();
                    String phenomenon = stationElement.getElementsByTagName("phenomenon").item(0).getTextContent();

                    System.out.println("Timestamp: " + timestamp);
                    System.out.println("Station Name: " + name);
                    System.out.println("WMO: " + wmo);
                    System.out.println("Air Temperature: " + airTemperature);
                    System.out.println("Wind Speed: " + windSpeed);
                    System.out.println("Phenomenon: " + phenomenon);
                    System.out.println();
                }
            }
        }

        // Close the InputStream
        is.close();
    }

    public void saveDataToDatabase(){
    }
}