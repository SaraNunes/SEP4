package logConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.GregorianCalendar;

import oracle.net.aso.b;
   
public class WeatherConverter {

      private File[] files;
      private DatabaseCommunication db;
      
      public WeatherConverter() throws IOException {
         File forder = new File("weather_logs");
         files = forder.listFiles();
         db = DatabaseCommunication.getInstance();
         for (int a = 0; a < files.length; a++) {
            System.out.println(files[a].getName());
            convert(files[a]);
         }
      }
      
      public void convert(File file) throws IOException {
         FileReader fileReader = new FileReader(file);
         BufferedReader bufferedReader = new BufferedReader(fileReader);
         String line;
         Weather weather = null;
         
         while((line = bufferedReader.readLine()) != null) {
            if(line.charAt(0) == 'M' || line.length() < 56){
               weather = new Weather(
                     line.substring(6, 10),
                     Integer.parseInt(line.substring(11, 13)),
                     Integer.parseInt(line.substring(13, 15)),
                     Integer.parseInt(line.substring(15, 17)),
                     Double.parseDouble(line.substring(24, 27)),              
                     Double.parseDouble(line.substring(27, 29)),
                     line.substring(32,39),
                     line.substring(40, 43),
                     Double.parseDouble(line.substring(44, 46)),
                     Double.parseDouble(line.substring(47, 49)),
                     Double.parseDouble(line.substring(51, 55)));
            }
            else if(line.charAt(0) == 'M' || line.length() > 56){
               weather = new Weather(
                     line.substring(6, 10),
                     Integer.parseInt(line.substring(11, 13)),
                     Integer.parseInt(line.substring(13, 15)),
                     Integer.parseInt(line.substring(15, 17)),
                     Double.parseDouble(line.substring(24, 27)),              
                     Double.parseDouble(line.substring(27, 29)),
                     line.substring(32,39),
                     line.substring(40, 46),
                     Double.parseDouble(line.substring(47, 49)),
                     Double.parseDouble(line.substring(50, 52)),
                     Double.parseDouble(line.substring(55, 58)));
               
            }
            try {
               db.insertWeather(weather, file.getName().substring(0, file.getName().length()));
            } catch (SQLException e) {
               System.out.println(line);
               e.printStackTrace();
            }
               
           
         }
         
      }
         public static void main(String[] args) throws IOException {
            WeatherConverter converter = new WeatherConverter();
         }
      }
