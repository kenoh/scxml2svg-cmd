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

    public boolean validateAgainstXsdDir(File xmlFile, File xsdDir) throws FileNotFoundException {
        InputStream xml = new FileInputStream(xmlFile);
        
        File[] files = xsdDir.listFiles();
        Source[] sources = new Source[files.length];
        
        // load all xsd files in the dir
        for(int i=0;i<files.length;i++) {
            File f = files[i];
            sources[i] = new StreamSource(f);
        }

        return validateAgainstXsd(xml, sources);
    }
    
    public boolean validateAgainstXsd(File xmlFile, File xsdFile) throws FileNotFoundException {
        InputStream xml = new FileInputStream(xmlFile);
        InputStream xsd = new FileInputStream(xsdFile);
        
        return validateAgainstXsd(xml, xsd);
    }

    public boolean validateAgainstXsd(InputStream xmlStream, InputStream xsdStream) {
        Source[] sources = new Source[1];
        sources[0] = new StreamSource(xsdStream);
        
        return validateAgainstXsd(xmlStream, sources);
    }
    
    public boolean validateAgainstXsd(InputStream xmlStream, Source[] sources) {
        // create schema factory
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        // load schema from file
        Schema schema = null;
        try {
            schema = sFactory.newSchema(sources);
        } catch (SAXException se) {
            System.err.println("Schema not loaded.");
            System.err.println(se.getMessage());
            System.exit(1);
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
        }
    }
}
