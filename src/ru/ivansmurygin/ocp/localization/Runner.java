package ru.ivansmurygin.ocp.localization;

import java.util.*;

/**
 * Created by SmuryginIM on 08.03.2016.
 */
public class Runner {
    public static final String CURRENT_PACK = "ru.ivansmurygin.ocp.localization.resource_bundle";

    public static void main(String[] args) throws InterruptedException {
        testBundleProperties();

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
