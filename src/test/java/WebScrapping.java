import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class WebScrapping {

	static Date date = Calendar.getInstance().getTime();
	static DateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
	static String strDate = dateFormat.format(date);

	public static void main(String[] args) throws InterruptedException, IOException {

		//Object Mapper provides functionality for reading and writing JSON as class Objects
		ObjectMapper om = new ObjectMapper();
		Eliminator elimatorclass = om.readValue(new File("src/main/resources/configuration.json"), Eliminator.class);

		//Reading the Alphabets and corresponding page numbers from Json file and storing it as Key Value pair
		Map<String, Integer> hm = new HashMap<String, Integer>();

		String pagelistvalues[] = elimatorclass.paglist.split(",");

		for (String paglist : pagelistvalues) {

			String pageval[] = paglist.split("@");

			hm.put(pageval[0], Integer.parseInt(pageval[1]));// hm.put(A, 22)
		}

		//initializing web driver
		System.setProperty("Webdriver.edge.driver", elimatorclass.property);
		int row = 1;

		//dynamically taking the path from json file 
		String path = elimatorclass.path + strDate + ".xlsx";
		
		//Creating object for class which has method to write it to excel
		XLUtility xlutil = new XLUtility(path);
		
		//Handle exception 
		try {

			xlutil.setCellData("Sheet1", 0, 0, "Receipe ID");
			xlutil.setCellData("Sheet1", 0, 1, "Receipe Name");
			xlutil.setCellData("Sheet1", 0, 2, "URL");
			xlutil.setCellData("Sheet1", 0, 3, "Recipe_Category");
			xlutil.setCellData("Sheet1", 0, 4, "Food_Category");
			xlutil.setCellData("Sheet1", 0, 5, "Eatable (Good For)");
			xlutil.setCellData("Sheet1", 0, 6, "Allergy Information");
			xlutil.setCellData("Sheet1", 0, 7, "Ingredients");
			xlutil.setCellData("Sheet1", 0, 8, "Preparation Time");
			xlutil.setCellData("Sheet1", 0, 9, "Cooking Time");
			xlutil.setCellData("Sheet1", 0, 10, "Preperation Method");
			xlutil.setCellData("Sheet1", 0, 11, "Nutrition Value");
			xlutil.setCellData("Sheet1", 0, 12, "ADD Ingredients List for Diabetic");
			xlutil.setCellData("Sheet1", 0, 13, "ADD Ingredients List for Hypertension");
			xlutil.setCellData("Sheet1", 0, 14, "ADD Ingredients List for Hypothyroid");
			xlutil.setCellData("Sheet1", 0, 15, "ADD Ingredients List for PCOS");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String url = "";

		//iterate through each alphabet and page index and scrap all the links within the page index
		for (Map.Entry<String, Integer> me : hm.entrySet()) {

			for (int i = 1; i <= me.getValue(); i++) {
				try {
					System.out.println("--------------------------------------------------- page is " + i);
					
					//opening the driver
					WebDriver driver = new EdgeDriver();
					driver.manage().window().maximize();
					url = elimatorclass.url + me.getKey() + "&pageindex=" + i;
					driver.get(url);
					List<WebElement> raw_recipes = driver.findElements(By.className("rcc_recipename"));
					ArrayList<String> links = new ArrayList<>(14);

					for (WebElement e : raw_recipes) {

						String clickonlinkTab = Keys.chord(Keys.CONTROL, Keys.ENTER);

						e.findElement(By.tagName("a")).sendKeys(clickonlinkTab);
					}
					Thread.sleep(5000);
					Set<String> abc = driver.getWindowHandles();
					Iterator<String> it = abc.iterator();

					while (it.hasNext()) {

						driver.switchTo().window(it.next());
						Thread.sleep(1000);

						// Getting URL
						String URL = driver.getCurrentUrl();
						

						// Getting Recipe Name
						String Recipe_Name = "";
						String Recipe_Name1 = driver.getCurrentUrl().split(".com")[1].split("/")[1];
						
						String list[] = Recipe_Name1.split("-");
						if (list.length < 3) {
							for (int J = 0; J < list.length - 1; J++)
								Recipe_Name = Recipe_Name + "\t" + list[J];
						} else {
							for (int J = 0; J < list.length - 2; J++)
								Recipe_Name = Recipe_Name + "\t" + list[J];
						}
						// Getting Recipe ID
						String RecipeID = list[list.length - 1];
						RecipeID = RecipeID.replace('r', ' ');					

						if (RecipeID.contains("RecipeAtoZ"))
							continue;
						
						// Getting Prp time & Cook time
						String Preparation_Time = driver.findElement(By.xpath("//time[@itemprop ='prepTime']"))
								.getText();
						String Cooking_Time = driver.findElement(By.xpath("//time[@itemprop ='cookTime']")).getText();

						// Getting Recipe Category
						String Recipe_Category = "";
						String Search_text[] = { "BREAKFAST", "LUNCH", "DINNER", "SNACK" };

						WebElement Search_item = driver.findElement(By.id("recipe_tags"));
						
						for (String text : Search_text) {
							
							if (Search_item.getText().toUpperCase().contains(text))
								Recipe_Category = Recipe_Category + "\n" + text;

						}

						// Nutrition Value
						String Nutrition = "";
						List<WebElement> list_values = driver.findElements(By.xpath("//table[@id='rcpnutrients']//td"));
						String Nutrition_Values = "";
						for (WebElement e1 : list_values) {
							Nutrition = Nutrition + "\t" + e1.getText();
							
						}

					

						// Getting Preparation Method
						List<WebElement> Method = driver.findElements(By.xpath("//span[@itemprop='text']"));
						String method = "";
						for (WebElement e1 : Method) {
							method = method + "\n" + e1.getText();
							
						}

						// Getting Ingredients
						List<WebElement> ingredints = driver
								.findElements(By.xpath("//span[@itemprop='recipeIngredient']"));
						
						String Ingredients = "";
						for (WebElement e1 : ingredints) {
							Ingredients = Ingredients + "\n" + e1.getText();
							
						}

						List<WebElement> ingredints_name = driver
								.findElements(By.xpath("//span[@itemprop='recipeIngredient']/a/span"));
						
						String Ingredients_name = "";
						for (WebElement e1 : ingredints_name) {
							Ingredients_name = Ingredients_name + "\n" + e1.getText();
							
						}
						
						// Getting Food Category(Veg/non-veg/vegan/Jain/Egg)

						String Food_Category = "";
						String Search_text2[] = { "VEG", "NON-VEG", "JAIN" };
					
						for (String text : Search_text2) {
							
							if (Search_item.getText().toUpperCase().contains(text)) {
								if (Search_item.getText().toUpperCase().contains("VEG")
										&& (!Search_item.getText().toUpperCase().contains("VEGAN"))
										&& (Ingredients_name.toUpperCase().contains("EGG"))
										&& (!Ingredients_name.toUpperCase().contains("EGGPLANT"))
										&& (!Search_item.getText().toUpperCase().contains("VEGGIE"))
										&& (!Ingredients_name.toUpperCase().contains("EGGLESS"))
										&& (!Search_item.getText().toUpperCase().contains("EGGLESS")))
									Food_Category = "EGGETARIAN";
								else if (Search_item.getText().toUpperCase().contains("VEGAN") ||
								        (URL.contains("VEGAN")))
								         
									Food_Category = "VEGAN";
								else		
									Food_Category = text;
							}

						}
						if (Ingredients_name.toUpperCase().contains("EGG")
								&& (!Ingredients_name.toUpperCase().contains("EGGPLANT"))
								&& (!Search_item.getText().toUpperCase().contains("VEGGIE"))
								&& (!Ingredients_name.toUpperCase().contains("EGGLESS"))
								&& (!Search_item.getText().toUpperCase().contains("EGGLESS")))
							Food_Category = "EGGETARIAN";
						
					

						// Getting Targeted Morbid Condition
						String Eatable = "";

						boolean f1 = false, f2 = false, f3 = false, f4 = false;
						Eliminators obj = new Eliminators();
						if (!(obj.Diab_Receipe_Noteatable(Ingredients_name, elimatorclass))) {
							Eatable = "Diabetes";
							f1 = true;

						}

						if (!(obj.HyperTen_Receipe_Noteatable(Ingredients_name, elimatorclass))) {
							Eatable = Eatable + "\n" + "Hypertension";
							f2 = true;
						}

						if (!(obj.HypoTyrod_Receipe_Noteatable(Ingredients_name, elimatorclass))) {
							Eatable = Eatable + "\n" + "HypoThyroidism";
							f3 = true;
						}

						if (!(obj.PCOS_Receipe_Noteatable(Ingredients_name, elimatorclass))) {
							Eatable = Eatable + "\n" + "PCOS";
							f4 = true;
						}

						// Allergy Information

						String Allergy_Information = "";
						String Search_text1[] = elimatorclass.allergy.split(",");

						for (String text : Search_text1) {
							if (Ingredients_name.toUpperCase().contains(text))
								Allergy_Information = Allergy_Information + "\n" + text;

						}

						if (Ingredients_name.toUpperCase().contains("EGGLESS")) {

						} else if (Ingredients_name.toUpperCase().contains("EGGPLANT")) {

						} else if (Ingredients_name.toUpperCase().contains("EGG")) {
							Allergy_Information = Allergy_Information + "\n" + "EGG";
						}
						
						

						// Checking if Add ingredients are present
						String IsDiabGoodIndrdientpresent = "No";
						String IsHyperTenGoodIndrdientpresent = "No";
						String IsHypoTyrodGoodIndrdientpresent = "No";
						String IsPCOSGoodIndrdientpresent = "No";

						if (obj.Diab_Receipe_good_ingrdents(Ingredients_name, elimatorclass)) {
							IsDiabGoodIndrdientpresent = "Yes";

						}
						if (obj.HyperTen_Receipe_good_ingrdents(Ingredients_name, elimatorclass)) {
							IsHyperTenGoodIndrdientpresent = "Yes";
						}

						if (obj.HypoTyrod_Receipe_good_ingrdents(Ingredients_name, elimatorclass)) {
							IsHypoTyrodGoodIndrdientpresent = "Yes";
						}

						if (obj.PCOS_Receipe_good_ingrdents(Ingredients_name, elimatorclass)) {
							IsPCOSGoodIndrdientpresent = "Yes";
						}

						if ((f1) || (f2) || (f3) || (f4)) {
							if (RecipeID != null && !RecipeID.trim().isEmpty()) {
								WritetoExcel(row, xlutil, RecipeID, Recipe_Name, URL, Recipe_Category, Food_Category,
										Eatable, Allergy_Information, Ingredients, Preparation_Time, Cooking_Time,
										method, Nutrition, IsDiabGoodIndrdientpresent, IsHyperTenGoodIndrdientpresent,
										IsHypoTyrodGoodIndrdientpresent, IsPCOSGoodIndrdientpresent);
								row++;
							}
						}

					}

					driver.quit();
				} catch (Exception e) {
					if (e.toString().contains(("Connection reset"))) {
						System.out.println("Excption occured" + e.toString());
						continue;
					}
				}
			}
		}

	}

	static void WritetoExcel(int row, XLUtility xlutil, String ReceipeID, String ReceipeName, String url,
			String Recipe_Category, String Food_Category, String Eatable, String Allergy_Information,
			String ingredients, String Preparation_Time, String Cooking_Time, String prepmethod, String Nutrition,
			String IsDiabGoodIndrdientpresent, String IsHyperTenGoodIndrdientpresent,
			String IsHypoTyrodGoodIndrdientpresent, String IsPCOSGoodIndrdientpresent) {

		try {
			xlutil.setCellData("Sheet1", row, 0, ReceipeID);
			xlutil.setCellData("Sheet1", row, 1, ReceipeName);
			xlutil.setCellData("Sheet1", row, 2, url);
			xlutil.setCellData("Sheet1", row, 3, Recipe_Category);
			xlutil.setCellData("Sheet1", row, 4, Food_Category);
			xlutil.setCellData("Sheet1", row, 5, Eatable);
			xlutil.setCellData("Sheet1", row, 6, Allergy_Information);
			xlutil.setCellData("Sheet1", row, 7, ingredients);
			xlutil.setCellData("Sheet1", row, 8, Preparation_Time);
			xlutil.setCellData("Sheet1", row, 9, Cooking_Time);
			xlutil.setCellData("Sheet1", row, 10, prepmethod);
			xlutil.setCellData("Sheet1", row, 11, Nutrition);
			xlutil.setCellData("Sheet1", row, 12, IsDiabGoodIndrdientpresent);
			xlutil.setCellData("Sheet1", row, 13, IsHyperTenGoodIndrdientpresent);
			xlutil.setCellData("Sheet1", row, 14, IsHypoTyrodGoodIndrdientpresent);
			xlutil.setCellData("Sheet1", row, 15, IsPCOSGoodIndrdientpresent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
