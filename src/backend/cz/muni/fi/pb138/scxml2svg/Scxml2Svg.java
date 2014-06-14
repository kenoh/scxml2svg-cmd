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
    private static final String SCHEMA = "scxml.xsd";
    private static final String TEMPLATE = "xsl/simplified-scxml-to-dot.xsl";
    private static final String DOT = "graph.dot";

    /**
     * Wraps all the functionality and handles errors. First, validates command
     * line arguments. If valid, validates input file and processes it to DOT
     * graph, and then to SVG using GraphViz. If any error occurs, exits the
     * program and prints error message.
     *
     * @param args command line parameters recieved at startup.
     */
    public void process(String[] args) {
        if (args.length != 2 && args.length != 3) {
            System.err.println(strings.getString("notEnoughArgs"));
            System.err.println(strings.getString("howToUse"));
            System.exit(1);
        }

        String srcFileName = args[0];
        String dstFileName = args[1];
        String templFileName;
        if (args.length == 3) {
            templFileName = args[2];
        } else {
            templFileName = TEMPLATE;
        }
        String dotFileName = DOT;
        String schemaFileName = SCHEMA;

        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);
        File templFile = new File(templFileName);
        File dotFile = new File(dotFileName);
        File schemaFile = new File(schemaFileName);

        XmlTransformator trans = new XmlTransformator();
        XmlValidator valid = new XmlValidator();
        GraphVizConverter converter = new GraphVizConverter();

        // validate source file
        boolean isValid = false;
        try {
            isValid = valid.validateAgainstXsd(new FileInputStream(srcFile), new FileInputStream(schemaFile));
        } catch (FileNotFoundException e) {
            System.err.println(strings.getString("fileNotFound"));
            System.err.println(e.getMessage());
            System.exit(2);
        } catch (IllegalArgumentException e) {
            System.err.println(strings.getString("badSchema"));
            System.err.println(e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println(strings.getString("validFailed"));
            System.exit(2);
        }
        if (!isValid) {
            System.err.println(strings.getString("inputNotValid"));
            System.exit(1);
        } else {
            System.out.println(strings.getString("inputValid"));
        }

        // transform to DOT
        try {
            trans.transform(srcFile, templFile, dotFile);
        } catch (TransformerConfigurationException e) {
            System.err.println(strings.getString("badTemplate"));
            System.err.println(e.getMessage());
            System.exit(3);
        } catch (TransformerException e) {
            System.err.println(strings.getString("transformFailed"));
            System.err.println(e.getMessage());
            System.exit(3);
        } catch (IllegalArgumentException e) {
            System.err.println(strings.getString("fileNotFound"));
            System.err.println(e.getMessage());
            System.exit(3);
        }
        System.out.println(strings.getString("transformComplete"));

        // convert DOT to SVG
        int code = converter.convertDotToSvg(dotFile, dstFile);
        if (code == 1) {
            System.err.println(strings.getString("cantCreateImage"));
            System.exit(4);
        }
        if (code == 2) {
            System.err.println(strings.getString("cantSaveImage"));
            System.exit(4);
        }

    }

}
