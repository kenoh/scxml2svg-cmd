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

    /**
     * Uses instance of GraphViz class to call use of GraphViz installed on the
     * computer in order to convert given DOT file to output SVG image file.
     *
     * @param dotFile file containing DOT format of graph to be visualized in
     * output.
     * @param svgFile output file to store generated SVG image.
     */
    public int convertDotToSvg(File dotFile, File svgFile) {
        GraphViz gv = new GraphViz();
        gv.readSource(dotFile.getPath());
        byte[] image = gv.getGraph(gv.getDotSource(), "svg");

        if (image == null) {
            // chyba
            return 1;
        }

        if (gv.writeGraphToFile(image, svgFile) != 1) {
            //chyba
            return 2;
        }

        return 0;
    }
}
