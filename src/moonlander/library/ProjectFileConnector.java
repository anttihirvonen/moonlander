package moonlander.library;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 


/*
 * Implements "connection" with exported sync data files.
 */
class ProjectFileConnector extends Connector {
    /*
     * Read syncdata from given path
     */
    public ProjectFileConnector(Logger logger, TrackContainer tracks, Controller controller, String path) throws Exception {
        super(logger, tracks, controller);

        try {
            // This can throw if reading fails
            readAndLoadTracks(path);
        } catch (Exception e) {
            close();
            throw e;
        }

        // All good; start controller
        controller.play();
    }

    /**
     * Loads syncdata from Rocket's project file at given path
     */
    private void readAndLoadTracks(String path) throws Exception {
        logger.info("Loading tracks from '" + path + "'.");

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File(path));

        NodeList trackList = doc.getElementsByTagName("track");
        int totalTracks = trackList.getLength();
        logger.finer("Total number of tracks: " + totalTracks);

        // Read all tracks
        for(int s = 0; s < totalTracks; s++){
            Element trackElement = (Element)trackList.item(s);
            NodeList keyList = trackElement.getElementsByTagName("key");

            if (keyList.getLength() > 0) {
                // Create new track
                Track track = tracks.getOrCreate(trackElement.getAttribute("name"));
                int totalKeys = keyList.getLength();

                logger.finest("Loading track (name=" + trackElement.getAttribute("name") +
                        " keys=" + totalKeys + ")");

                // Add all found keys
                for (int i = 0; i < keyList.getLength(); i++) {
                    Element key = (Element)keyList.item(i);

                    int row = Integer.parseInt(key.getAttribute("row"));
                    float value = Float.parseFloat(key.getAttribute("value"));
                    int keyType = Integer.parseInt(key.getAttribute("interpolation"));

                    track.addOrUpdateKey(new TrackKey(row, value, keyType));
                }

            }
        }
    }

    public void update() {
        // Data loaded; do nothing
        return;
    }


}
