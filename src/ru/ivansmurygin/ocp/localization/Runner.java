import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

/**
 * Created by SmuryginIM on 08.03.2016.
 */
public class Runner {
    public static void main(String[] args) throws InterruptedException {
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
