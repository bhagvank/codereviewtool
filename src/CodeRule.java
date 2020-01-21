/**
 * This class is used for modelling
 * a code rule
 *
 */
public class CodeRule
{

    // member variable representing the fileType
    private String mFileType;

    // member variable representing the rule name
    private String mName;

    // member variable representing the regular expression
    private String mRegexp;

    // member variable representing the file to be parsed in single line
    // or group (the whole file)
    private String mParserMode;

    // name of the class
    private String mRuleClass;

    /**
     * Class Constructor
     *
     */
    public CodeRule()
    {
    }


    /**
     * Class Constructor with filetype, rulename and regular expression
     * parameters
     * @param fileType the type of the file
     * @param name  rule Name
     * @param regexp regular expression
     */
    public CodeRule(String ruleClass,String parserMode,String fileType,String name,String regexp)
    {
        setRuleClass(ruleClass);

        setParserMode(parserMode);

        setFileType(fileType);

        setName(name);

        setRegExp(regexp);
    }

    /**
     * gets the FileType
     * @return the fileType
     */
    public String getFileType()
    {
        return mFileType;
    }

    /**
     * gets the rule name
     * @return the name
     */
    public String getName()
    {
        return mName;
    }

    /**
     * gets the regular expression
     * @return the regularexpression
     */
    public String getRegExp()
    {
        return mRegexp;
    }

    /**
     * sets the file Type
     * @param fileType
     */
    public void setFileType(String fileType)
    {
        mFileType = fileType;

    }

    /**
     * sets the rule name
     * @param name rule name
     */
    public void setName(String name)
    {
        mName = name;
    }

    /**
     * sets the regular expression
     * @param regexp regular expression
     */
    public void setRegExp(String regexp)
    {
        mRegexp   = regexp;
    }

    /**
     * sets the parserMode
     * @param parserMode single or group
     */
    public void setParserMode(String parserMode)
    {
        mParserMode = parserMode;
    }

    /**
     * gets the parser mode
     *
     * @return the parser mode
     */
    public String getParserMode()
    {
        return mParserMode;
    }

    public String getRuleClass()
    {
        return mRuleClass;
    }

    public void setRuleClass(String ruleClass)
    {
        mRuleClass = ruleClass;
    }

}