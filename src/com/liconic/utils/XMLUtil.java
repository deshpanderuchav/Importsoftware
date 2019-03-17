package com.liconic.utils;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XMLUtil {

    public static String xmlToString(Object jaxbElement, Class classToBeBound, Class objectFactoryToBeBound) {
        StringWriter sw = new StringWriter();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(classToBeBound, objectFactoryToBeBound);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(jaxbElement, sw);
        } catch (JAXBException ex) {
            Logger.getLogger(XMLUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sw.toString();
    }

}
