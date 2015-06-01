package prog.util;

public enum XMLEntityReference {
	LESS_THAN("<", "&lt;"),
	GREATER_THAN(">", "&gt;"),
	AMPERSAND("&", "&amp;"),
	APOSTROPHE("'", "&apos"),
	QUOTATION_MARK("\"", "&quot;");
	
	String ch;
	String ref;
	
	private XMLEntityReference(String ch, String ref){
		this.ch = ch;
		this.ref = ref;
	}
}
