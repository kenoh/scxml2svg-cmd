package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
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

    public boolean validateAgainstXsd(File xmlFile, File xsdFile) {
        // create schema factory
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // load schema from file
        Schema schema = null;
        try {
            schema = sFactory.newSchema(xsdFile);
        } catch (SAXException se) {

        }

        // validate xml file using validator from created schema
        Validator validator = schema.newValidator();
        try {
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (SAXException se) {
            return false;
        } catch (IOException ioe) {
            return false;
        }
    }
}
