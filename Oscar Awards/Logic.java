import java.nio.file.Files;
import java.nio.file.Paths;
public class Logic {
    public static void main(String args[])
    {
        try  
        {  
            String path="/home/nikhildewoolkar/Desktop/RedHat_Work/CSV_TO_JSON/the_oscar_award.csv";
            StringBuffer ans=new StringBuffer("[");
            // StringBuffer k=new StringBuffer("1");
            Files.lines(Paths.get(path)).skip(1).forEach(line -> {
                // if(!k.toString().equals("1"))
                // {
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
                // ["1930","1931","4","CINEMATOGRAPHY","Barney ""Chick"" McGill","Svengali","false"]
                String s1=s.toString();
                int z=1;
                while(z+1<s1.length())
                {
                    // System.out.println(s1.substring(z,z+2)+" "+s1+" "+s1.charAt(z+1));System.out.println(s1);
                    if(s1.substring(z,z+2).equals("\"\"") && s1.charAt(z+2)!=',')
                    {
                        s1 = s1.substring(0, z) + "\\\"" + s1.substring(z + 2);
                    }
                    z++;
                }
                ans.append(s1+(","));
            // }
            // k.setLength(0);
            // k.append("0");  
        });
            String ans1=ans.toString();
            ans1=ans1.substring(0,ans.length()-1);
            ans1+="]";
            ans1=ans1.replace("True","true");
            ans1=ans1.replace("False","false");
            // System.out.println(ans);
            Files.writeString(Paths.get("ans.json"), ans1);
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }
    }
}

