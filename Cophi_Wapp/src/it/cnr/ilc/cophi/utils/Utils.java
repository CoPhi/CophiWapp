package it.cnr.ilc.cophi.utils;

import it.cnr.ilc.cophi.model.Link;
import it.cnr.ilc.cophi.model.Reference;
import it.cnr.ilc.cophi.model.comment.Comment;
import it.cnr.ilc.cophi.model.text.PericopeText;
import it.cnr.ilc.cophi.model.text.RefPericopeText;
import it.cnr.ilc.cophi.model.text.RefTokenText;
import it.cnr.ilc.cophi.model.text.TokenText;
import it.cnr.ilc.cophi.model.view.SelectedTextBoundaries;
import it.cnr.ilc.cophi.model.view.TokenViewEntity;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class Utils {

	public static CharSequence fromFile(String filename) throws IOException {
		FileInputStream input = new FileInputStream(filename);
		FileChannel channel = input.getChannel();

		// Create a read-only CharBuffer on the file
		ByteBuffer bbuf = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int)channel.size());
		CharBuffer cbuf = Charset.forName("UTF8").newDecoder().decode(bbuf);
		input.close();

		return cbuf;
	}


	public static HashMap<String, List<Comment>> commentsGroupByLink (HashMap<String, Comment> commentsHM) {
		HashMap<String,List<Comment>> hmloc = new HashMap<String, List<Comment>>();
		List<Comment>  list = null;
		for (Comment com : commentsHM.values()) {
			String linkId = com.getLinkId();
			if (hmloc.containsKey(linkId)){
				list = hmloc.get(linkId);
			} else {
				list = new ArrayList<Comment>();
			}
			list.add(com);
			hmloc.put(linkId, list);
		}
		return hmloc;
	}



	public static String getDate () {

		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();

		return dateFormat.format(date);

	}


	public static String createFilename(Comment comment) {

		return comment.getCommentId();
	}


	public static String substring(SelectedTextBoundaries bound, String text) {
		String ret = "";
		if (bound.getStart() < bound.getEnd()){
			ret = text.substring(bound.getStart(), bound.getEnd());
		}
		return ret;
	}

	public static void addFatalMessageToContext(String summary, String messageId) {
		addMessageToContext(summary, messageId, FacesMessage.SEVERITY_FATAL);

	}
	public static void addErrorMessageToContext(String summary, String messageId) {
		addMessageToContext(summary, messageId, FacesMessage.SEVERITY_ERROR);

	}
	public static void addWarnMessageToContext(String summary, String messageId) {
		addMessageToContext(summary, messageId, FacesMessage.SEVERITY_WARN);

	}
	public static void addInfoMessageToContext(String summary, String messageId) {
		addMessageToContext(summary, messageId, FacesMessage.SEVERITY_INFO);

	}
	private static void addMessageToContext(String summary, String messageId, Severity severity) {

		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage();
		message.setSeverity(severity);
		message.setSummary(summary);
		context.addMessage(messageId, message);

	}

	public static String extractTextFromSpan (String text) {

		return extractTextFromTag("span", text);
	}

	private static String extractTextFromTag (String tag, String text) {
		//<span class="pericope c" style="background-color: lightgreen">kjhdkjashdkajds</span>
		String value = null;
		Pattern p = Pattern.compile("<\\*"+ tag + ".+?>(.+?)<\\s*" + tag + "\\*/>");
		Matcher m = p.matcher(text);

		if (m.find()) {
			value = m.group(0);
		}

		return value;

	}

	public static String getLangfromLangId(int langId) {
		String lang = null;
		switch (langId) {
		case 0:
			lang = "greek";
			break;
		case 1:
			lang = "arabic";
			break;

		default:
			break;
		}
		return  lang;
	}

	public static HashMap<String, String> createToken2PericopeHM (HashMap<String, PericopeText> pericopes) {

		HashMap<String, String> token2pericope = new HashMap<String, String>();
		if (null != pericopes) {
			Iterator<Entry<String, PericopeText>> it = pericopes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, PericopeText> pairs = (Map.Entry)it.next();
				//System.out.println(pairs.getKey() + " = " + pairs.getValue());
				List<Reference> lortt = pairs.getValue().getValue();
				for (Reference reference : lortt) {
					token2pericope.put(reference.getRef(), pairs.getKey());
				}
			}
		}

		return token2pericope;
	}

	public static HashMap<String, String> createPericope2LinkHM (HashMap<String, Link<RefPericopeText>> links) {

		HashMap<String, String> pericope2link = new HashMap<String, String>();
		if (null != links) {
			Iterator<Entry<String, Link<RefPericopeText>>> it = links.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Link<RefPericopeText>> pairs = (Map.Entry)it.next();
				//System.out.println(pairs.getKey() + " = " + pairs.getValue());
				List<RefPericopeText> lortt = pairs.getValue().getValue();
				for (Reference reference : lortt) {
					pericope2link.put(reference.getRef(), pairs.getKey());
				}
			}
		}

		return pericope2link;
	}


	public static List<TokenViewEntity> createTokenViewList (HashMap<String, TokenText> tokens, HashMap<String, PericopeText> pericopes, 
			HashMap<String,Link<RefPericopeText>> links, 
			HashMap<String, String> token2pericope, HashMap<String, String> pericope2link, int langId, CharSequence text) {

		List<TokenViewEntity> ltv = new ArrayList<TokenViewEntity>();
		String pericopeId = null;
		String linkId = null;
		String precPericopeId = null;
		List<TokenText> tokensList = new ArrayList<TokenText>(tokens.values());
		int i = 0;
		TokenViewEntity prec = null;
		Collections.sort(tokensList,CophiSort.TOKENTEXT_FROM_ORDER);

		for (TokenText token: tokensList) {
			TokenViewEntity tv = new TokenViewEntity();
			tv.setId(token.getId());
			tv.setText(token.getText(text));
			tv.setFrom(token.getFrom());
			tv.setTo(token.getTo());
			tv.setColor(i);
			
			if (null != prec) {
				prec.setSucc(tv);
			}

			tv.setPrec(prec);

			if (null != (pericopeId = token2pericope.get(token.getId()))){
				tv.setPericopeInfo(pericopes.get(pericopeId).getInfo());
				if (!pericopeId.equals(precPericopeId)){
					i++;
					precPericopeId = pericopeId;
				}
				tv.setPericopeId(pericopeId);
				tv.setColor(i);
				
				if (pericopeId.contains(".")) {
					tv.setHisPage(Integer.parseInt(pericopeId.substring(0, pericopeId.indexOf(".")))); //se arabo
				} else {
					tv.setHisPage(Integer.parseInt(pericopeId)); //se greco
				}
				if (null !=(linkId = pericope2link.get(pericopeId))){
					tv.setLinkId(linkId);
				}
			} else {
				tv.setColor(-1);
				tv.setPericopeId("-1");
				tv.setPericopeInfo("NONE");
				tv.setLinkId("-1");
			}
			prec = tv;
			ltv.add(tv);
		}
//		Collections.sort(ltv,CophiSort.TOKENVIEW_FROM_ORDER);
		
		return ltv;
	}

	public static Document createPericopeDom (HashMap<String, TokenText> tokens, HashMap<String,Link<RefPericopeText>> links, HashMap<String, String> token2pericope, HashMap<String, String> pericope2link, int langId, CharSequence text) {

		Document rootDom = new Document();
		Element rootElement = new Element("span").setAttribute("class", "document  " + Utils.getLangfromLangId(langId));
		rootDom.setRootElement(rootElement);
		String pericopeId = null;
		String linkId = null;
		TokenText tokenPrec = null;
		StringBuilder tokenClass = null;
		StringBuilder tokenRel = null;
		String precPericopeId = null;
		Element pericopeElement = new Element("span");
		rootElement.addContent(pericopeElement);

		for (TokenText token: tokens.values()) {
			tokenClass = new StringBuilder();
			tokenRel = new StringBuilder();
			String tokenText = token.getText(text);
			tokenClass.append("token");
			Element tokenElement = new Element("span");
			tokenElement.setAttribute("id", token.getId());
			tokenElement.setText(tokenText);
			if (null != (pericopeId = token2pericope.get(token.getId()))) {
				tokenClass.append(" pericope");
				tokenRel.append(pericopeId);

				if (null != tokenPrec) {
					precPericopeId = token2pericope.get(tokenPrec.getId());
				}
				if (!pericopeId.equals(precPericopeId)) {//cambio pericope
					pericopeElement.setAttribute("id", (precPericopeId==null)?"-1":precPericopeId);
					pericopeElement.setAttribute("rel", links.get(pericope2link.get(precPericopeId)).getValue().get((langId + 1)%2).getRef()); //pericope relativa nell'altra lingua
					rootElement.addContent(pericopeElement); 
					pericopeElement = new Element("span");
				}

				if (null != (linkId = pericope2link.get(tokenText))) {
					String idPericopeRef = links.get(linkId).getValue().get(langId).getRef();
					tokenRel.append(" " + idPericopeRef);

				}

			} else {
				tokenClass.append("nopericope");
			}
			if (null != tokenPrec){
				int noOfspace = token.getFrom() - tokenPrec.getTo();
				if (noOfspace > 0) {
					Element preElement = new Element("pre");
					preElement.setAttribute("style", "display:inline");
					preElement.setText(String.format("%"+noOfspace+"s", " "));
				}
			}
			tokenElement.setAttribute("rel", tokenRel.toString());
			tokenElement.setAttribute("class", tokenClass.toString());
			pericopeElement.addContent(tokenElement);
		}

		return rootDom;
	}



	public static Document createPericopeDomOld (HashMap<String,Link<Reference>> links, int langId, CharSequence text) {

		Document rootDom = new Document();
		Element rootElement = new Element("span").setAttribute("class", "document  " + Utils.getLangfromLangId(langId));
		rootDom.setRootElement(rootElement);

		TokenText tokenPrec = null;

		for (Link<Reference> link : links.values()) {
			PericopeText pericope = (PericopeText) ((RefPericopeText)link.getValue().get(langId)).getPericope();
			List<Reference> lor = pericope.getValue();
			Element pericopeElement = new Element("span").setAttribute("class", "pericope");
			pericopeElement.setAttribute("id", pericope.getId());
			pericopeElement.setAttribute("rel",  ((RefPericopeText)link.getValue().get((langId + 1)%2)).getPericope().getId()); //pericope relativa nell'altra lingua
			rootElement.addContent(pericopeElement);
			for (Reference reference : lor) {
				TokenText token = ((RefTokenText)reference).getTok();
				String tokenText = token.getText(text);
				Element tokenElement = new Element("span").setAttribute("class", "token");
				tokenElement.setAttribute("id", token.getId());
				tokenElement.setText(tokenText);
				tokenElement.setAttribute("rel", pericope.getId());
				if (null != tokenPrec){
					int noOfspace = token.getFrom() - tokenPrec.getTo();
					if (noOfspace > 0) {
						Element preElement = new Element("pre");
						preElement.setAttribute("style", "display:inline");
						preElement.setText(String.format("%"+noOfspace+"s", " "));
						pericopeElement.addContent(preElement);
					}
				}
				pericopeElement.addContent(tokenElement);
				tokenPrec = token;
			}
		}
		return rootDom;

	}

	public static String JDOMtoString (Document jdom) {

		XMLOutputter xmlout = new XMLOutputter(Format.getRawFormat());
		return xmlout.outputString(jdom.getRootElement());

	}


	public static float emToFloat (String em) {
		float ret = 0;
		if (null != em) {
			ret = Float.parseFloat(em.substring(0, em.indexOf("em")));
		}
		return ret;
	}


}
