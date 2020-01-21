import java.util.regex.*;
import java.util.*;

public class EqualCheck implements ICheck
{


  public boolean check(StringBuffer buffer, String path)
  {

    String str = buffer.toString();

    String[] tokens = str.split("\n");

    ArrayList list = getMemberList(tokens);

    Iterator iterator = list.iterator();

    while(iterator.hasNext())
    {
        String member = (String) iterator.next();

        if( !checkEqual(member,buffer))
        {

            System.out.println("path :"+ path+ "  Violation EqualCheck"+"missing equal for  :"+member);
        }

    }

    return true;

  }

  private ArrayList getMemberList(String[] tokens)
  {

    ArrayList list = new ArrayList(11);

    for(int i=0; i< tokens.length; i++)
     {
       String str = tokens[i];

       if(RegexUtil.find("private .* m.*;",str))
        {

          String subStr = str.substring(str.indexOf(" m")+1,str.indexOf(';'));

          int index = subStr.indexOf("=");
          if(index != -1)
          {
              subStr = subStr.substring(0,index);
              subStr = subStr.trim();

          }
          list.add(subStr);


        }
     }

     return list;
  }

  private boolean checkEqual(String member, StringBuffer buffer)
  {

      boolean check = false;

      if(RegexUtil.find("CompareUtil.checkEquals.*"+member+".*",buffer))
      {

          check = true;
      }
      else if(RegexUtil.find(member+".* == .*",buffer))
      {
          check = true;
      }
      else if(RegexUtil.find("CompareUtil.checkEquals(get"+member.substring(1)+"().*)",buffer))
      {
          check = true;
      }
      else if(RegexUtil.find("get"+member.substring(1)+"(.*).*==.*",buffer))
      {
          check = true;
      }
      return check;

  }


}