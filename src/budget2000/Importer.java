/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budget2000;

//import au.com.bytecode.opencsv.CSVReader;
import static budget2000.AbstractDbAdapter.logger;
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
import java.time.format.DateTimeFormatter;
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
            
            while ( (line = csvReader.readNext()) != null && line[0].length() != 0 ) {
                //"Transaction Date"  ,  "Posted Date"  ,  "Description"  ,  "Debit"  ,  "Credit"
                //"11/13/2014"  ,  "11/14/2014"  ,  "7-ELEVEN 12345 Foo FG"  ,  "41.33",""


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                LocalDate date = LocalDate.parse(line[0], formatter);

                String amount = "";
                if (line[3].length() != 0) {
                    amount = "-" + line[3];
                } else {
                    amount = line[4];
                }
                //System.out.println("LINE: " + line[0] + ":" + line[1] + ":" + line[2] + ":" +  line[3] + ":" + line[4] );
                //Expense(Long date, Long id, String merchant, String location, Double amount)
                //Expense expense = new Expense(date.getTime(), new Double(line[1]), line[2], line[3], new Double(line[4]));
                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.setParseBigDecimal(true);
                BigDecimal bigDecimal = (BigDecimal) decimalFormat.parse(amount);

                //Transaction transaction = new Transaction(localDate, line[2], bigDecimal);
                Transaction transaction = new Transaction();
                transaction.setDate(date.toEpochDay());
                transaction.setName(line[2]);
                transaction.setDisplayName(line[2]);
                transaction.setAmount(bigDecimal.doubleValue());

                transactionList.add(transaction);
                //System.out.println(transaction + "\n");
                // }
                //System.out.println("-------" + expenses.size() + "-----------");

            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);            
        } catch (IOException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {            
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
            //logger.log(Level.SEVERE, null, ex);
        }

        return transactionList;
    } // readData
}// Importer
