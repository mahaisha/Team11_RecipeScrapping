
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Eliminators {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}
	
	
	   
	    
	   
	
	public boolean Diab_Receipe_Noteatable(String Ingredients, Eliminator elimatorclass) throws JsonParseException, JsonMappingException, IOException
	{
		
		String Diab_constrainsts[] = elimatorclass.diabetes.split(",");
			
	    for(String item : Diab_constrainsts)
	    {
	        if (Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true ;
	        }
	    }

	    return false;
	}
	
	
	public boolean HyperTen_Receipe_Noteatable(String Ingredients, Eliminator elimatorclass) throws JsonParseException, JsonMappingException, IOException
	{
		
		String HyperTEns_constrainsts[] = elimatorclass.hypertension.split(",");;
			
	    for(String item : HyperTEns_constrainsts)
	    {
	    	if (Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	
	public boolean HypoTyrod_Receipe_Noteatable(String Ingredients, Eliminator elimatorclass)
	{
		String HyperThy[] = elimatorclass.hypothyroid.split(",");;
			
	    for(String item : HyperThy)
	    {
	    	if (Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	public boolean PCOS_Receipe_Noteatable(String Ingredients, Eliminator elimatorclass)
	{
		String PCOS_constrainsts[] = elimatorclass.pcos.split(",");;
			
	    for(String item : PCOS_constrainsts)
	    {
	        if(Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	public boolean Diab_Receipe_good_ingrdents(String Ingredients, Eliminator elimatorclass) throws JsonParseException, JsonMappingException, IOException
	{
		
		String Diab_constrainsts[] = elimatorclass.diabetesgoodingredients.split(",");
			
	    for(String item : Diab_constrainsts)
	    {
	        if (Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true ;
	        }
	    }

	    return false;
	}
	
	
	public boolean HyperTen_Receipe_good_ingrdents(String Ingredients, Eliminator elimatorclass) throws JsonParseException, JsonMappingException, IOException
	{
		
		String HyperTEns_constrainsts[] = elimatorclass.hypertensiongoodingredients.split(",");;
			
	    for(String item : HyperTEns_constrainsts)
	    {
	    	if (Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	
	public boolean HypoTyrod_Receipe_good_ingrdents(String Ingredients, Eliminator elimatorclass)
	{
		String HyperThy[] = elimatorclass.hypothyroidgoodingredients.split(",");;
			
	    for(String item : HyperThy)
	    {
	    	if (Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true;
	        }
	    }

	    return false;
	}
	
	public boolean PCOS_Receipe_good_ingrdents(String Ingredients, Eliminator elimatorclass)
	{
		String PCOS_constrainsts[] = elimatorclass.pcosgoodingredients.split(",");;
			
	    for(String item : PCOS_constrainsts)
	    {
	        if(Ingredients.toUpperCase().contains(item.toUpperCase()))
	        {
	            return true;
	        }
	    }

	    return false;
	}


}
