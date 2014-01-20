package it.cnr.ilc.cophi.action.controller.content;

import it.cnr.ilc.cophi.model.Factory;
import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.ReferenceSet;
import it.cnr.ilc.cophi.model.handler.EntityTypeHandler;
import it.cnr.ilc.cophi.model.text.PericopeText;
import it.cnr.ilc.cophi.model.text.RefTokenText;
import it.cnr.ilc.cophi.model.text.TokenText;
import it.cnr.ilc.cophi.model.xmlmapping.ElementDocument.Element;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument.Sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;

public class PericopeTextContentBehaviour extends BaseContent implements ContentBehaviour<PericopeText> {

	private HashMap<String, TokenText> tokens = null;

	public PericopeTextContentBehaviour(SequenceDocument sd) {
		super(sd);
	}

	public PericopeTextContentBehaviour() {

	}

	@Override
	public HashMap<String,PericopeText> getValue() {

		Sequence s = getSequenceDocument().getSequence();
		HashMap<String,PericopeText> lop = Factory.getPericopeTextMap(); 


		if (EntityTypeHandler.isPericopes(s)) {
			Sequence[] pericopes = s.getSequenceArray();

			for (Sequence pericope : pericopes) {
				if (EntityTypeHandler.isPericope(pericope)) {
					PericopeText pt = Factory.getPericopeText();
					ArrayList<Reference> lort = Factory.getReferenceList();

					pt.setId(pericope.getId());
					pt.setInfo(pericope.getExtended());
					pt.setType(pericope.getType());

					Element[] refTokens =  pericope.getElementArray();
					for (Element refToken : refTokens) {
						RefTokenText rtt = Factory.getRefTokenTextInstance();
						rtt.setId(refToken.getId());
						rtt.setRef(refToken.getRef());
						rtt.setTok(getTokens().get(refToken.getRef()));
						rtt.setExtended(refToken.getExtended());
						//TODO
						lort.add(rtt);
					}
					pt.setValue(lort);
					lop.put(pt.getId(),pt);
				}
			}
		}
		return lop;
	}

	/**
	 * @return the tokens
	 */
	public HashMap<String, TokenText> getTokens() {
		return tokens;
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setTokens(HashMap<String, TokenText> tokens) {
		this.tokens = tokens;
	}


	@Override
	public Document createJDOM(PericopeText t) {

		return null;
	}

	@Override
	public SequenceDocument createSequence(List<ReferenceSet<Reference>> listOfPericope,
			String work) {
		if (null != listOfPericope) {

			setSequenceDocument(SequenceDocument.Factory.newInstance());

			Sequence pericopesNode = getSequenceDocument().addNewSequence();
			pericopesNode.setClassname("pericopes");
			pericopesNode.setExtended(work);//work = plotino o badaoui
			for (ReferenceSet<Reference> pericopeText : listOfPericope) {
				Sequence seq = pericopesNode.addNewSequence();
				seq.setClassname("pericope");
				seq.setType(pericopeText.getType());
				seq.setExtended(((PericopeText)pericopeText).getInfo());
				seq.setId(pericopeText.getId());
				for (Reference refToken : pericopeText.getValue()) {
					Element e = seq.addNewElement();
					e.setId(refToken.getId());
					e.setRef(refToken.getRef());
					e.setClassname("refToken");
					e.setExtended(refToken.getExtended());
				}

			}

		}
		return getSequenceDocument();
	}

}

