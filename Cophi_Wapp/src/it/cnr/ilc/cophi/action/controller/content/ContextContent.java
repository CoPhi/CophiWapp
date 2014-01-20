package it.cnr.ilc.cophi.action.controller.content;

import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.ReferenceSet;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ContextContent<T> {

	private HashMap<String,T> value;

	private ContentBehaviour contentType = null;

	private Document document = null;
	/**
	 * @return the value
	 */
	public HashMap<String,T> getValue() {
		value = contentType.getValue();
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(HashMap<String,T> value) {
		this.value = value;
	}

	/**
	 * @return the contentType
	 */
	public ContentBehaviour getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(ContentBehaviour contentType) {
		this.contentType = contentType;
	}


	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(Document document) {
		this.document = document;
	}

	public void contentToJDOM (T t) {

		setDocument(this.contentType.createJDOM(t));

	}

	public void contentToSequence(List listOfReference, String work) { 

		SequenceDocument sd = this.contentType.createSequence(listOfReference, work);
		SAXBuilder sb = new SAXBuilder();
		Document doc;
		try {
			doc = sb.build(new StringReader(sd.toString()));
			setDocument(doc);		

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


}
