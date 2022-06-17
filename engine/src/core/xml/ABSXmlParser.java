package core.xml;

import core.entities.Customer;
import core.entities.Loan;
import core.entities.Transaction;
import core.xml.schema.AbsCustomer;
import core.xml.schema.AbsDescriptor;
import core.xml.schema.AbsLoan;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class ABSXmlParser {
    private AbsDescriptor descriptor;
    private static  final String JAX_XML_PACKAGE_NAME = "core.xml.schema";
    private static final String goodUrl = "C:\\Users\\TEST\\Desktop\\ex1-small.xml";
    private static final String badUrl = "C:\\Users\\TEST\\Desktop\\ex1-error-2.2.xml";
    public ABSXmlParser(String path) {
        try{
            InputStream inputStream = new FileInputStream(path);
            descriptor  = deserializerForm(inputStream);

        } catch (FileNotFoundException | JAXBException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCategoriesFromFile(){
        return descriptor.getAbsCategories().getAbsCategory();
    }

    public List<Customer> getCustomersFromFile(){
        List<Customer> res = new ArrayList<>();

        List<AbsCustomer> temp = descriptor.getAbsCustomers().getAbsCustomer();

        for (int i = 0; i < temp.size(); i++) {
            AbsCustomer curC = temp.get(i);
            res.add(new Customer(curC.getName(),curC.getAbsBalance(),
                    new ArrayList<Loan>(),new ArrayList<Loan>(),
                    new ArrayList<Transaction>()));
        }
        return res;
    }

    public List<Loan> getLoansFromFile(){
        List<Loan> res = new ArrayList<>();
        List<AbsLoan> tempList = descriptor.getAbsLoans().getAbsLoan();

        for (int i = 0; i < tempList.size(); i++) {
            AbsLoan curL = tempList.get(i);
            String loanId = curL.getId();
            String ownerName = curL.getAbsOwner();
            String category = curL.getAbsCategory();
            int capital = curL.getAbsCapital();
            int totalYaz = curL.getAbsTotalYazTime();
            int payEveryYaz = curL.getAbsPaysEveryYaz();
            int interestPerPayment = curL.getAbsIntristPerPayment();

            res.add(new Loan(loanId,1,
                    capital,null,new ArrayList<Customer>(),
                    Loan.LoanStatus.NEW,category,interestPerPayment,ownerName,totalYaz,payEveryYaz));
            }

        return res;
    }


    public static AbsDescriptor deserializerForm(InputStream in) throws JAXBException{

        JAXBContext jc  = JAXBContext.newInstance(JAX_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (AbsDescriptor) u.unmarshal(in);

    }
}
