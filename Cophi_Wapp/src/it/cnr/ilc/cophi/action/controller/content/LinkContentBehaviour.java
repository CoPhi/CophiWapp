package it.cnr.ilc.cophi.action.controller.content;

import it.cnr.ilc.cophi.model.Factory;
import it.cnr.ilc.cophi.model.Link;
import it.cnr.ilc.cophi.model.RefPericope;
import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.ReferenceSet;
import it.cnr.ilc.cophi.model.handler.EntityTypeHandler;
import it.cnr.ilc.cophi.model.text.PericopeText;
import it.cnr.ilc.cophi.model.text.RefPericopeText;
import it.cnr.ilc.cophi.model.xmlmapping.ElementDocument.Element;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument.Sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;

public class LinkContentBehaviour extends BaseContent implements
		ContentBehaviour<Link<RefPericope>> {

	private HashMap<String, PericopeText> pericopes = null;
	
	public LinkContentBehaviour(SequenceDocument sd) {
		super(sd);
	}

	@Override
	public HashMap<String,Link<RefPericope>> getValue() {
		
		Sequence s = getSequenceDocument().getSequence();
		HashMap<String,Link<RefPericope>> lol = Factory.getLinkMap(); 
		
		if (EntityTypeHandler.isLinks(s)) {
			Sequence[] links = s.getSequenceArray();
			
			for (Sequence slnk : links) {
				
				if (EntityTypeHandler.isLink(slnk)) {
					
					Link<RefPericope> link = Factory.getLink();
					ArrayList<RefPericope> lorp = Factory.getRefPericopeList();
					
					link.setId(slnk.getId());
					link.setType(slnk.getExtended());
					
					Element[] refPericopes =  slnk.getElementArray();
					for (Element refPericope : refPericopes) {
						RefPericopeText rp = Factory.getRefPericopeTextInstance(refPericope.getExtended());
						rp.setId(slnk.getId());
						rp.setRef(refPericope.getRef());
						rp.setPericope(pericopes.get(refPericope.getRef()));
						rp.setExtended(refPericope.getExtended());
						lorp.add(rp);
					}
					link.setValue(lorp);
					lol.put(link.getId(),link);
				}
			}
		}
		return lol;
	}

	/**
	 * @return the pericopes
	 */
	public HashMap<String, PericopeText> getPericopes() {
		return pericopes;
	}

	/**
	 * @param pericopes the pericopes to set
	 */
	public void setPericopes(HashMap<String, PericopeText> pericopes) {
		this.pericopes = pericopes;
	}

	@Override
	public Document createJDOM(Link<RefPericope> t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SequenceDocument createSequence(List<ReferenceSet<Reference>> t,
			String work) {
		// TODO Auto-generated method stub
		return null;
	}



}
