import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;

public class ConvertforDisplay {
	public String getPersonalParticulars(File file,TextRetrieval textretrieval,String path)
	{
		String name=getName(file);
		ArrayList<String> list=textretrieval.getCVDetails(path);
		String retrievedData="",results="";
		
		retrievedData=list.get(1);
		retrievedData=retrievedData.replaceAll(" +", " ");
		retrievedData=retrievedData.replaceAll(";;", System.lineSeparator());
		retrievedData=retrievedData.replaceAll("^\\s+", "");

		if(retrievedData.contains("page1"))
		{
			//results="LinkedIn Profile : " + results.substring(results.lastIndexOf("page1")+5, results.length())+ System.getProperty("line.separator") +"Please contact through LinkedIn";
			results="LinkedIn Profile : " + getName(file)+ System.getProperty("line.separator") +"Please contact through LinkedIn";
		}
		else
		{
			results=getName(file)+ System.getProperty("line.separator")+ getEmail(retrievedData) + System.getProperty("line.separator") + extractPhoneNumber(retrievedData);;
		}

	return results;
	}
	
	private String getName(File file)
	{
		List<String> list;
		String parsed = "";
	   	String filename=file.toString().substring(file.toString().lastIndexOf("\\")+1, file.toString().lastIndexOf("."));
	   	if(filename.contains("_"))
	   	{
	   		list=Arrays.asList(filename.split("_"));
	   	}else{
	   		list=Arrays.asList(filename.split(" "));	   		
	   	}
	   	for(int i=0;i<list.size();i++)
	   	{
	   		if(!(list.get(i).toLowerCase().contains("cv")||list.get(i).toLowerCase().contains("resume")))
	   		{
	   			parsed+=list.get(i).replaceAll(" ", "");
	   		}
	   	}
	   	parsed=parsed.replaceAll("\\d", "");
	   	parsed=parsed.replaceAll("(\\p{Ll})(\\p{Lu})","$1 $2");
		return parsed;
	}
	
	private String getEmail(String str)
	{
		String email="";
	    Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(str);
	    while (m.find()) {
	        email=m.group();
	    }
	    if(email.contains(".com"))
	    {
	    	email=email.substring(0, email.indexOf(".com")+4);
	    }
	    return email;
	}
	
	public String extractPhoneNumber(String input){
		String temp="",numbers="",result="";
		List<String> list;
		list=Arrays.asList(input.replaceAll("[^-+()?0-9]+", " ").split(" ")); 
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).contains("+"))
			{
				numbers+=list.get(i)+" ";
			}
			else if(list.get(i).contains("(")&&list.get(i).contains(")"))
			{
				numbers+="+" + list.get(i).substring(list.get(i).indexOf("(")+1, list.get(i).indexOf(")"))+" ";
			}
			else if(list.get(i).length()%2==0 &&(list.get(i).length()==4 || list.get(i).length()>6))
			{
				numbers+=list.get(i)+" ";
			}
		}
		if (!numbers.contains("+"))
		{
			temp=numbers;
			numbers="+65 " + temp;
		}
		if(numbers.contains("+65")&&numbers.length()>13)
		{
			numbers=numbers.substring(0, 13);
		}
	    Iterator<PhoneNumberMatch> existsPhone=PhoneNumberUtil.getInstance().findNumbers(numbers, "IN").iterator();
	    while (existsPhone.hasNext()){
	        //System.out.println("Phone == " + existsPhone.next().number());
	        result="Phone == " + existsPhone.next().number();
	    }
	    return result;
	}
}