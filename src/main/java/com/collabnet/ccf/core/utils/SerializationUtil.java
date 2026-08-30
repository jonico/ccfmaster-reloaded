package com.collabnet.ccf.core.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

public abstract class SerializationUtil {

    public static <T> T deSerialize(File file, Class<T> className)
            throws JAXBException, IOException {
        return deSerialize(FileUtils.openInputStream(file), className);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deSerialize(InputStream stream, Class<T> className)
            throws JAXBException {
        JAXBContext xmlContext = JAXBContext.newInstance(className);
        Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
        return (T) unmarshaller.unmarshal(stream);
    }

}
