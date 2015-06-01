package prog.util;

import java.util.ArrayList;
import java.util.List;

public class SimpleHTMLDocument {
	//General info
	XMLTag title;
	String doctype;
	
	//HTML Head
	XMLTag head = new XMLTag("","head");
	XMLTag style = new XMLTag("","style");
	List<XMLTag> link = new ArrayList<XMLTag>();
	
	//HTML Body
	XMLTag body = new XMLTag("","body");
	XMLTag header = new XMLTag("","header");
	
	public SimpleHTMLDocument(String charset, String title){
		doctype = "<!doctype html>";
		this.title = new XMLTag(title,"title");
		
		XMLTag chset = new XMLTag("","meta");
		chset.addAttr("charset", charset);
		head.addChild(chset);
	}
	
	public void setTitle(String title){
		this.title = new XMLTag(title,"title");
	}
	
	public void addMeta(String name, String content){
		XMLTag meta = new XMLTag("","meta");
		meta.addAttr("name", name);
		meta.addAttr("content", content);
		
		head.addChild(meta);
	}
	
	public void addLink(String rel, String href){
		XMLTag link = new XMLTag("","link");
		link.addAttr("rel", rel);
		link.addAttr("href", href);
		
		this.link.add(link);
	}
	
	public void addHeadline(String s, int type){
		XMLTag h = new XMLTag(s,("h"+type));
		body.addChild(h);
	}
	
	public void addParagraph(String s){
		XMLTag p = new XMLTag(s, "p");
		body.addChild(p);
	}
	
	public String build(){
		XMLTag html = new XMLTag("","html");
		
		//Construct head
		for(XMLTag t : link){
			head.addChild(t);
		}
		head.addChild(style);
		head.addChild(title);
		
		//Construct body
		//BLA
		
		//Construct html
		html.addChild(head);
		html.addChild(body);
		
		String out = doctype + "\n" + html.generate();
		return out;
	}
}
