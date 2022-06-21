package utils.xml;


import utils.xml.generated.AbsDescriptor;
import utils.xml.generated.AbsLoan;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;


public class ClientXmlParser {
    private AbsDescriptor descriptor;
    private static  final String JAX_XML_PACKAGE_NAME = "utils.xml.generated";

    public ClientXmlParser(String path) {
        try{
            InputStream inputStream = new FileInputStream(path);
            descriptor  = deserializerForm(inputStream);


        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public List<AbsLoan> getLoans(){
        return descriptor.getAbsLoans().getAbsLoan();

    }

    public static AbsDescriptor deserializerForm(InputStream in) throws JAXBException {

        JAXBContext jc  = JAXBContext.newInstance(JAX_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (utils.xml.generated.AbsDescriptor) u.unmarshal(in);

    }
}
