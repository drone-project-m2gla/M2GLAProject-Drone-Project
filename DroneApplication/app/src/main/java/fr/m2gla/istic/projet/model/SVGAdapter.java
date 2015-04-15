package fr.m2gla.istic.projet.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import fr.m2gla.istic.projet.model.Symbol;

/**
 * Created by fernando on 4/9/15.
 */
public class SVGAdapter {

    /**
     * Modifies the given SVG input stream by changing the texts and color with the given values
     * @param inputStream
     * @param symbol
     * @return
     */
    public static InputStream modifySVG(InputStream inputStream, Symbol symbol) {
        InputStream isOut = null;
        String newText1 = symbol.getFirstText();
        String newText2 = symbol.getSecondText();
        if (newText1 == null) newText1 = "";
        if (newText2 == null) newText2 = "";
        String color = symbol.getColor();
        if (color == null || color.length() != 6) color = "ff0000";
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            NodeList textNodes = document.getElementsByTagName("text");
            for (int i = 0; i < textNodes.getLength(); i++){
                Node textNode = textNodes.item(i);
                //NamedNodeMap attribute = textNode.getAttributes();
                //Node nodeAttrId = attribute.getNamedItem("id");
                //if (nodeAttrId.getTextContent().equals(textId)) {
                    NodeList textChildNodes = textNode.getChildNodes();
                    for (int j = 0; j < textChildNodes.getLength(); j++) {
                        Node element = textChildNodes.item(j);
                        if ("tspan".equals(element.getNodeName())) {
                            if (i == 0) {
                                element.setTextContent(newText1);
                            } else {
                                element.setTextContent(newText2);
                            }
                            NamedNodeMap attribute = element.getAttributes();
                            Node nodeAttrStyle = attribute.getNamedItem("style");
                            if (nodeAttrStyle != null) {
                                nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("fill:#ff00ff;", "fill:#" + color + ";"));
                            }
                        }
                    }
                //}
            }

            NodeList pathNodes = document.getElementsByTagName("rect");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                Node pathNode = pathNodes.item(i);
                NamedNodeMap attribute = pathNode.getAttributes();
                Node nodeAttrStyle = attribute.getNamedItem("style");
                nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("stroke:#ff00ff;", "stroke:#" + color + ";"));
                nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("fill:#ff00ff;", "fill:#" + color + ";"));
                if (symbol.isValidated()) {
                    nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("stroke-dasharray:.*;", "stroke-dasharray:none;"));
                } else {
                    nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("stroke-dasharray:none;", "stroke-dasharray:4,4;"));
                }
            }

            List<Node> listForms = new ArrayList<Node>();
            pathNodes = document.getElementsByTagName("path");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                listForms.add(pathNodes.item(i));
            }

            pathNodes = document.getElementsByTagName("flowRoot");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                listForms.add(pathNodes.item(i));
            }

            for (Node pathNode : listForms){
                NamedNodeMap attribute = pathNode.getAttributes();
                Node nodeAttrStyle = attribute.getNamedItem("style");
                nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("stroke:#ff00ff;", "stroke:#" + color + ";"));
                nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("fill:#ff00ff;", "fill:#" + color + ";"));
            }
            isOut = documentToInputStream(document);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        return isOut;
    }

    /**
     * Convert an XML document into an InputStream
     * @param document
     * @return
     * @throws IOException
     */
    public static InputStream documentToInputStream(Document document) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Source xmlSource = new DOMSource(document);
        Result outputTarget = new StreamResult(outputStream);
        InputStream isOut = null;
        try {
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            isOut = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return isOut;
    }

    /**
     * Converts a drawable object into a bitmap
     *
     * @param drawable     drawable object to convert
     * @param widthPixels  output image width
     * @param heightPixels output image height
     * @return converted bitmap
     */
    public static Bitmap convertDrawableToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}
