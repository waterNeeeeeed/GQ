import java.nio.charset.Charset;
import java.util.SortedMap;
import java.time.*;
import java.time.format.*;

public class CharsetTest {
	public static void main(String[] args){
		/*SortedMap<String, Charset> map = Charset.availableCharsets();
		for (String alias : map.keySet()){
			System.out.println(alias);*/
		LocalDateTime ld = LocalDateTime.now();
		System.out.println(ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd" + " " + "HH:mm:ss")));
			
			
		}
	
}
