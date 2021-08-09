/*
 * Created on 2 May 2012 16:03:10 
 */
package bank.ui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Ingrid Nunes
 * 
 */
public class TextManager {

	public static final Locale LANGUAGE[] = { new Locale("en", "US"),
			new Locale("pt", "BR") };

	public static final int LANGUAGE_ENGLISH = 0;
	public static final int LANGUAGE_PORTUGUESE = 1;

	private String baseName;
	private ResourceBundle bundle;
	private ResourceBundle customBundle;
	private String customName;
	private Locale locale;
	private Log log;

	public TextManager(String baseName) {
		this(baseName, Locale.getDefault());
	}

	public TextManager(String baseName, Integer language) {
		this(baseName, LANGUAGE[language]);
	}

	public TextManager(String baseName, Locale locale) {
		this.log = LogFactory.getLog(this.getClass());
		this.locale = locale;
		setBaseName(baseName);
		setCustomName(null);
	}

	/**
	 * @return the baseName
	 */
	public String getBaseName() {
		return baseName;
	}

	/**
	 * @return the bundle
	 */
	public ResourceBundle getBundle() {
		return bundle;
	}

	/**
	 * @return the customBundle
	 */
	public ResourceBundle getCustomBundle() {
		return customBundle;
	}

	/**
	 * @return the customName
	 */
	public String getCustomName() {
		return customName;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	public String getText(String key) {
		String text = null;
		if (customBundle != null) {
			try {
				text = customBundle.getString(key);
			} catch (MissingResourceException exc) {
				text = null;
			}
		}
		if (text == null) {
			try {
				text = bundle.getString(key);
			} catch (MissingResourceException exc) {
				log.warn(exc);
				text = key;
			}
		}
		return text;
	}

	public String getText(String key, String arg) {
		return getText(key, arg, true);
	}

	public String getText(String key, String arg, boolean prepare) {
		return getText(key, new String[] { arg }, prepare);
	}

	public String getText(String key, String[] args) {
		return getText(key, args, true);
	}

	public String getText(String key, String[] args, boolean prepare) {
		String[] newArgs = args;
		if (prepare) {
			newArgs = new String[args.length];
			for (int i = 0; i < newArgs.length; i++) {
				newArgs[i] = getText(args[i]);
			}
		}
		return new MessageFormat(getText(key)).format(newArgs);
	}

	/**
	 * @param baseName
	 *            the baseName to set
	 */
	public void setBaseName(String baseName) {
		this.baseName = baseName;
		bundle = ResourceBundle.getBundle(baseName, locale);
	}

	/**
	 * @param customName
	 *            the customName to set
	 */
	public void setCustomName(String customName) {
		this.customName = customName;
		if (customName == null) {
			customBundle = null;
		} else {
			customBundle = ResourceBundle.getBundle(customName, locale);
		}
	}

	public void setLanguage(int language) {
		setLocale(LANGUAGE[language]);
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
		bundle = ResourceBundle.getBundle(baseName, locale);
		if (customName != null) {
			customBundle = ResourceBundle.getBundle(customName, locale);
		}
	}

}
