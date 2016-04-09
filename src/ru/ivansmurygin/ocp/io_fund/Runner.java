package ru.ivansmurygin.ocp.io_fund;

import java.io.*;
import java.util.*;

/**
 * Created by SmuryginIM on 25.03.2016.
 */
public class Runner {

    public static void main(String[] args) {
        objectStream();
    }

    public static void objectStream(){
        BlogInfo blogInfo = new BlogInfo(7, "I\\O Fundamaentals", "Hidden description", "static");
        try {
            System.out.println("Start serialization....");
            SerializationUtil.serialize(blogInfo, "info.txt");
            System.out.println("Start deserialization....");
            BlogInfo newInfo = (BlogInfo) SerializationUtil.deserialize("info.txt");

            System.out.println(newInfo);
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void markReader() {
        try (Reader r = new BufferedReader(new FileReader("to_read.txt"))) {
            //to check if reader supports marking
            if (r.markSupported()) {
                BufferedReader in = (BufferedReader) r;
                System.out.print(in.readLine());
                //we put mark which will be hold for 100 characters, then it will be removed
                in.mark(100);
                System.out.print(in.readLine());
                System.out.print(in.readLine());
                //attempt to return to the mark
                in.reset();
                System.out.print(in.readLine());
                in.reset();
                System.out.println(in.readLine());
            }else{
                System.out.println("Mark Not Supported");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dataOutputStream(){
        ReadWriteUtils.writeSequence("stream.data", 3);
        ReadWriteUtils.readSequence("stream.data", 3);
    }

    public static void randomAccessFile(){
        String data = "Position 0";
        String data2 = "Super reader/writer";
        String data3 = "Some absent data";
        String file = "temp.store";
        List<Long> pointers = new ArrayList<>();
        try (RandomAccessFile fileStore = new RandomAccessFile(file, "rw")){
            System.out.println("Start to write records");

            pointers.add(fileStore.getFilePointer());
            fileStore.writeUTF(data);
            pointers.add(fileStore.getFilePointer());
            fileStore.writeUTF(data2);
            pointers.add(fileStore.getFilePointer());
            fileStore.writeUTF(data3);

            System.out.println("Start to read only second record");
            fileStore.seek(pointers.get(1));
            String result = fileStore.readUTF();
            System.out.println("Result retrieved = " + result);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

}
