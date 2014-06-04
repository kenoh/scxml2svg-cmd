package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
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

    private ResourceBundle strings = ResourceBundle.getBundle("cz/muni/fi/pb138/scxml2svg/Strings");

    /**
     * Wraps all the functionality and handles errors. First, validates command
     * line arguments. If valid, validates input file and processes it to DOT
     * graph, and then to SVG using GraphViz. If any error occurs, exits the
     * program and prints error message.
     *
     * @param args command line parameters recieved at startup.
     */
    public void process(String[] args) {
        validateArgs(args);

        String srcFileName = args[0];
        String dstFileName = args[1];

        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);
        File dotFile = new File("graph.dot");
        InputStream srcStream = null;
        try {
            srcStream = new FileInputStream(srcFile);
        } catch (FileNotFoundException ex) {
            System.err.println(strings.getString("cantOpenSrc"));
            System.exit(1);
        }
        OutputStream dotStream = null;
        try {
            dotStream = new FileOutputStream(dotFile);
        } catch (FileNotFoundException ex) {
            System.err.println(strings.getString("cantOpenDot"));
            System.exit(1);
        }

        File xsdDir = new File("xsd");
        //File templateFile = new File("template.xsl");
        InputStream templateStream = this.getClass().getResourceAsStream("resources/scxml-to-dot.xsl");

        XmlTransformator trans = new XmlTransformator();
        XmlValidator valid = new XmlValidator();
        GraphVizConverter converter = new GraphVizConverter();

        // validate source file
        //boolean isValid = false;
        //try {
        //    isValid = valid.validateAgainstXsdDir(srcFile, xsdDir);
        //} catch (FileNotFoundException e) {
        //    System.err.println("File not found.");
        //    System.exit(1);
        //}
        //if (!isValid) {
        //    System.err.println(strings.getString("inputNotValid"));
        //    System.exit(1);
        //} else {
        //    System.out.println("Source file " + srcFileName + " is valid.");
        //}
        // transform to DOT
        try {
            trans.transform(srcStream, templateStream, dotStream);
        } catch (TransformerConfigurationException e) {
            System.err.println("Problem with template file.");
            System.exit(1);
        } catch (TransformerException e) {
            System.err.println("Transformation failed.");
            System.exit(1);
        }

        System.out.println("Transformation to DOT complete.");

        // convert DOT to SVG
        int code = converter.convertDotToSvg(dotFile, dstFile);
        if (code == 1) {
            System.err.println(strings.getString("cantCreateImage"));
            System.exit(1);
        }
        if (code == 2) {
            System.err.println(strings.getString("cantSaveImage"));
            System.exit(1);
        }

    }

    private void validateArgs(String[] args) {
        // exit the program if arguments are not valid
        if (args == null) {
            System.err.println(strings.getString("nullArgs"));
            System.err.println(strings.getString("howToUse"));
            System.exit(1);
        }
        if (args.length < 2) {
            System.err.println(strings.getString("notEnoughArgs"));
            System.err.println(strings.getString("howToUse"));
            System.exit(1);
        }
    }
}
