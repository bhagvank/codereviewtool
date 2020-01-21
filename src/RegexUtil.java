
import java.util.regex.*;

public class RegexUtil
{

  public static boolean find(String regexp, String string)
  {
      Pattern pattern = Pattern.compile(regexp);

      Matcher matcher = pattern.matcher(string);

      return matcher.find();

  }


  public static boolean find(String regexp, StringBuffer buffer)
  {

      Pattern pattern = Pattern.compile(regexp);

      Matcher matcher = pattern.matcher(buffer);

      return matcher.find();

  }

  public static String group(String regexp,StringBuffer buffer)
  {
      Pattern pattern = Pattern.compile(regexp);

      Matcher matcher = pattern.matcher(buffer);

      matcher.find();

      return matcher.group();
  }


}