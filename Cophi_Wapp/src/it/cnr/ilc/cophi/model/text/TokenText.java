/**
 * 
 */
package it.cnr.ilc.cophi.model.text;

import it.cnr.ilc.cophi.model.Token;

/**
 * @author simone
 *
 */
public class TokenText extends Token {
	
	private int from;
	private int to;
	
	/**
	 * @return the from
	 */
	public int getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(int from) {
		this.from = from;
	}
	/**
	 * @return the to
	 */
	public int getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(int to) {
		this.to = to;
	}

	public String getText (CharSequence s) {
		String ret = null;
		if (getTo() > s.length()) {
			System.err.println(" ERR in getText("+ s.subSequence(0, 20) +")" + s.length() +", "+getFrom() + ":" + getTo());
			
		} else {
			ret = (s.subSequence(getFrom(), getTo())).toString(); 
		}
		return ret;
	}
	
}

