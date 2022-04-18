import java.nio.file.Files;
import java.nio.file.Paths;
public class Logic {
    public static void main(String args[])
    {
        try  
        {  
            String path="/home/nikhildewoolkar/Desktop/RedHat_Work/CSV_TO_JSON/the_oscar_award.csv";
            StringBuffer ans=new StringBuffer("[");
            Files.lines(Paths.get(path)).skip(1).forEach(line -> {
                StringBuffer s=new StringBuffer("[\"");
                int commacount=0;
                int i=0;
                while(i<line.length())
                {
                    if(line.charAt(i)!=',' && line.charAt(i)!='\"')
                    {
                        s.append(line.charAt(i));
                    }
                    else if(line.length()>i+1 && line.charAt(i)=='\"')
                    {
                        i++;
                        commacount++;
                        while(true)
                        {
                            
                            if(line.charAt(i)=='\"')
                            {
                                commacount++;
                            }
                            if(commacount%2==0 && (i+1>=line.length() || line.charAt(i+1)==','))
                            {
                                break;
                            }
                            s.append(line.charAt(i));
                            i++;
                        }
                        commacount=0;
                    }
                    else if(line.charAt(i)==',')
                    {
                        s.append("\""+","+"\"");
                    }
                    i++;
                }
                s.append("\"]");
                String s1=s.toString();
                int z=1;
                while(z<s1.length())
                {
                    if(s1.substring(z-1,z+1).equals("\"\"") && (z+1<s1.length() && s1.charAt(z+1)!=','))
                    {
                        s1 = s1.substring(0, z-1) + "\\\"" + s1.substring(z+1);
                    }
                    z++;
                }
                ans.append(s1+(","));
        });
            String ans1=ans.toString();
            ans1=ans1.substring(0,ans.length()-1);
            ans1+="]";
            ans1=ans1.replace("True","true");
            ans1=ans1.replace("False","false");
            Files.writeString(Paths.get("ans.json"), ans1);
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }
    }
}

