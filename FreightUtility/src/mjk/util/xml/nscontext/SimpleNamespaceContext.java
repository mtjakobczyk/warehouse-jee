package mjk.util.xml.nscontext;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

public class SimpleNamespaceContext implements NamespaceContext {
	private final Map<String, String> prefixNamespaceMap = new HashMap<String, String>();
	
	public SimpleNamespaceContext(final Map<String, String> prefMap) {
        prefixNamespaceMap.putAll(prefMap);       
    }

	@Override
    public String getNamespaceURI(String prefix) {
        return prefixNamespaceMap.get(prefix);
    }

    @Override
    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		throw new UnsupportedOperationException();
	}

}
