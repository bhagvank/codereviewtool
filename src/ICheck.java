/**
 *  This will be used for further enhancements
 *  of this tool
 */
public interface ICheck
{
    /**
     * checks fileStrings for code quality
     * @param fileStrings  list of fileStrings
     * @return checks for code quality
     */
    public boolean check(StringBuffer stringBuffer,String path);
}