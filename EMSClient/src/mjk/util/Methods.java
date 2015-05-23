package mjk.util;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Methods {
	public static InitialContext prepareJNDIContext(String jmsProviderURL, String username, String password, String contextFactory ) throws NamingException {
		Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.PROVIDER_URL, jmsProviderURL);
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory );		
		return new InitialContext(env);		
	}
}
