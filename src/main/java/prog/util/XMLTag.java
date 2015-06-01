package prog.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class XMLTag {
	//XML tag names. Example: <b><i>
	private String[] tagNames;
	private String content;
	
	private Map<String,String> attr;
	private List<XMLTag> child;
	
	public XMLTag(String content, String tag, String... tagName){
		tagNames = new String[tagName.length+1];
		for(int i=0; i<tagName.length; i++){
			tagNames[i] = tagName[i];
		}
		tagNames[tagNames.length-1] = tag;
		
		this.content = content;
		attr = new HashMap<String,String>();
		child = new ArrayList<XMLTag>();
	}
	
	//Set content
	public void setContent(String content){
		this.content = content;
	}
	
	//Get content
	public String getContent(){
		return content;
	}
	
	//Get tag names
	public String[] getTagNames(){
		return tagNames;
	}
	
	//Get attribute list
	public Map<String,String> getAttr(){
		return attr;
	}
	
	//Get list of child tags
	public List<XMLTag> getChild(){
		return child;
	}
	
	//Add XML attributes
	public boolean addAttr(String name, String value){
		//Duplicate or multiple tag names
		if(attr.containsKey(name) || tagNames.length > 1){
			return false;
		}
		attr.put(name, value);
		return true;
	}
	
	//Add XML attributes
	public boolean addAttr(Map<String,String> attrs){
		if(tagNames.length > 1)
			return false;
		
		Iterator<Entry<String, String>> it = attrs.entrySet().iterator();
		
		while(it.hasNext()){
			String key = it.next().getKey();
			if(attr.containsKey(key)){
				return false;
			}
		}
		
		attr.putAll(attrs);
		return true;
	}
	
	//Update XML attributes
	public boolean updateAttr(String name, String value){
		if(!attr.containsKey(name)){
			return false;
		}
		attr.replace(name, value);
		return true;
	}
	
	//Delete an XML attribute
	public boolean deleteAttr(String name, String value){
		if(!attr.containsKey(name)){
			return false;
		}
		attr.remove(name);
		return true;
	}
	
	//Add children (they can contain children themselves)
	public void addChild(XMLTag tag){
		child.add(tag);
	}
	
	//Check if this tag has children
	public boolean hasChild(){
		if(child.isEmpty()){
			return false;
		}
		return true;
	}
	
	//Return valid XML entity reference
	public String toEntityRef(String s){
		String out = s;
		
		for(XMLEntityReference x : XMLEntityReference.values()){
			if(s.equals(x.ch)){
				return x.ref;
			}
		}
		
		return out;
	}
	
	//Return generated XML tree as string
	public String generate(){
		String out = "";
		String buffer = "";
		
		//No child tags
		if(!hasChild()){
			for(String t : tagNames){
				out += "<" + t + buildAttrs() + ">";
				buffer = "</" + t + ">" + buffer;
			}
			out += content + buffer + "\n";
			return out;
		}
		
		String childBuffer = "";
		for(String t : tagNames){
			out += "<" + t + buildAttrs() + ">";
			buffer = "</" + t + ">" + buffer;
		}
		
		for(XMLTag c : child){
			childBuffer += "\t" + c.generate();
		}

		
		//Assemble
		String deploy = out + "\n" + childBuffer + buffer + "\n";
		
		return deploy;
	}
	
	private String buildAttrs(){
		String out = "";
		if(attr.isEmpty())
			return out;
		
		for(Map.Entry<String, String> e : attr.entrySet()){
			out += " " + e.getKey() + "=\"" + e.getValue() + "\"";
		}
		
		return out;
	}
	
//	//Check for name conflicts
//	private boolean checkForNameConflicts(XMLTag tag){
//		List<String> cache = new ArrayList<String>();
//		if(!tag.hasChild()){
//			return false;
//		}
//		
//		for(XMLTag t : tag.)
//		
//		return true;
//	}
	
	//Alias for XMLTag.render
	public String toString(){
		return generate();
	}
	
//	//More complicated then anticipated :P
//	public boolean equals(Object o){
//		if(o.getClass().isInstance(this)){
//		}
//		
//		return false;
//	}
}
