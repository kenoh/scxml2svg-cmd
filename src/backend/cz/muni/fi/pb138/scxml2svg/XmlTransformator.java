package cz.muni.fi.pb138.scxml2svg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * @throws IllegalArgumentException if there is a problem with opening any
     * of files.
     * @throws TransformerConfigurationException Thrown if there are errors when
     * parsing the Source or it is not possible to create a Transformer
     * instance.
     * @throws TransformerException If an unrecoverable error occurs during the
     * course of the transformation.
     */
    public void transform(File sourceFile, File templateFile, File destFile)
            throws TransformerConfigurationException, TransformerException {

        InputStream source = null;
        try {
            source = new FileInputStream(sourceFile);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("sourceFile can not be opened");
        }

        InputStream template = null;
        try {
            template = new FileInputStream(templateFile);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("templateFile can not be opened");
        }

        OutputStream dest = null;
        try {
            dest = new FileOutputStream(destFile);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("destFile can not be opened");
        }

        transform(source, template, dest);
    }

    /**
     * Transforms given Xml file using given XSLT template, saving its result to
     * destination file.
     *
     * @param sourceStream stream from file to be transformed
     * @param templateStream template of XSLT transform
     * @param destStream stream to save the result of the transform
     * @throws TransformerConfigurationException Thrown if there are errors when
     * parsing the Source or it is not possible to create a Transformer
     * instance.
     * @throws TransformerException If an unrecoverable error occurs during the
     * course of the transformation.
     */
    public void transform(InputStream sourceStream, InputStream templateStream, OutputStream destStream)
            throws TransformerConfigurationException, TransformerException {

        TransformerFactory tFactory = TransformerFactory.newInstance();

        StreamSource template = new StreamSource(templateStream);
        Transformer xTransformer = tFactory.newTransformer(template);

        StreamSource source = new StreamSource(sourceStream);
        StreamResult destination = new StreamResult(destStream);

        xTransformer.transform(source, destination);
    }
}
