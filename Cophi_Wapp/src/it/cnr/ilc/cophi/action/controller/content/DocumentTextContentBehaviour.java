package it.cnr.ilc.cophi.action.controller.content;

import it.cnr.ilc.cophi.model.Factory;
import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.ReferenceSet;
import it.cnr.ilc.cophi.model.handler.EntityTypeHandler;
import it.cnr.ilc.cophi.model.text.TokenText;
import it.cnr.ilc.cophi.model.xmlmapping.ElementDocument.Element;
import it.cnr.ilc.cophi.model.xmlmapping.ParamDocument.Param;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument;
import it.cnr.ilc.cophi.model.xmlmapping.SequenceDocument.Sequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;


public class DocumentTextContentBehaviour extends BaseContent implements ContentBehaviour<TokenText> {

	/*
	 * Gestito dal Session Bean (fa la new di questa classe e passa nel costruttore il puntatore alla sequence document) 
	 */

	public DocumentTextContentBehaviour(SequenceDocument sd) {
		super(sd);
	}

	/**
	 * Mi restituisce tutto il documento come stringa (?)
	 */
	public HashMap<String,TokenText> getValue() {

		Sequence s = getSequenceDocument().getSequence();
		HashMap<String,TokenText> lot = Factory.getTokenTextMap(); 
		/*
		 * controllo che stia analizzando una sequenze di tipo (class) Text
		 * e quindi gli elements siano le foglie.
		 */
		if (EntityTypeHandler.isText(s)) { 
			Element[] ele = s.getElementArray();
			for (Element element : ele) {

				TokenText tok = Factory.getTokenTextInstance();
				tok.setId(element.getId());
				Param[] params = element.getParamArray();
				for (Param param : params) {
					if ("from".equals(param.getName())) {
						tok.setFrom(Integer.parseInt(param.getValue()));
					} else if ("to".equals(param.getName())) {
						tok.setTo(Integer.parseInt(param.getValue()));
					} else if ("refAnalysis".equals(param.getName())) {
						String analysis = param.getValue();
						tok.setRefAnalysis(analysis);
						tok.setAnalysis(null);
						//TODO fare una hash di analisi AnalysisHM (forse meglio caricamento lazy? e quindi non caricare subito le analisi)
						//   AnalysisHM.get(analysis);
					} else {
						//System.err.println("Unknown parameter? " + param.getName() + " : " + param.getValue());
					}
				}
				lot.put(tok.getId(), tok);
			}
		}
 		return lot;
	}

	@Override
	public Document createJDOM(TokenText t) {
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
