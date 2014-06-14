package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;

/**
 * Class that can validate an Xml document against its DTD or Xml Schema.
 *
 * @author Marek Zuzi
 */
public class XmlValidator {
    /**
     * Validates xml file stored in given InputStream against given xml schema.
     *
     * @param xmlStream InputStream containing Xml file to be validated
     * @param xsdStream InputStream pointing to xml schema
     * @return true if the file is valid, false otherwise
     * @throws IllegalArgumentException if there is a problem with loading
     * schema
     */
    public boolean validateAgainstXsd(InputStream xmlStream, InputStream xsdStream) {
        Source[] sources = new Source[1];
        sources[0] = new StreamSource(xsdStream);

        return validateAgainstXsd(xmlStream, sources);
    }

    /**
     * Validates xml file stored in given InputStream against given xml schemas.
     *
     * @param xmlStream InputStream containing Xml file to be validated
     * @param xsdStreams set of InputStreams pointing to xml schema files
     * @return true if the file is valid, false otherwise
     * @throws IllegalArgumentException if there is a problem with loading
     * schemas
     */
    public boolean validateAgainstXsd(InputStream xmlStream, InputStream[] xsdStreams) {
        Source[] sources = new Source[xsdStreams.length];
        for (int i = 0; i < xsdStreams.length; i++) {
            sources[i] = new StreamSource(xsdStreams[i]);
        }

        return validateAgainstXsd(xmlStream, sources);
    }

    /**
     * Validates xml file stored in given InputStream against given xml schemas.
     *
     * @param xmlStream InputStream containing Xml file to be validated
     * @param sources set of sources pointing to xml schemas
     * @return true if the file is valid, false otherwise
     * @throws IllegalArgumentException if there is a problem with loading
     * schemas
     */
    public boolean validateAgainstXsd(InputStream xmlStream, Source[] sources) {
        // create schema factory
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // load schema from file
        Schema schema = null;
        try {
            schema = sFactory.newSchema(sources);
        } catch (SAXException se) {
            throw new IllegalArgumentException("Schema error", se);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Null in sources", e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("One of sources not recognized", e);
        } catch (UnsupportedOperationException e) {
            throw new IllegalArgumentException("Invalid schema language", e);
        }

        // validate xml file using validator from created schema
        Validator validator = schema.newValidator();
        try {
            validator.validate(new StreamSource(xmlStream));
            return true;
        } catch (SAXException se) {
            System.err.println(se.getMessage());
            return false;
        } catch (IOException ioe) {
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
