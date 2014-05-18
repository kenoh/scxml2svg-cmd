package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Class executing transformation of scxml document containing finite automaton
 * to its graphic representation in SVG format, using XSLT transform to DOT
 * format and GraphViz library to create SVG.
 *
 * @author Marek Zuzi
 */
public class Scxml2Svg {

    public static void main(String[] args) {
        Scxml2Svg program = new Scxml2Svg();

        program.process(args);
    }

    public void process(String[] args) {
        validateArgs(args);
        
        String srcFileName = args[0];
        String dstFileName = args[1];

        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);
        File dotFile = new File("graph.dot");
        
        File xsdDir = new File("xsd");
        File templateFile = new File("scxml-to-dot.xsl");

        XmlTransformator trans = new XmlTransformator();
        XmlValidator valid = new XmlValidator();
        GraphVizConverter converter = new GraphVizConverter();

        // validate source file
        boolean isValid = false;
        try {
            isValid = valid.validateAgainstXsdDir(srcFile, xsdDir);
        } catch (FileNotFoundException e) {
            System.err.println("File not found.");
            System.exit(1);
        }

        if (!isValid) {
            System.err.println("Source file is not valid SCXML file. Aborting.");
            System.exit(1);
        } else {
            System.out.println("Source file " + srcFileName + " is valid.");
        }

        // transform to DOT
        try {
            trans.transform(srcFile, templateFile, dotFile);
        } catch (TransformerConfigurationException e) {
            System.err.println("Problem with template file.");
            System.exit(1);
        } catch (TransformerException ex) {
            System.err.println("Transformation failed.");
            System.exit(1);
        }
        
        System.out.println("Transformation to DOT complete.");
        
        // convert DOT to SVG
        converter.convertDotToSvg(dotFile, dstFile);
    }

    private void validateArgs(String[] args) {
        // exit the program if arguments are not valid
        if(args == null) {
            System.err.println("No arguments given.");
            System.exit(1);
        }
        if(args.length < 2) {
            System.err.println("Not enough arguments given.");
            System.exit(1);
        }
    }
}
