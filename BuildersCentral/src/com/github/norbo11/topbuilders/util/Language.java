package com.github.norbo11.topbuilders.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.norbo11.topbuilders.Constants;

public class Language {
	private Language(Document languageDoc) {
		generateBindings(languageDoc.getChildNodes(), "");
		System.out.println(bindings);
	}
	
	private HashMap<String, String> bindings;
	private static Language currentLanguage;
	
	public static void load(String filename) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    //factory.setValidating(true);
	    //factory.setIgnoringElementContentWhitespace(true);
		
	    try {
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        File file = new File(Constants.LANGUAGES_DIRECTORY + filename);
	        Document doc = builder.parse(file);
	        
	        currentLanguage = new Language(doc);
	        //User.getCurrentUser().getSettings().setLanguage(Util.removeFileExtension(file));
	    } catch (ParserConfigurationException | SAXException | IOException e) {
	    	Log.error("Error loading language file " + filename, e);
	    }
	}
	
	
	
	/*
	<settings>
	  <password>
	    <current>Current password</current>
	    <new>New password</new>
	    <confirm>Confirm new password</confirm>
	  </password>
	  
	  <email>
	    <current>Current e-mail</current>
	    <new>New e-mail</new>
	    <confirm>Confirm new e-mail</confirm>
	  </email>
	  
	  <general>
	    <choose>Choose language</choose>
	  </general>
	</settings>
	*/
	
	public static Language getCurrentLanguage() {
		return currentLanguage;
	}



	public static void setCurrentLanguage(Language currentLanguage) {
		Language.currentLanguage = currentLanguage;
	}



	public void setBindings(HashMap<String, String> bindings) {
		this.bindings = bindings;
	}



	private void generateBindings(NodeList currentNodes, String currentKey) {
		for (int i = 0; i < currentNodes.getLength(); i++) {
			Node child = currentNodes.item(i);
			
			if (child.getChildNodes().getLength() != 0) {
				generateBindings(child.getChildNodes(), currentKey + child.getNodeName()  + "_");
			} else bindings.put(currentKey + child.getNodeName(), child.getTextContent());
		}
	}

	public HashMap<String, String> getBindings() {
		return bindings;
	}
}
