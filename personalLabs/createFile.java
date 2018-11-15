import java.io.*;
import java.lang.*;
import java.util.*;

public class createfile{

        private Formatter x;


        public void openFile(){

                try{
                //making a file we can use
                x = new Formatter("chinese");
                }
                catch(Exception e){

                        System.out.println("you have an error");
                }
        }
        public void addRecords(){

        x.format("%s%s%s", "20", "bucky", "roberts");

        }

        public void closeFile(){
        x.close();
        }

}
