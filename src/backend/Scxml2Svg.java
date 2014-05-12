package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;

/**
 * Class executing transformation of scxml document containing finite automaton
 * to its graphic representation in SVG format, using XSLT transform to DOT
 * format and GraphViz library to create SVG.
 *
 * @author Marek Zuzi
 */
public class Scxml2Svg {

    public void process(String[] args) {
        String srcFileName = args[0];
        String dstFileName = args[1];

        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);
        File dotFile = new File("graph.dot");
        //File xsdFile = new File("");
        //File templateFile = new File("");

        XmlTransformator trans = new XmlTransformator();
        XmlValidator valid = new XmlValidator();
        GraphVizConverter converter = new GraphVizConverter();

        // validate source file
        //valid.validateAgainstXsd(srcFile, xsdFile);
        //
        // transform to DOT
        //try {
        //    trans.transform(srcFile, templateFile, dotFile);
        //} catch (TransformerException ex) {
        //    Logger.getLogger(Scxml2Svg.class.getName()).log(Level.SEVERE, null, ex);
        //}
        // convert DOT to SVG
        converter.convertDotToSvg(dotFile, dstFile);
    }

    private void validateArgs(String[] args) {
        // exit the program if arguments are not valid
    }
}
