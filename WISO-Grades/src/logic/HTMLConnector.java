/**
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
package logic;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import model.Area;
import model.CompleteExamList;
import model.Exam;
import model.Grade;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author Cihan Öcal
 * <i> 25.02.2013 </i>
 */
public class HTMLConnector implements Runnable{

	private static final String URLSTRING = "https://wisoapp.uni-koeln.de/";	
	
	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private HttpPost httpPost;

	private String userName = null, password = null;
	private String sessionID;
	private WISOGrades app;
	private Errorhandler errorhandler;
	
	
	/**
	 * Constructor
	 * 
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public HTMLConnector(WISOGrades app, String username, String password) {
		this.app = app;
		errorhandler = app.getErrorhandler();
		
		this.userName = username;
		this.password = password;
		 
	}
	
	@Override
	public void run() {
		loadExamList();
		
	}

	/**
	 * Method to get whole scraped examlist in appropriate form. 
	 * The new object will be saved at WISOGrades.exams.
	 * You can get it via wisoGrades.getExams();
	 * @return
	 * @author Cihan Öcal
	 * <i> 25.02.2013 </i>
	 */
	public void loadExamList(){
		
		CompleteExamList exams = null;
		
		if (login()){
			exams = convertGradeTables(getGradeTables());
			app.setExamList(exams);
		
		} else{
			app.setExamList(null);
		}

		app.getMainWindow().setFinished(true);
	}
	
	/**
	 * login to site
	 * @author Cihan Öcal
	 * @return <code>true</code>, if login-information is correct <br />
	 * <code>false</code>, else.
	 */
	private boolean login() {
		
		sessionID = "invalid Session ID";
		
		final String 	PARAM1KEY = "benutzername", 
						PARAM2KEY = "passwort",
						PARAM3KEY = "destination", PARAM3VALUE = "login";
	
		httpPost = new HttpPost(URLSTRING);
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair(PARAM1KEY, userName));
		nvps.add(new BasicNameValuePair(PARAM2KEY, password));
		nvps.add(new BasicNameValuePair(PARAM3KEY, PARAM3VALUE));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
	
			HttpEntity entity = response.getEntity();
		    
		    /* Save Inputstream into String */
		    String content = EntityUtils.toString(entity);
		    
		    /* Scrape/Parse String for Session ID */
		    Document doc = Jsoup.parse(content);
		    Element phpSessID = doc.select("input[name=PHPSESSID]").first();
		    
		    if (phpSessID == null){
		    	errorhandler.showError("Fehler: Konnte SessionID nicht auslesen! " +
		    			"Bitte geben Sie ihre Daten erneut ein...");
		    	app.getMainWindow().resetFields();
		    	return false;
		    }
		    
		    sessionID = phpSessID.val();
		   		
		    EntityUtils.consume(entity);
		    
		    return true;
		    
		} catch (IOException e) {
			errorhandler.showError("Verbindung zum WISO-Server kann nicht hergestellt werden!");
			return false;
			
		} finally {
		    httpPost.releaseConnection();
		}
	}

	/**
	 * Returns grade-tables sorted by area.
	 * @return Array, with headers in position 0 and <br />
	 * tables in position 1
	 * @author Cihan Öcal
	 */
	private Elements[] getGradeTables() {
	
		final String 	PARAM1KEY = "destination", PARAM1VALUE = "notenliste",
						PARAM2KEY = "PHPSESSID";
		
		httpPost = new HttpPost(URLSTRING);
		
		Elements tables = null;
		Elements headings = null;
		
		Elements[] result = new Elements[2];
		try{
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair(PARAM1KEY, PARAM1VALUE));
			nvps.add(new BasicNameValuePair(PARAM2KEY, sessionID));
			
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			HttpResponse response = httpclient.execute(httpPost);
	
			HttpEntity entity = response.getEntity();
		    String content = EntityUtils.toString(entity);
		    
		    Document doc = Jsoup.parse(content);
		    
		    // ADD TABLES
		    tables = doc.select("table");
		    //remove first two tables since they are not relevant
		    tables.remove(0);
		    tables.remove(0);
	
		    // ADD HEADINGS
		    headings = doc.select("h3");
	
		    /* DE-COMMENT FOLLOWING LINES TO REMOVE STUDIUM INTEGRALE */
//		    // Remove "Studium Integrale"
//		    
//		    for (int i = 0; i < tables.size(); i++){
//		    	if (tables.get(i).text().contains("Studium Integrale")){
//		    		tables.remove(i);
//		    		headings.remove(i);
//		    	}
//		    }
		    /* DECOMMENT UNTIL HERE */
	
		    
		    result = new Elements[]{headings, tables};
		    
		    EntityUtils.consume(entity);
			
		} catch(UnsupportedEncodingException uee){
			uee.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			httpPost.releaseConnection();
		}
	
		return result;
		
	}

	/**
	 * Each Table represents an area. In each Area, there are different Exams
	 * with the student's individually achieved Grades.
	 * 
	 * @param gradeTables
	 *            , requires that:<br />
	 *            - List at position 0 <i>(i.e. Elements[0])</i> contains headers <br />
	 *            - List at position 1 <i>(i.e. Elements[1])</i> contains tables.
	 * 
	 * @author Cihan Öcal
	 */
	private CompleteExamList convertGradeTables(Elements[] gradeTables) {
		
		// First Headers == Areas
		LinkedList<Area> areas = new LinkedList<Area>();
		for (Element element : gradeTables[0]){
			areas.add(new Area(element.text()));
		}
		
		// Now tables(-entries) == exams
		CompleteExamList exams = new CompleteExamList();
		
		for (int i = 0; i < gradeTables[1].size(); i++){
			Element singleGradeTable = gradeTables[1].get(i);
			Elements tableDataList = singleGradeTable.select("td");
			
			for (int j = 0; j < tableDataList.size() ; j += 4){
				
				
				/*
				 * Set examID
				 */
				int examID = -1;
				try{
					examID = Integer.parseInt(tableDataList.get(j).text());
				} catch (NumberFormatException e){
					e.printStackTrace();
					errorhandler.showError
						("Es gibt ein Problem beim Auslesen der VeranstaltungsId");
					continue;
				}
				Exam exam = new Exam(areas.get(i), examID);
				
				/*
				 * Set examName
				 */
				Element examName = tableDataList.get(j + 1);
				exam.setName(examName.text());
				
				
				/*
				 * Set Creditpoints
				 */
				Element creditPoints = tableDataList.get(j + 2);
				String cpString = creditPoints.text();
				byte cp = -99;
				
				try {
					cp = Byte.parseByte(cpString);
				} catch (NumberFormatException e){
					e.printStackTrace();
					errorhandler.showError("Es gibt ein Problem beim Auslesen der Creditpoints");
					continue;
				}
				
				exam.setCreditpoints(cp);
				
				/*
				 * Set archievedGrade
				 */
				Element gradeOnSite = tableDataList.get(j + 3);
				String gradeString = gradeOnSite.text();
				Grade grade = Grade.NaN;
				try{
					float parsedGrade = Float.parseFloat(gradeString.replace(',', '.'));
					grade = Grade.getGradeByNumericValue(parsedGrade);
					
				} catch (NumberFormatException e){
					System.out.println(exam.getName() + ": '" + 
							gradeString + "' ist keine numerische Note.");
					grade = Grade.NaN;
				}
				
				exam.setRating(grade);
				
				/*
				 * Finished Updating exam-object here
				 */
				
				
				exams.add(exam);
				
			}
			
		}
		
		Collections.sort(exams);
		
//		for (Area ar : areas){
//			System.out.println(ar.getName());
//		}
//		
//		for (Exam ex : exams){
//			Collections.sort(exams);
//			System.out.println(ex.getArea().getName() + 
//					" ---------- Veranstaltung: " + ex.getId() + 
//					ex.getName() + ".");
//		
//		}
		
		return exams;
		

		
	}

	
	
}
