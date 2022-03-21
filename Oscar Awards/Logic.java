import java.io.*;
public class Logic {
    public static void main(String args[])
    {
        try  
        {  
            // String path="/home/nikhildewoolkar/Desktop/RedHat_Work/CSV_TO_JSON/the_oscar_award.csv";
            File file=new File("the_oscar_award.csv");//path
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line;
            String ans="[";
            System.out.println("year_film,year_ceremony,ceremony,category,name,film,winner");
            line=br.readLine();
            while((line=br.readLine())!=null)  
            {  
                String s="[\"";
                int commacount=0;
                int i=0;
                while(i<line.length())
                {
                    if(line.charAt(i)!=',' && line.charAt(i)!='\"')
                    {
                        s+=line.charAt(i);
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
                            if(commacount%2==0 && line.charAt(i+1)==',')
                            {
                                break;
                            }
                            s+=line.charAt(i);
                            i++;
                        }
                        commacount=0;
                    }
                    else if(line.charAt(i)==',')
                    {
                        s+="\""+","+"\"";
                    }
                    i++;
                }
                s+="\"]";
                ans+=s+",";
            }
            ans=ans.substring(0,ans.length()-1);
            ans+="]";
            ans=ans.replace("True","true");
            ans=ans.replace("False","true");
            fr.close();
            System.out.println(ans);
            BufferedWriter writer = new BufferedWriter(new FileWriter("ans.json"));
            writer.write(ans);
            writer.close();
        }  
        catch(Exception e)  
        {  
            e.printStackTrace();  
        }
    }
}
