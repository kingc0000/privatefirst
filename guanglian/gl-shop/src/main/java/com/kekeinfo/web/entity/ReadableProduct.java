package com.kekeinfo.web.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 数据池返回商品对象
 * @author sam
 *
 */
public class ReadableProduct extends Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String enname;
	private String code;
	private String cas;
	private String manufacturer;//id of the manufacturer
	private String category;//id of the category
	private String merchant;//id of the merchant
	private String manufacturerName; //name of the manufacturer
	private String categoryName; // name of the category
	private String merchantName; // name of the merchant
	private String image;
	private String imageSrc;
	private List<String> tags;//keywords ?
	private String website;//数据抓取自哪个网站
	private String originalUrl; //数据初始地址
	private String summary; //商品简单描述
	private String description;
	private String keywords;
	private String storecondDescription; //存储条件
	private InnerMerchant innerMerchant; //内部类，商家信息
	private InnerPrice innerPrice;  //内部类，商品价格
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getOriginalUrl() {
		return originalUrl;
	}
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getStorecondDescription() {
		return storecondDescription;
	}
	public void setStorecondDescription(String storecondDescription) {
		this.storecondDescription = storecondDescription;
	}
	
	public InnerMerchant getInnerMerchant() {
		return innerMerchant;
	}
	public void setInnerMerchant(InnerMerchant innerMerchant) {
		this.innerMerchant = innerMerchant;
	}

	public InnerPrice getInnerPrice() {
		return innerPrice;
	}
	public void setInnerPrice(InnerPrice innerPrice) {
		this.innerPrice = innerPrice;
	}
	public String getCas() {
		return cas;
	}
	public void setCas(String cas) {
		this.cas = cas;
	}


	public class InnerMerchant {
		private Long id;
		private String storename;
		private String code;
		private String storeaddress;
		private String owner; //联系人
		private String storephone;
		private String storemobile;
		private String storefax;
		private String storeurl;
		private String storememo;
		private String email;
		
		public InnerMerchant() {
			super();
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getStorename() {
			return storename;
		}
		public void setStorename(String storename) {
			this.storename = storename;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getStoreaddress() {
			return storeaddress;
		}
		public void setStoreaddress(String storeaddress) {
			this.storeaddress = storeaddress;
		}
		public String getOwner() {
			return owner;
		}
		public void setOwner(String owner) {
			this.owner = owner;
		}
		public String getStorephone() {
			return storephone;
		}
		public void setStorephone(String storephone) {
			this.storephone = storephone;
		}
		public String getStoremobile() {
			return storemobile;
		}
		public void setStoremobile(String storemobile) {
			this.storemobile = storemobile;
		}
		public String getStorefax() {
			return storefax;
		}
		public void setStorefax(String storefax) {
			this.storefax = storefax;
		}
		public String getStoreurl() {
			return storeurl;
		}
		public void setStoreurl(String storeurl) {
			this.storeurl = storeurl;
		}
		public String getStorememo() {
			return storememo;
		}
		public void setStorememo(String storememo) {
			this.storememo = storememo;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
	}
	
	public class InnerPrice {
		private String id;
		private String code;
		private BigDecimal productPriceAmount = new BigDecimal(0);
		private boolean defaultPrice = false;
		private BigDecimal productPriceSpecialAmount;
		private Integer productPriceStockAmount;
		private String productPricePeriod;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public BigDecimal getProductPriceAmount() {
			return productPriceAmount;
		}
		public void setProductPriceAmount(BigDecimal productPriceAmount) {
			this.productPriceAmount = productPriceAmount;
		}
		public boolean isDefaultPrice() {
			return defaultPrice;
		}
		public void setDefaultPrice(boolean defaultPrice) {
			this.defaultPrice = defaultPrice;
		}
		public BigDecimal getProductPriceSpecialAmount() {
			return productPriceSpecialAmount;
		}
		public void setProductPriceSpecialAmount(BigDecimal productPriceSpecialAmount) {
			this.productPriceSpecialAmount = productPriceSpecialAmount;
		}
		public Integer getProductPriceStockAmount() {
			return productPriceStockAmount;
		}
		public void setProductPriceStockAmount(Integer productPriceStockAmount) {
			this.productPriceStockAmount = productPriceStockAmount;
		}
		public String getProductPricePeriod() {
			return productPricePeriod;
		}
		public void setProductPricePeriod(String productPricePeriod) {
			this.productPricePeriod = productPricePeriod;
		}
	}
}
