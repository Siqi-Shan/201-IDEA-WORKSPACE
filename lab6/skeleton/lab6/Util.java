package lab6;
import java.text.*;
import java.util.*;

public class Util
{
	public static String getDate(){

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
		Date date = new Date();
		String time = dateFormat.format(date);
		return time;
	}

	public static void print(String s)
	{
		System.out.println(s);
	}
}


