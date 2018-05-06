/**
 * 
 */
package com.kekeinfo.core.utils;

import java.util.Locale;

import com.kekeinfo.core.business.generic.exception.ConversionException;



/**
 * @author Umesh A
 *
 */
public abstract class AbstractDataPopulator<Source,Target> implements DataPopulator<Source, Target>
{

 
   
    private Locale locale;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public Locale getLocale() {
		return locale;
	}
	

	@Override
	public Target populate(Source source) throws ConversionException{
	   return populate(source,createTarget());
	}
	
	protected abstract Target createTarget();
	
}
