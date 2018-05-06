/**
 * 
 */
package com.kekeinfo.core.utils;

import com.kekeinfo.core.business.generic.exception.ConversionException;


/**
 * @author Umesh A
 *
 */
public interface DataPopulator<Source,Target>
{


    public Target populate(Source source,Target target) throws ConversionException;
    public Target populate(Source source) throws ConversionException;

   
}
