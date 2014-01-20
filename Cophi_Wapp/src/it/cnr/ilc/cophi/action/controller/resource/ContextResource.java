package it.cnr.ilc.cophi.action.controller.resource;

import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument;

import java.util.HashMap;

import org.jdom2.Document;
import org.xmldb.api.base.Resource;
import org.xmldb.api.modules.XMLResource;

/**
 * 
 * 
 */
public class ContextResource {

	
	private HashMap<String, Resource> resources = null;

	private ResourceBehaviour resourceType = null;
	

	public ContextResource() {
		this.resources = new HashMap<String, Resource>();
	}

	/**
	 * @return the resources
	 */
	public HashMap<String, Resource> getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(HashMap<String, Resource> resources) {
		this.resources = resources;
	}

	public ResourceBehaviour getResourceBehaviourType() {
		return resourceType;
	}

	public void setResourceBehaviourType(ResourceBehaviour type) {
		this.resourceType = type;
	}

	/**
	 * 
	 * @param dbName
	 */
	public void loadResources (String dbName) {
	
		setResources(resourceType.resourceLoad(dbName));
	}
	
	public SequenceDocument retrieveContent (String resourceName) {
	
		return resourceType.retrieveContent( getResources().get(resourceName));
	}

	public HashMap<String, SequenceDocument> retriveAllCollectionContent () {
		
		HashMap<String, SequenceDocument> allCollectionContent = new HashMap<String, SequenceDocument>();
		for (String resourceName: getResources().keySet()) {
			allCollectionContent.put(resourceName, retrieveContent(resourceName));
		}
		return allCollectionContent;
	}

	public boolean store (Document document, String dbName, String fileName) {
		
		return resourceType.store(document, dbName, fileName);
		
	}

	public boolean remove (String dbName, String xmlFileName) {
		
		boolean ret = false;
		if (ret = resourceType.remove(dbName, (XMLResource) getResources().get(xmlFileName))){
			getResources().remove(xmlFileName);
		}
		
		return ret;
	}

}
