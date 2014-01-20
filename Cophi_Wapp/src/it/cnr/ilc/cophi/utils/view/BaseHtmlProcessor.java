package it.cnr.ilc.cophi.utils.view;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class BaseHtmlProcessor implements NodeVisitor, BaseHtmlProcessorInterface {

	private Node lastNode = null;
	private Map<Node, Node> nextMap = new IdentityHashMap<Node, Node>();
	private Map<Node, Node> prevMap = new IdentityHashMap<Node, Node>();
	private Map<String, Set<Element>> tagMap = new HashMap<String, Set<Element>>();
	private Map<String, Set<Element>> classMap = new HashMap<String, Set<Element>>();
	private final String[] validTags;
	private final String[] validAttrs;
	private final Set<String> validClasses;
	private final String[] validClassesStringArray;
	private EscapeMode escapeMode = EscapeMode.xhtml;
	private final static Set<Element> emptySet = new HashSet<Element>();
	protected Document root;
	private boolean _debug = false;

	final protected GrammarInterface preReplaceGrammar = new Grammar();
	final protected GrammarInterface htmlGrammar = new Grammar();

	@SuppressWarnings("unchecked")
	private void init() {
		for (String tagName : validTags) {
			tagMap.put(tagName, new HashSet<Element>());
		}
		preReplaceGrammar.willReplace("&quot;", "\"");
		preReplaceGrammar.willReplace("&apos;", "'");
		preReplaceGrammar.willReplace("&nbsp;", " ");
		preReplaceGrammar.willReplace("\u00A0", " "); // unicode for
														// non-breaking space
														// (&nbsp;)

		htmlGrammar.willReplace("\\s+", " ");
		// Remove ' and " translation into their respective entities &apos and
		// &quot; when rendering the html output.
		// This strict-xhtml mapping results in a performance loss and poor
		// compatibility, among other side-effects, and this is why we're
		// getting rid of it.
		// FIXME WARNING: The changed mapping is global and should not be done
		// this way! Unfortunately it's impossible to change the EscapeMode enum
		// differently, since it's built-in. Some ideas?
		escapeMode.getMap().remove('\'');
		escapeMode.getMap().remove('"');
	}

	private void debug(String s) {
		if (_debug)
			System.out.println("*** " + getClass().getSimpleName() + "(): " + s);
	}

	public BaseHtmlProcessor(String[] args, String[] attrs) {
		validTags = args;
		validAttrs = attrs;
		validClasses = null;
		validClassesStringArray = null;
		init();
	}

	public BaseHtmlProcessor(String[] args, String[] attrs, String[] classes) {
		validTags = args;
		validAttrs = attrs;
		validClassesStringArray = classes;
		if (classes != null) {
			validClasses = new HashSet<String>();
			for (String a : classes)
				validClasses.add(a);
		} else
			validClasses = null;
		init();
	}

	public void start(String html) {
		debug("START: original = " + html);
		classMap.clear();
		for (Set<Element> el : tagMap.values())
			el.clear();
		root = clean(html);
		nextMap.clear();
		prevMap.clear();
		new NodeTraversor(this).traverse(root);
		debug("START: cleaned = " + root.body().html());
	}

	public void finish() {
		aggregate();
		debug("FINISH: aggregate = " + root.body().html());
		normalize(root);
		debug("FINISH: normalize = " + root.body().html());
		nextMap.clear();
		prevMap.clear();
	}

	@Override
	public Element process(String html) {
		start(html);
		finish();
		return root.body();
	}

	private void aggregate() {
		Set<Element> originalEl = tagMap.get("span");
		if (originalEl == null) {
			System.out.println("HtmlProcessor.aggregate(): WARNING: element tagname span was never processed!");
			return;
		}
		List<Element> el = new ArrayList<Element>(originalEl);
		for (int i = 0; i < el.size(); i++) {
			Element span = el.get(i);
			String klass = span.attr("class");
			String[] classes = klass.split(" ");
			// System.out.println("SONO TUA ZIA E HO UN ELEMENT SPAN CON CLASS: "+klass);
			if (validClasses != null && !isOneOf(classes)) {
				unwrap(span);
				// System.out.println("---HO UNWRAPPATO LA MERDA: " + span);
			} else {
				Node nextNode = span.nextSibling();
				if (!(nextNode == null || !(nextNode instanceof Element))) {
					Element nextElement = (Element) nextNode;
					String rel = span.attr("rel");
					if (nextElement.tagName().equals("span") && nextElement.attr("class").equals(klass) && nextElement.attr("rel").equals(rel)) {
						Node prev = prevMap.get(nextElement);
						Node next = nextMap.get(nextElement);
						// System.out.println("---HO TROVATO DUE SPAN UGUALI: "
						// + nextElement.text() + " E " + span.text());
						while (nextElement.childNodes().size() > 0) {
							Node child = nextElement.childNode(0);
							child.remove();
							span.appendChild(child);
							// System.out.println("HO APPESO: "+child);

						}
						remove(nextElement);
						originalEl.remove(nextElement);
						// System.out.println("HO FATTO MERGE E HO: "+span.text());
						// System.out.println("IN REALTA' NELLA LISTA DI SPAN HO: "+originalEl);
						// System.out.println("IL PREV DI NEXTELEMENT: "+prev);
						// System.out.println("IL NEXT DI NEXTELEMENT: "+next);
						// System.out.println("SINCERAMENTE NELLA MAPPA NEXT AVREI: "+nextMap);
						// System.out.println("CIONONDIMENO NELLA MAPPA PREV MI RISULTA: "+prevMap);
						// System.out.println("E IN EFFETTI IL MIO NEXTELEMENT E': "+nextElement);

						if (prev instanceof TextNode && next instanceof TextNode) {
							TextNode prevTextNode = (TextNode) prev;
							TextNode nextTextNode = (TextNode) next;
							next = nextMap.get(next);
							prevTextNode.text(prevTextNode.text() + nextTextNode.text());
							nextTextNode.remove();
						}
						nextMap.put(prev, next);
						prevMap.put(next, prev);

						i--;
					}
				}
			}
		}
	}

	@Override
	public Document clean(String html) {
		html = preReplaceGrammar.process(html);
		debug("CLEAN: prereplace = " + html);
		Whitelist wl = Whitelist.simpleText();
		wl.addTags(validTags);
		wl.addAttributes("span", validAttrs);
		Document dirty = Jsoup.parseBodyFragment(html);
		Cleaner cleaner = new Cleaner(wl);
		Document doc = cleaner.clean(dirty);
		OutputSettings s = doc.outputSettings();
		s.charset(Charset.forName("UTF-8"));
		s.prettyPrint(false);
		s.escapeMode(escapeMode);
		return doc;
	}

	private boolean isInTags(String tag) {
		return tagMap.containsKey(tag);
	}

	private boolean isOneOf(String[] classes) {
		for (String klass : classes)
			if (validClasses.contains(klass))
				return true;
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void head(Node node, int depth) {
		if (node instanceof TextNode || (node instanceof Element && isInTags(((Element) node).tagName()))) {
			if (lastNode != null) {
				nextMap.put(lastNode, node);
			}
			prevMap.put(node, lastNode);
			lastNode = node;
			if (node instanceof Element) {
				Element e = (Element) node;
				Set<Element> tl = tagMap.get(e.tagName());
				tl.add(e);
				for (String name : e.classNames()) {
					Set<Element> cl = classMap.get(name);
					if (cl == null) {
						cl = new HashSet<Element>();
						classMap.put(name, cl);
					}
					cl.add(e);
				}
			}
		}
	}

	@Override
	public void tail(Node node, int depth) {
	}

	private Node next(Node current) {
		return nextMap.get(current);
	}

	private void normalize(Node doc) {
		if (doc instanceof TextNode) {
			TextNode textNode = (TextNode) doc;
			// System.out.println("*** SONO QUELLA CHE DOVREBBE NORMALIZZARE, MA '"
			// + (int) textNode.text().charAt(0) + "' IS BLANK? " +
			// textNode.isBlank());
			if (textNode.isBlank()) {
				textNode.remove();
			} else {
				// String text = textNode.text().replace("\\s+", " ").trim();
				// String text =
				// textValidator("allora  ,   come  : va      ?    Io , sto bene  . per ora  !  ");
				String text = applyReplacements(textNode.text());
				// System.out.println("TESTO VALIDATO: '" + text+"'");
				Element parent = (Element) textNode.parent();
				if (parent != null) {
					Node nextNode = next(textNode);
					// System.out.println("§§§§§§§§§§§§§§§§§§ NextNode:" +
					// nextNode + " del nodo corrente: " + textNode +
					// " validTags: " + validTags);
					if (nextNode != null) {
						if ((!(nextNode instanceof TextNode) || !startsWithPunctuation(nextNode)) && !endsWithPunctuation(textNode)) {
							// System.out.println("SONO NEXTNODE: " + nextNode);
							nextNode.before(new TextNode(" ", ""));
						}
					}
				}
				textNode.text(text);
			}
		} else if (doc.childNodes().size() == 0) {
			if (doc instanceof Element) {
				Element e = (Element) doc;
				String tagName = e.tagName();
				if (tagName.equals("br")) {
					Node sibling = next(e);
					// debug("NORMALIZE: removing BR? " + doc);
					if (sibling instanceof Element && ((Element) sibling).tagName().equals("br")) {
						// debug("NORMALIZE: removing double BR: " + doc);
						remove(e);
					}
				} else if (!tagName.equals("body"))
					remove(e);
			} else {
				debug("NORMALIZE: should be dead code, but removing generic node " + doc);
				doc.remove(); // should never be executed
			}
		} else {
			List<Node> tuaZia = new ArrayList<Node>(doc.childNodes());
			for (Node node : tuaZia)
				normalize(node);
		}
	}

	private boolean startsWithPunctuation(Node n) {
		// System.out.println("*** startsWithPunctuation *** MATCH PRIMA:" +
		// ((TextNode)n).text());
		String s = ((TextNode) n).text().replaceAll("^\\s+", "");
		// System.out.println("*** startsWithPunctuation *** MATCH DOPO :" + s);
		return s.length() > 0 && ".,;:?!)]}".indexOf(s.substring(0, 1)) >= 0;
	}

	private boolean endsWithPunctuation(Node n) {
		// System.out.println("*** startsWithPunctuation *** MATCH PRIMA:" +
		// ((TextNode)n).text());
		String s = ((TextNode) n).text().replaceAll("\\s+$", "");
		// System.out.println("*** startsWithPunctuation *** MATCH DOPO :" + s);
		int l = s.length();
		return l > 0 && "([{".indexOf(s.substring(l - 1, l)) >= 0;
	}

	@Override
	public String applyReplacements(String text) {
		// System.out.println("    ****    GRAMMATICA DOPO IL BaseHTMLProcessor: "
		// + textGrammar);
		return htmlGrammar.process(text);
	}

	@Override
	public Set<Element> getElementsByTag(String name) {
		return tagMap.get(name);
	}

	@Override
	public Set<Element> getElementsByClass(String name) {
		Set<Element> results = classMap.get(name);
		return results == null ? emptySet : results;
	}

	@Override
	public void retag(String oldTag, String newTag) {
		Set<Element> el = tagMap.get(oldTag);
		Set<Element> nl = tagMap.get(newTag);
		if (el != null && nl != null) {
			for (Element b : el)
				b.tagName(newTag);
			nl.addAll(el);
			el.clear();
		} else
			System.out.println("HtmlProcessor.retag(): WARNING - tagnames " + oldTag + " and/or " + newTag + " were never processed!");
	}

	private void removeElementMappings(Element tag) {
		Set<Element> tl = tagMap.get(tag.tagName());
		if (tl != null)
			tl.remove(tag);
		for (String className : tag.classNames()) {
			Set<Element> cl = classMap.get(className);
			if (cl != null)
				cl.remove(tag);
		}
		Node prev = prevMap.get(tag);
		Node next = nextMap.get(tag);
		nextMap.put(prev, next);
		prevMap.put(next, prev);
	}

	@Override
	public void unwrap(Element tag) {
		tag.unwrap();
		removeElementMappings(tag);
	}

	@Override
	public void remove(Element tag) {
		tag.remove();
		removeElementMappings(tag);
	}

	@Override
	public void removeElementsByClass(String name) {
		Set<Element> s = getElementsByClass(name);
		List<Element> el = new ArrayList<Element>(s);
		while (el.size() > 0)
			remove(el.remove(0));
	}

	@Override
	public String html() {
		return root.body().html();
	}

	@Override
	public String text() {
		return root.body().text();
	}

	@Override
	public String[] getValidTags() {
		return validTags;
	}

	@Override
	public String[] getValidAttrs() {
		return validAttrs;
	}

	@Override
	public String[] getValidClasses() {
		return validClassesStringArray;
	}
}
