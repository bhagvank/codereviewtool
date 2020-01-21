
import java.io.*;
import java.util.regex.*;
import java.util.*;
import java.lang.reflect.*;

/**
 *  This class is a tool for code reviewing
 *  It expects a input file which has the code
 *  rules with filetype,rulename, regexp delimited
 *  by #
 *  To run this tool, java CodeReviewTool <directory/filename> <coderulesfile>
 *  @author Bhagvan K
 *  @version 1.0
 */

public class CodeReviewTool
{

    // member variable for code rules
    private ArrayList mCodeRules = new ArrayList(11);

    private boolean mPrintCode = true;

    /**
     * main module where the code review is done
     * @param file the working file
     * @exception Exception
     */
    private void codeReview(File file) throws Exception
    {

        LineNumberReader reader = new LineNumberReader(new FileReader(file));

        String fileName = file.getName();

        String str = "";

        StringBuffer buffer = new StringBuffer();

        while(str != null)
        {
            str = reader.readLine();

            if(str != null)
            {
                buffer.append(str+"\n");
                // checking for comments
                if(! isComments(str))
                {

                    checkWithCodeRules(file.getAbsolutePath(),fileName,str,
                                       reader.getLineNumber());
                }
            }
        }


          checkGroupRules(file.getAbsolutePath(), fileName,buffer);


          checkCustomRules(file.getAbsolutePath(),fileName,buffer);


    }

    /**
     * checks for custom rules
     * @param path the file path
     * @param fileName the fileName
     * @param buffer the stringbuffer
     *
     */

     public void checkCustomRules(String path, String fileName, StringBuffer buffer) throws Exception
     {
       Iterator iterator = mCodeRules.iterator();

       while(iterator.hasNext())
       {

            CodeRule codeRule = (CodeRule) iterator.next();


            String parserMode = codeRule.getParserMode();

            String className  = codeRule.getRuleClass();

            if(!className.equals("Standard"))
            {

               String fileType = codeRule.getFileType();

               if(fileType.equals("General") || fileName.endsWith(fileType+".java"))
               {

                  Class ruleClass = Class.forName(className);

                  Constructor[] constructors = ruleClass.getConstructors();

                  Object object = constructors[0].newInstance(null);
                  Method method = getCheckMethod(ruleClass);

                  Object[] args = new Object[2];
                  args[0] = buffer;
                  args[1] = path;

                  method.invoke(object,args);

               }
            }
       }

     }

    /**
     * checks for Group rules
     * @param path the file path
     * @param fileName the fileName
     * @param buffer the stringbuffer
     */

     public void checkGroupRules(String path, String fileName, StringBuffer buffer)
     {
        Iterator iterator = mCodeRules.iterator();

        while(iterator.hasNext())
        {

            CodeRule codeRule = (CodeRule) iterator.next();


            String parserMode = codeRule.getParserMode();

            String ruleClass = codeRule.getRuleClass();

            if(parserMode.equals("Group") && ruleClass.equals("Standard"))
            {

               String fileType = codeRule.getFileType();

               if(fileType.equals("General") || fileName.endsWith(fileType+".java"))
               {
                  String regexp = codeRule.getRegExp();

                  if(RegexUtil.find(regexp,buffer))
                  {
                     if(isPrintCode())
                     {
                       System.out.println("path " + path+" Violation="+codeRule.getName()+" : "+RegexUtil.group(regexp,buffer));
                     }
                     else
                     {
                       System.out.println("path " + path+" Violation="+codeRule.getName());
                     }

                  }
               }
            }

        }


     }

    /**
     * checks for comments
     * @param str the string in the file
     * @return true if the str is a comment
     */
    private boolean isComments(String str)
    {
        String trimmedString = str.trim();

        if(str != null)
        {
            if(trimmedString.startsWith("/*") || trimmedString.startsWith("//")
            || trimmedString.startsWith("*"))
            {

                return true;
            }
            else
            {
                return false;
            }
        }

        return false;
    }

    /**
     * checks the string to a code rule
     * @param path file Path
     * @param fileName name of the java file
     * @param str the string to be checked for quality
     * @param lineNumber the line number
     *
     */
    private void checkWithCodeRules(String path,String fileName, String str,
                                    int lineNumber)
    {
        Iterator iterator = mCodeRules.iterator();

        while(iterator.hasNext())
        {

            CodeRule codeRule = (CodeRule) iterator.next();


            String parserMode = codeRule.getParserMode();

            String ruleClass = codeRule.getRuleClass();

            if(parserMode.equals("Single") && ruleClass.equals("Standard"))
            {
               String fileType = codeRule.getFileType();

               if(fileType.equals("General") ||
                  fileName.endsWith(fileType+".java"))
               {
                  String regexp = codeRule.getRegExp();

                  if(RegexUtil.find(regexp,str))
                  {
                     if(isPrintCode())
                     {
                       System.out.println("path " + path+ " LineNumber ="+
                        lineNumber+" Violation="+codeRule.getName()+ " : "+str);
                     }
                     else
                     {
                       System.out.println("path " + path+ " LineNumber ="+
                        lineNumber+" Violation="+codeRule.getName());
                     }

                  }
               }
            }

        }
    }

    /**
     * reads the code rules from the input file
     * @param fileName working file name
     * @exception Exception
     */
    public void readCodeRules(String fileName) throws Exception
    {

        LineNumberReader reader = new LineNumberReader(new FileReader(
                                                           new File(fileName)));

        String str = "";

        while(str != null)
        {
            str = reader.readLine();

            if(str != null && !str.startsWith("#"))
            {
                String[] tokens = str.split("#");

                CodeRule codeRule = new CodeRule(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4]);

                mCodeRules.add(codeRule);
            }

        }

    }
    /**
     * main method which expects two arguments
     * the directory or file name and the coderules file name (with the path)
     * @param args arguments to be passed
     * @exception Exception
     */
    public static void main(String[] args) throws Exception
    {

        File file = new File(args[0]);

        CodeReviewTool tool = new CodeReviewTool();
        if(args.length == 2)
        {
            tool.readCodeRules(args[1]);
        }
        if(args.length==3)
        {
            tool.setPrintCode(new Boolean(args[2]).booleanValue());
        }
        tool.recurse(file);
    }

    /**
     * recurses if the file is a directory
     * and calls the codereview method
     * @param     file working file
     * @exception Exception
     */
    public void recurse(File file) throws Exception
    {
        if(file.isDirectory())
        {

            File[] files = file.listFiles();

            for(int i=0; i < files.length; i++)
            {

                File fileTobeChecked = files[i];


                if( isJavaFile(fileTobeChecked))
                {

                    codeReview(fileTobeChecked);

                }

                if(fileTobeChecked.isDirectory())
                {

                    recurse(fileTobeChecked);

                }

            }

        }
        else
        {
            codeReview(file);
        }
    }
    /**
     * checks if the file is a java file
     * @param file  working file
     * @return whether the file is a java file
     */
    private boolean isJavaFile(File file)
    {

        String fileName = file.getName();

        if(fileName.endsWith(".java"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * checks for file type
     * @param file working file
     * @param fileType to be checked for
     * @return checks if the given file is of passed fileType
     */
    private boolean checkFileType(File file, String fileType)
    {
        String fileName = file.getName();

        return checkFileType(fileName,fileType);

    }
    /**
     * checks for file type
     * @param fileName name of the file
     * @param fileType to be checked for
     * @return checks if the given file is of passed fileType
     */
    private boolean checkFileType(String fileName, String fileType)
    {
        if(fileName.endsWith(fileType+".java"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    /**
     * sets the print code
     * @param printCode
     */
    public void setPrintCode(boolean printCode)
    {
        mPrintCode = printCode;
    }
    /**
     * gets the print code
     * @return the print code
     */
    public boolean isPrintCode()
    {
        return mPrintCode;
    }

    private Method getCheckMethod(Class ruleClass) throws Exception
    {
        Class[] params = new Class[2];

        params[0]  = StringBuffer.class;

        params[1]  = String.class;

        return ruleClass.getMethod("check",params);
    }
}

