package ru.ivansmurygin.ocp.localization;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SmuryginIM on 08.03.2016.
 */
public class Runner {
    public static final String CURRENT_PACK = "ru.ivansmurygin.ocp.localization.resource_bundle";

    public static void main(String[] args) throws InterruptedException {
        dateFormat();
    }

    static void simpleDateFormat(){
        Date currentDate = new Date();
        Locale.setDefault(Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        System.out.println("Current format date is " + sdf.format(currentDate));
    }

    static void dateFormat(){
        Locale.setDefault(Locale.CANADA);
        Date dt = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL, Locale.CANADA);
        DateFormat dfTime = DateFormat.getTimeInstance(DateFormat.FULL, Locale.CANADA);
        DateFormat dfDateTime = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);

        System.out.println("Date format simple instance: \t" + dateFormat.format(dt));
        System.out.println("Date format time instance \t" + dfTime.format(dt) );
        System.out.println("Date format date time instance \t" + dfDateTime.format(dt) );
    }

    static void numberFormat(){
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.CHINESE);
        NumberFormat nfCur = NumberFormat.getCurrencyInstance(Locale.CHINESE);
        NumberFormat nfPer = NumberFormat.getPercentInstance(Locale.CHINESE);
        System.out.printf("%10s \t %10s \t %15s \t %10s \t \n",
                "Number", "Simple", "Cur Format", "Per Format");
        Long digit = 10000000L;
        System.out.printf("%10s \t %10s \t %10s \t %10s \t \n",
                digit, numberFormat.format(digit), nfCur.format(digit), nfPer.format(digit));
        System.out.println("Currency for locale is " + numberFormat.getCurrency().getSymbol());
    }

    public static void testCurrency(){
        Locale indianLocale = new Locale.Builder().setRegion("IN").build();
        Currency indCur = Currency.getInstance(indianLocale);
        System.out.printf("Numeric code is %s and symbol is %s and currency Code is %s",
                indCur.getNumericCode(), indCur.getSymbol(), indCur.getCurrencyCode());
    }

    public static void testBundleProperties(){
        Locale locale = Locale.CANADA_FRENCH;
        printLocale(ResourceBundle.getBundle(CURRENT_PACK, locale));

        locale = Locale.GERMANY;
        printLocale(ResourceBundle.getBundle(CURRENT_PACK, locale));

        locale = new Locale.Builder().setLanguageTag("ru").setRegion("RU").build();
        printLocale(ResourceBundle.getBundle(CURRENT_PACK, locale));
    }

    static void printLocale(ResourceBundle resourceBundle){
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            System.out.printf("Value for %s is %s \n", key, resourceBundle.getString(key));

        }
    }

    public static void testLocale(){
        Collection<Locale> locales = Arrays.asList(Locale.CANADA_FRENCH, //predefined locales
                Locale.forLanguageTag("in"), // static factory
                new Locale("it", "IT"), // new constructor example
                new Locale.Builder().setLanguageTag("ru").setRegion("RU").build(), //builder example
                Locale.getDefault()); // current locale of the jvm
        System.out.printf("%10s \t %10s \t %10s \t %15s \t %10s \t %10s \t  \n",
                "Country", "Language", "Locale code",
                "D. Country", "D. Language", "Locale Name");
        for (Locale locale : locales) {
            System.out.printf("%10s \t %10s \t %10s \t %15s \t %10s \t %10s \t  \n",
                    locale.getCountry(), locale.getLanguage(), locale,
                    locale.getDisplayCountry(), locale.getDisplayLanguage(), locale.getDisplayName());
        }
    }

}
