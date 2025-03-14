package org.example.delivery.utilities;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.example.delivery.model.Weather;
import org.example.delivery.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * This class provides methods for fetching data from url and saving it to database
 */

@Service
public class XMLDataFetcher { // This only fetches data about Tallinn-Harku, Tartu-Tõravere and Pärnu right now

    @Value("${weather.fetch.url}") // Reads the url from the application.properties
    private String urlString;

    private final WeatherRepository weatherRepository;

    public XMLDataFetcher(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /**
     * Method for fetching xml type data from url and saving it to database
     *
     * @throws Exception If an error occurs during fetching or parsing
     */
    @Scheduled(cron = "${weather.fetch.cron}") // Reads the cron expression from the application.properties
    public void fetchDataFromURL() throws Exception {
        // Create an Url object from the urlString
        URL url = new URI(urlString).toURL();
        // Open a connection to the URL and get the InputStream
        InputStream is = url.openStream();

        // Create a DocumentBuilder for parsing XML and creating a document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbf.newDocumentBuilder();
        // Parse the input stream
        Document doc = dBuilder.parse(is);
        // Normalize text representation to make it more processable
        doc.getDocumentElement().normalize();

        // Get the root element ("Observation" in this case)
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

                    saveDataToDatabase(name, timestamp, wmo, airTemperature, windSpeed, phenomenon);
                }
            }
        }

        // Close the InputStream
        is.close();
    }

    /**
     * Method for inserting info to database
     *
     * @param name           name of the station
     * @param timestamp      timestamp of data
     * @param wmo            wmo code of the station
     * @param airTemperature air temperature
     * @param windSpeed      wind speed
     * @param phenomenon     weather phenomenon
     */
    public void saveDataToDatabase(String name, String timestamp, String wmo, String airTemperature, String windSpeed, String phenomenon) {
        Weather weather = new Weather();
        weather.setStationName(name);
        weather.setTimestamp(timestamp);
        weather.setWmo(wmo);
        weather.setTemperature(Float.parseFloat(airTemperature));
        weather.setWindSpeed(Float.parseFloat(windSpeed));
        weather.setPhenomenon(phenomenon);
        weatherRepository.save(weather);
    }
}