package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.tcl.dias.productcatelog.entity.base.BaseEntity;


/**
 * 
 * @author Manojkumar R
 *
 */
@Entity
@Table(name = "product_offering")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "is_addon_service_ind")
	private String isAddonService;

	@Column(name = "is_template_ind")
	private String isTemplate;

	@Column(name = "long_desc")
	private String description;

	@Column(name = "nm")
	private String name;

	@Column(name = "parent_id")
	private Integer parentId;

	//@Column(name = "url")
	@Column(name = "image_nm")
	private String url;
	
	
	// bi-directional many-to-one association to ProductFamily
	@ManyToOne
	@JoinColumn(name = "product_family_id")
	private ProductCatalog productFamily;

	// bi-directional many-to-one association to ProductAttributeValue
	@OneToMany(mappedBy = "product")
	private List<ProductAttributeValue> productAttributeValues;

	// bi-directional many-to-one association to ProductBundleAssoc
	@OneToMany(mappedBy = "product")
	private List<ProductBundleAssoc> productBundleAssocs;

	// bi-directional many-to-one association to ProductComponentAssoc
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(mappedBy = "product")
	private List<ProductComponentAssoc> productComponentAssocs;

	// bi-directional many-to-one association to ProductComponentDiscount
	/*@OneToMany(mappedBy = "product")
	private List<ProductComponentDiscount> productComponentDiscounts;
*/
	// bi-directional many-to-one association to ProductLocationAssoc
	@OneToMany(mappedBy = "product")
	private List<ProductLocationAssoc> productLocationAssocs;

	// bi-directional many-to-one association to ProductServiceCategory
	@OneToMany(mappedBy = "product")
	private List<ProductServiceCategory> productServiceCategories;
	
	// bi-directional many-to-one association to CustomConfigBusinessRules
	@OneToMany(mappedBy = "product")
	private List<CustomConfigBusinessRules> customConfigBusinessRulesList;


	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getIsAddonService() {
		return isAddonService;
	}

	public void setIsAddonService(String isAddonService) {
		this.isAddonService = isAddonService;
	}

	public String getIsTemplate() {
		return isTemplate;
	}

	public void setIsTemplate(String isTemplate) {
		this.isTemplate = isTemplate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ProductCatalog getProductFamily() {
		return this.productFamily;
	}

	public void setProductFamily(ProductCatalog productFamily) {
		this.productFamily = productFamily;
	}

	public List<ProductAttributeValue> getProductAttributeValues() {
		return this.productAttributeValues;
	}

	public void setProductAttributeValues(List<ProductAttributeValue> productAttributeValues) {
		this.productAttributeValues = productAttributeValues;
	}

	public ProductAttributeValue addProductAttributeValue(ProductAttributeValue productAttributeValue) {
		getProductAttributeValues().add(productAttributeValue);
		productAttributeValue.setProduct(this);

		return productAttributeValue;
	}

	public ProductAttributeValue removeProductAttributeValue(ProductAttributeValue productAttributeValue) {
		getProductAttributeValues().remove(productAttributeValue);
		productAttributeValue.setProduct(null);

		return productAttributeValue;
	}

	public List<ProductBundleAssoc> getProductBundleAssocs() {
		return this.productBundleAssocs;
	}

	public void setProductBundleAssocs(List<ProductBundleAssoc> productBundleAssocs) {
		this.productBundleAssocs = productBundleAssocs;
	}

	public ProductBundleAssoc addProductBundleAssoc(ProductBundleAssoc productBundleAssoc) {
		getProductBundleAssocs().add(productBundleAssoc);
		productBundleAssoc.setProduct(this);

		return productBundleAssoc;
	}

	public ProductBundleAssoc removeProductBundleAssoc(ProductBundleAssoc productBundleAssoc) {
		getProductBundleAssocs().remove(productBundleAssoc);
		productBundleAssoc.setProduct(null);

		return productBundleAssoc;
	}

	public List<ProductComponentAssoc> getProductComponentAssocs() {
		return this.productComponentAssocs;
	}

	public void setProductComponentAssocs(List<ProductComponentAssoc> productComponentAssocs) {
		this.productComponentAssocs = productComponentAssocs;
	}

	public ProductComponentAssoc addProductComponentAssoc(ProductComponentAssoc productComponentAssoc) {
		getProductComponentAssocs().add(productComponentAssoc);
		productComponentAssoc.setProduct(this);

		return productComponentAssoc;
	}

	public ProductComponentAssoc removeProductComponentAssoc(ProductComponentAssoc productComponentAssoc) {
		getProductComponentAssocs().remove(productComponentAssoc);
		productComponentAssoc.setProduct(null);

		return productComponentAssoc;
	}
/*
	public List<ProductComponentDiscount> getProductComponentDiscounts() {
		return this.productComponentDiscounts;
	}

	public void setProductComponentDiscounts(List<ProductComponentDiscount> productComponentDiscounts) {
		this.productComponentDiscounts = productComponentDiscounts;
	}

	public ProductComponentDiscount addProductComponentDiscount(ProductComponentDiscount productComponentDiscount) {
		getProductComponentDiscounts().add(productComponentDiscount);
		productComponentDiscount.setProduct(this);

		return productComponentDiscount;
	}

	public ProductComponentDiscount removeProductComponentDiscount(ProductComponentDiscount productComponentDiscount) {
		getProductComponentDiscounts().remove(productComponentDiscount);
		productComponentDiscount.setProduct(null);

		return productComponentDiscount;
	}
*/
	public List<ProductLocationAssoc> getProductLocationAssocs() {
		return this.productLocationAssocs;
	}

	public void setProductLocationAssocs(List<ProductLocationAssoc> productLocationAssocs) {
		this.productLocationAssocs = productLocationAssocs;
	}

	public ProductLocationAssoc addProductLocationAssoc(ProductLocationAssoc productLocationAssoc) {
		getProductLocationAssocs().add(productLocationAssoc);
		productLocationAssoc.setProduct(this);

		return productLocationAssoc;
	}

	public ProductLocationAssoc removeProductLocationAssoc(ProductLocationAssoc productLocationAssoc) {
		getProductLocationAssocs().remove(productLocationAssoc);
		productLocationAssoc.setProduct(null);

		return productLocationAssoc;
	}

	public List<ProductServiceCategory> getProductServiceCategories() {
		return this.productServiceCategories;
	}

	public void setProductServiceCategories(List<ProductServiceCategory> productServiceCategories) {
		this.productServiceCategories = productServiceCategories;
	}

	public ProductServiceCategory addProductServiceCategory(ProductServiceCategory productServiceCategory) {
		getProductServiceCategories().add(productServiceCategory);
		productServiceCategory.setProduct(this);

		return productServiceCategory;
	}

	public ProductServiceCategory removeProductServiceCategory(ProductServiceCategory productServiceCategory) {
		getProductServiceCategories().remove(productServiceCategory);
		productServiceCategory.setProduct(null);

		return productServiceCategory;
	}
	public List<CustomConfigBusinessRules> getCustomConfigBusinessRulesList() {
		return customConfigBusinessRulesList;
	}

	public void setCustomConfigBusinessRulesList(List<CustomConfigBusinessRules> customConfigBusinessRulesList) {
		this.customConfigBusinessRulesList = customConfigBusinessRulesList;
	}

	
}