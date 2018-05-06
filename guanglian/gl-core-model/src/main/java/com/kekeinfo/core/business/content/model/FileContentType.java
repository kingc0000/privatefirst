/**
 * 
 */
package com.kekeinfo.core.business.content.model;

/**
 * Enum defining type of static content.
 * Currently following type of static content can be store and managed within 
 * Shopizer CMS system
 * <pre>
 * 1. Static content like JS, CSS file etc
 * 2. Digital Data (audio,video)
 * </pre>
 * 
 * StaticContentType will be used to distinguish between Digital data and other type of static data
 * stored with in the system.
 * 
 * @author Umesh Awasthi
 * @since 1.2
 *
 */
public enum FileContentType
{
  STATIC_FILE, IMAGE, LOGO, PRODUCT, PRODUCTLG, PROPERTY, MANUFACTURER, PRODUCT_DIGITAL,SUBSTRATE,MONPROBLEM, 
  DAILY, DREPORT,IWELL_DIGITAL,DEWELL_DIGITAL,OWELL_DIGITAL,PWELL_DIGITAL,
  MONITOR_DAILY,GUARD_DAILY,GUARD_PRO,MONITOR_PRO,MONITOR_SUB,
  PREVIEW ,EQUIPMENT,QCODE,SIGN//项目评价
}
