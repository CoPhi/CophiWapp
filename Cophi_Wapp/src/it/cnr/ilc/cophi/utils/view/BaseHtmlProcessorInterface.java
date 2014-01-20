package it.cnr.ilc.cophi.utils.view;

import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public interface BaseHtmlProcessorInterface {
	public Document clean(String html);

	public Element process(String html);

	public void start(String html);

	public void finish();

	public Set<Element> getElementsByTag(String name);

	public Set<Element> getElementsByClass(String name);

	public void removeElementsByClass(String name);

	public void retag(String oldTag, String newTag);

	public void unwrap(Element tag);

	public void remove(Element tag);

	public String applyReplacements(String text);

	public String html();

	public String text();

	public String[] getValidTags();

	public String[] getValidAttrs();

	public String[] getValidClasses();
}
