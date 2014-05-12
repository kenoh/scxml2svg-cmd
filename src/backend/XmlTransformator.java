package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Class, that can apply an XSLT transform to Xml files.
 *
 * @author Marek Zuzi
 */
public class XmlTransformator {

    /**
     * Transforms given Xml file using given XSLT template, saving its result to
     * destination file.
     *
     * @param sourceFile file to be transformed
     * @param templateFile template of XSLT transform
     * @param destFile file to save the result of the transform
     * @throws TransformerConfigurationException
     * @throws TransformerException
     */
    public void transform(File sourceFile, File templateFile, File destFile)
            throws TransformerConfigurationException, TransformerException {

        TransformerFactory tFactory = TransformerFactory.newInstance();

        StreamSource template = new StreamSource(templateFile);
        Transformer xTransformer = tFactory.newTransformer(template);

        StreamSource source = new StreamSource(sourceFile);
        StreamResult destination = new StreamResult(destFile);

        xTransformer.transform(source, destination);
    }
}
