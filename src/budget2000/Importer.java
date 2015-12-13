/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

//import au.com.bytecode.opencsv.CSVReader;
import com.opencsv.CSVReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Brian
 */
public class Importer {

    public ArrayList<Transaction> readData(File file) {
        CSVReader csvReader;
        ArrayList<Transaction> transactionList = new ArrayList<>();

        try {
//            csvReader = new CSVReader(new FileReader("sample.csv"));
            csvReader = new CSVReader(new FileReader(file));
            String[] line;
            line = csvReader.readNext(); // get first non line
            while ((line = csvReader.readNext()) != null) {
                // nextLine[] is an array of values from the line
                //System.out.println(line);
                //for ( String foo : nextLine ) {
                //System.out.println(foo.trim());

                //String[] line = csvReader.readNext();
//                    String out = String.format("%s, %s, %s, %s, %s", line[0], line[1], line[2], line[3], line[4]);
                //"Transaction Date"  ,  "Posted Date"  ,  "Description"  ,  "Debit"  ,  "Credit"
                //"11/13/2014"  ,  "11/14/2014"  ,  "7-ELEVEN 12345 Foo FG"  ,  "41.33",""
                //                   System.out.println(out);
                // convert date string to long
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                System.out.println("line 0 " + line[0]);
                System.out.println("line 1 " + line[1]);
                Date date = df.parse(line[0]);
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                Date date2 = df.parse(line[1]);

                String amount = "";
                if (line[3].length() != 0) {
                    amount = "-" + line[3];
                } else {
                    amount = line[4];
                }
                //Expense(Long date, Long id, String merchant, String location, Double amount)
                //Expense expense = new Expense(date.getTime(), new Double(line[1]), line[2], line[3], new Double(line[4]));
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setParseBigDecimal(true);
                BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse(amount);

                
                //Transaction transaction = new Transaction(localDate, line[2], bigDecimal);
                Transaction transaction = new Transaction();
                
                transactionList.add(transaction);
                System.out.println(transaction + "\n");
                // }
                //System.out.println("-------" + expenses.size() + "-----------");

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return transactionList;
    } // readData
}// Importer
