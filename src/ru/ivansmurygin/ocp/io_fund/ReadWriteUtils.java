package ru.ivansmurygin.ocp.io_fund;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by SmuryginIM on 25.03.2016.
 */
public class ReadWriteUtils {

    public static void readSequence(String file, int sequence) {
        try(DataInputStream fileStore = new DataInputStream(new FileInputStream(file))) {
            for (int i = 0; i < sequence; i++) {
                System.out.printf("%d %d %g \n",
                        fileStore.readByte(),
                        fileStore.readInt(),
                        fileStore.readFloat());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSequence(String file, int sequence) {
        try(DataOutputStream fileStore = new DataOutputStream(new FileOutputStream(file))) {
            for (int i = 0; i < sequence; i++) {
                fileStore.writeByte(i);
                fileStore.writeInt(i);
                fileStore.writeFloat(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromCharacterStream(String file){
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            int ch = 0;
            while ((ch = reader.read()) != -1){
                System.out.print((char) ch);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeToCharacterStream(String file, String text){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void findByPatternInFile(String file, String pattern){
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            scanner.useDelimiter(pattern);

            while (scanner.hasNext()){
                System.out.println(scanner.next());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void consoleExample(){
        Console console = System.console();

        String greeting = console.readLine("Input greetings:");
        char[] secretWord = console.readPassword("Type you secret:");

        console.printf("Greeting: %s\n Secret: %s", greeting, Arrays.toString(secretWord));
    }
}
