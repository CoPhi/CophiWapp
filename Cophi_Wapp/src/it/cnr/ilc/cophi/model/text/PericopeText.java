package it.cnr.ilc.cophi.model.text;


import java.util.List;

import it.cnr.ilc.cophi.model.Pericope;
import it.cnr.ilc.cophi.model.Reference;

public class PericopeText extends Pericope<Reference>{

	public String getText(CharSequence cb) {

		List<Reference> list = getValue();
		
		int start = 0;
		int end = 0;

		if (null != list && list.size() > 0){
			RefTokenText firstToken = (RefTokenText) list.get(0);
			RefTokenText lastToken = (RefTokenText) list.get(list.size() - 1);
			if (firstToken.getTok() != null) {
				start = firstToken.getTok().getFrom();
			} else {
				System.err.println("Error in get pericope text firstToken.getId() " + firstToken.getId());
			}
			if (lastToken.getTok() != null) {
				end = lastToken.getTok().getTo();
			} else {
				System.err.println("Error in get pericope text lastToken.getId(): " + lastToken.getId());
			}
		}
		return cb.subSequence(start, end).toString();
	}


}
