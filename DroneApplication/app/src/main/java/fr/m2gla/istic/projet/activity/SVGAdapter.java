package fr.m2gla.istic.projet.activity;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

/**
 * Created by fernando on 4/9/15.
 */
public class SVGAdapter {
    public static InputStream modifySVG(InputStream is, String newText1, String newText2, String color) {
        //String textId = "text3826";
        InputStream isOut = null;
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(is);
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
                            if (j==0) {
                                element.setTextContent(newText1);
                            } else {
                                element.setTextContent(newText2);
                            }
                        }
                    }
                //}
            }
            List<Node> listForms = new ArrayList<Node>();
            NodeList pathNodes = document.getElementsByTagName("path");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                listForms.add(pathNodes.item(i));
            }
            pathNodes = document.getElementsByTagName("rect");
            for (int i = 0; i < pathNodes.getLength(); i++) {
                listForms.add(pathNodes.item(i));
            }

            for (Node pathNode : listForms){
                NamedNodeMap attribute = pathNode.getAttributes();
                Node nodeAttrStyle = attribute.getNamedItem("style");
                nodeAttrStyle.setTextContent(nodeAttrStyle.getTextContent().replaceAll("stroke:#.*;", "stroke:#" + color + ";"));
            }
            isOut = document2InputStream(document);

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
        return isOut;
    }

    public static InputStream document2InputStream(Document document) throws IOException {
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
}
