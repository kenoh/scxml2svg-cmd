package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import graphViz.GraphViz;

/**
 * Class enabling user to style and visualize finite automaton described in DOT
 * format to output SVG image using GraphViz functionality.
 *
 * @author Marek Zuzi
 */
public class GraphVizConverter {

    public void convertDotToSvg(File dotFile, File svgFile) {
        GraphViz gv = new GraphViz();
        gv.readSource(dotFile.getPath());
        byte[] image = gv.getGraph(gv.getDotSource(), "dot");
        
        if(image == null) {
            // chyba
        }
        
        if(gv.writeGraphToFile(image, svgFile) != 1) {
            //chyba
        }
    }
}
