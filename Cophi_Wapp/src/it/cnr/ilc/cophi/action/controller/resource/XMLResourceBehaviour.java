package it.cnr.ilc.cophi.action.controller.resource;

import it.cnr.ilc.cophi.datahandler.XMLHandler;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument;

import java.util.HashMap;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

public class XMLResourceBehaviour implements ResourceBehaviour {


	XMLHandler handler = new XMLHandler();
	
	public XMLResourceBehaviour () {
	}
	
	@Override
	public Resource getResource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setResource() {
		// TODO Auto-generated method stub

	}

	@Override
	public HashMap<String, Resource> resourceLoad(String collectionName) {

		return handler.getResources(collectionName);
	}

	@Override
	public SequenceDocument retrieveContent(Resource resource) {
		// TODO Auto-generated method stub
		SequenceDocument sd = null;
		try {
			sd = (SequenceDocument) handler.loadDocument((XMLResource) resource, SequenceDocument.Factory.newInstance());
			
		} catch (XMLDBException e) {
			//TODO
			System.err.println(e.getMessage());
		} finally {
			if(null == sd) {
				sd = SequenceDocument.Factory.newInstance();
			}
		}
		return sd;
	}

	@Override
	public boolean store(Document document, String dbName, String fileName) {

		XMLOutputter xmlout = new XMLOutputter(Format.getPrettyFormat());
		
		return handler.save(dbName, xmlout.outputString(document), fileName);
	}

	
	@Override
	public boolean remove( String dbName, XMLResource xr) {

		return handler.remove(dbName, xr);
	}


}
