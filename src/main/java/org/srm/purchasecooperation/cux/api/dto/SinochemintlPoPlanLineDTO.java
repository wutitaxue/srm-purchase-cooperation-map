package org.srm.purchasecooperation.cux.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.choerodon.mybatis.domain.AuditDomain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hzero.boot.platform.lov.annotation.LovValue;
import org.hzero.core.base.BaseConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 采购计划行表
 *
 * @author tianjing.gui@hand-china.com 2021-09-06 09:59:20
 */
@ApiModel("采购计划行表")
@VersionAudit
@ModifyAudit
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SinochemintlPoPlanLineDTO extends AuditDomain {

    @ApiModelProperty("表ID，主键，供其他表做外键")
    @Id
    private Long poPlanLineId;
    @ApiModelProperty(value = "采购方租户", required = true)
    private Long tenantId;
    @ApiModelProperty(value = "头表id", required = true)
    private Long poPlanHeaderId;
    @ApiModelProperty(value = "状态(SCUX.SINOCHEMINTL.PO_PLAN_NUMBER)", required = true)
    @LovValue(lovCode = "SCUX.SINOCHEMINTL.PO_PLAN_NUMBER", meaningField = "statusName")
    private String status;
    @ApiModelProperty(value = "已拼单省区数量", required = true)
    private Long spellDocProvince;
    @ApiModelProperty(value = "需求日期", required = true)
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATETIME)
    @JsonFormat(pattern = BaseConstants.Pattern.DATETIME)
    private Date needDate;
    @ApiModelProperty(value = "计划共享省区(SPFM.USER_AUTH.COMPANY)", required = true)
    private String planSharedProvince;
    @ApiModelProperty(value = "计划共享省区名称", required = true)
    @Transient
    private List<Map<String, Object>> planSharedProvinceName;
    @ApiModelProperty(value = "省公司/项目ID(SPFM.USER_AUTH.COMPANY)", required = true)
    private Long provinceCompanyId;
    @ApiModelProperty(value = "省公司/项目", required = true)
    private String provinceCompany;
    @ApiModelProperty(value = "产品名称", required = true)
    private String productName;
    @ApiModelProperty(value = "包装规格", required = true)
    private String specification;
    @ApiModelProperty(value = "初步沟通供应商Id(SPFM.USER_AUTH.SUPPLIER)", required = true)
    private Long initialSupplierId;
    @ApiModelProperty(value = "初步沟通供应商", required = true)
    private String initialSupplier;
    @ApiModelProperty(value = "最终供应商", required = true)
    private String endSupplier;
    @ApiModelProperty(value = "最终价格", required = true)
    private BigDecimal endPrice;
    @ApiModelProperty(value = "出厂价", required = true)
    private BigDecimal factoryPrice;
    @ApiModelProperty(value = "发货地址", required = true)
    private String deliveryAddress;
    @ApiModelProperty(value = "物料id(SSRC.PRICE_LIB_ITEM)")
    private Long materialId;
    @ApiModelProperty(value = "物料编码")
    private String materialCoding;
    @ApiModelProperty(value = "物料分类")
    private String materialClass;
    @ApiModelProperty(value = "需求数量", required = true)
    private BigDecimal requiredQuantity;
    @ApiModelProperty(value = "单位(SPRM.UOM)", required = true)
    private Long uomId;
    @ApiModelProperty(value = "单位编码")
    private String uomCode;
    @ApiModelProperty(value = "单位名称")
    private String uomName;
    @ApiModelProperty(value = "单位名称", required = true)
    private String unitName;
    @ApiModelProperty(value = "最终行金额", required = true)
    private BigDecimal totalPurchaseFinalPrice;
    @ApiModelProperty(value = "行金额", required = true)
    private BigDecimal totalPurchasePrice;

    public String getUomName() {
        return uomName;
    }

    public void setUomName(String uomName) {
        this.uomName = uomName;
    }

    @ApiModelProperty(value = "币种(SPFM.TENANT_CURRENCY)", required = true)
    private String currencyId;
    @ApiModelProperty(value = "币种名称", required = true)
    private String currencyName;
    @ApiModelProperty(value = "运费供应商")
    private String freightSupplier;
    @ApiModelProperty(value = "运费价格")
    private BigDecimal freightPrice;
    @ApiModelProperty(value = "价格条款")
    private String priceTerms;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "税种ID(SPRM.TAX)")
    private Long taxId;
    @ApiModelProperty(value = "税种")
    private String taxType;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "申请人")
    private Long applicantId;
    @ApiModelProperty(value = "申请人")
    private String applicant;
    @ApiModelProperty(value = "附件id")
    private String appendixId;

    @ApiModelProperty(value = "标题", required = true)
    private String title;
    @ApiModelProperty(value = "采购计划单号", required = true)
    private String poPlanNumber;
    @ApiModelProperty(value = "计划类型", required = true)
    private String planType;
    @ApiModelProperty(value = "拼单截至时间", required = true)
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATETIME)
    @JsonFormat(pattern = BaseConstants.Pattern.DATETIME)
    private Date deadline;
    @ApiModelProperty(value = "公司ID(SPFM.USER_AUTH.COMPANY)")
    private Long companyId;
    @ApiModelProperty(value = "公司", required = true)
    private String companyName;
    @ApiModelProperty(value = "业务实体ID(SPFM.USER_AUTH.OU)")
    private Long businessId;
    @ApiModelProperty(value = "业务实体", required = true)
    private String businessName;
    @ApiModelProperty(value = "采购组织ID(HPFM.PURCHASE_ORGANIZATION_M)")
    private Long purchaseOrgId;
    @ApiModelProperty(value = "采购组织", required = true)
    private String purchaseOrgName;
    @ApiModelProperty(value = "采购员ID(SPUC.PURCHASE_AGENT)")
    private Long purchaseId;
    @ApiModelProperty(value = "采购员", required = true)
    private String purchaseName;
    @ApiModelProperty(value = "部门ID(SPUC.SD_PH_UNIT)")
    private Long departmentId;
    @ApiModelProperty(value = "所属部门", required = true)
    private String departmentName;
    @ApiModelProperty(value = "创建人id")
    private Long createId;
    @ApiModelProperty(value = "创建人", required = true)
    private String createName;
    @ApiModelProperty(value = "创建日期")
    @DateTimeFormat(pattern = BaseConstants.Pattern.DATETIME)
    @JsonFormat(pattern = BaseConstants.Pattern.DATETIME)
    private Date establishDate;
    @ApiModelProperty(value = "申请总额", required = true)
    private BigDecimal applicationSum;
    @ApiModelProperty(value = "申请最终总额", required = true)
    private BigDecimal applicationFinalSum;

//
// 非数据库字段
// ------------------------------------------------------------------------------

    @ApiModelProperty(value = "头状态")
    private String headerStatus;

    @ApiModelProperty(value = "序号")
    private Integer serialNum;

    @ApiModelProperty(value = "展示行号")
    private Integer displayLineNum;

    @Transient
    @ApiModelProperty(value = "状态名")
    private String statusName;

    @ApiModelProperty(value = "共享省区对应id")
    private Long sharedProvinceId;

    @Transient
    @ApiModelProperty(value = "行表数据")
    private Set<Long> poPlanLineIds;
//
// getter/setter
// ------------------------------------------------------------------------------

    public List<Map<String, Object>> getPlanSharedProvinceName() {
        return planSharedProvinceName;
    }

    public void setPlanSharedProvinceName(List<Map<String, Object>> planSharedProvinceName) {
        this.planSharedProvinceName = planSharedProvinceName;
    }

    public String getHeaderStatus() {
        return headerStatus;
    }

    public void setHeaderStatus(String headerStatus) {
        this.headerStatus = headerStatus;
    }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public Set<Long> getPoPlanLineIds() {
        return poPlanLineIds;
    }

    public void setPoPlanLineIds(Set<Long> poPlanLineIds) {
        this.poPlanLineIds = poPlanLineIds;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Long getPurchaseOrgId() {
        return purchaseOrgId;
    }

    public void setPurchaseOrgId(Long purchaseOrgId) {
        this.purchaseOrgId = purchaseOrgId;
    }

    public String getPurchaseOrgName() {
        return purchaseOrgName;
    }

    public void setPurchaseOrgName(String purchaseOrgName) {
        this.purchaseOrgName = purchaseOrgName;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    public BigDecimal getApplicationSum() {
        return applicationSum;
    }

    public void setApplicationSum(BigDecimal applicationSum) {
        this.applicationSum = applicationSum;
    }

    public BigDecimal getApplicationFinalSum() {
        return applicationFinalSum;
    }

    public void setApplicationFinalSum(BigDecimal applicationFinalSum) {
        this.applicationFinalSum = applicationFinalSum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoPlanNumber() {
        return poPlanNumber;
    }

    public void setPoPlanNumber(String poPlanNumber) {
        this.poPlanNumber = poPlanNumber;
    }

    public Integer getDisplayLineNum() {
        return displayLineNum;
    }

    public void setDisplayLineNum(Integer displayLineNum) {
        this.displayLineNum = displayLineNum;
    }

    public Long getSharedProvinceId() {
        return sharedProvinceId;
    }

    public void setSharedProvinceId(Long sharedProvinceId) {
        this.sharedProvinceId = sharedProvinceId;
    }

    public String getEndSupplier() {
        return endSupplier;
    }

    public BigDecimal getTotalPurchaseFinalPrice() {
        return totalPurchaseFinalPrice;
    }

    public void setTotalPurchaseFinalPrice(BigDecimal totalPurchaseFinalPrice) {
        this.totalPurchaseFinalPrice = totalPurchaseFinalPrice;
    }

    public void setEndSupplier(String endSupplier) {
        this.endSupplier = endSupplier;
    }

    public BigDecimal getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(BigDecimal endPrice) {
        this.endPrice = endPrice;
    }

    public Long getInitialSupplierId() {
        return initialSupplierId;
    }

    public void setInitialSupplierId(Long initialSupplierId) {
        this.initialSupplierId = initialSupplierId;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public Long getUomId() {
        return uomId;
    }

    public void setUomId(Long uomId) {
        this.uomId = uomId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getProvinceCompanyId() {
        return provinceCompanyId;
    }

    public void setProvinceCompanyId(Long provinceCompanyId) {
        this.provinceCompanyId = provinceCompanyId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(Integer serialNum) {
        this.serialNum = serialNum;
    }

    public String getStatus() {
        return status;
    }

    public SinochemintlPoPlanLineDTO setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * @return 表ID，主键，供其他表做外键
     */
    public Long getPoPlanLineId() {
        return poPlanLineId;
    }

    public SinochemintlPoPlanLineDTO setPoPlanLineId(Long poPlanLineId) {
        this.poPlanLineId = poPlanLineId;
        return this;
    }

    /**
     * @return 采购方租户
     */
    public Long getTenantId() {
        return tenantId;
    }

    public SinochemintlPoPlanLineDTO setTenantId(Long tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    /**
     * @return 头表id
     */
    public Long getPoPlanHeaderId() {
        return poPlanHeaderId;
    }

    public SinochemintlPoPlanLineDTO setPoPlanHeaderId(Long poPlanHeaderId) {
        this.poPlanHeaderId = poPlanHeaderId;
        return this;
    }

    /**
     * @return 已拼单省区数量
     */
    public Long getSpellDocProvince() {
        return spellDocProvince;
    }

    public SinochemintlPoPlanLineDTO setSpellDocProvince(Long spellDocProvince) {
        this.spellDocProvince = spellDocProvince;
        return this;
    }

    /**
     * @return 需求日期
     */
    public Date getNeedDate() {
        return needDate;
    }

    public SinochemintlPoPlanLineDTO setNeedDate(Date needDate) {
        this.needDate = needDate;
        return this;
    }

    /**
     * @return 计划共享省区
     */
    public String getPlanSharedProvince() {
        return planSharedProvince;
    }

    public SinochemintlPoPlanLineDTO setPlanSharedProvince(String planSharedProvince) {
        this.planSharedProvince = planSharedProvince;
        return this;
    }

    /**
     * @return 省公司/项目
     */
    public String getProvinceCompany() {
        return provinceCompany;
    }

    public SinochemintlPoPlanLineDTO setProvinceCompany(String provinceCompany) {
        this.provinceCompany = provinceCompany;
        return this;
    }

    /**
     * @return 产品名称
     */
    public String getProductName() {
        return productName;
    }

    public SinochemintlPoPlanLineDTO setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    /**
     * @return 规格
     */
    public String getSpecification() {
        return specification;
    }

    public SinochemintlPoPlanLineDTO setSpecification(String specification) {
        this.specification = specification;
        return this;
    }

    /**
     * @return 初步沟通供应商
     */
    public String getInitialSupplier() {
        return initialSupplier;
    }

    public SinochemintlPoPlanLineDTO setInitialSupplier(String initialSupplier) {
        this.initialSupplier = initialSupplier;
        return this;
    }

    /**
     * @return 出厂价
     */
    public BigDecimal getFactoryPrice() {
        return factoryPrice;
    }

    public SinochemintlPoPlanLineDTO setFactoryPrice(BigDecimal factoryPrice) {
        this.factoryPrice = factoryPrice;
        return this;
    }

    /**
     * @return 发货地址
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public SinochemintlPoPlanLineDTO setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    /**
     * @return 物料编码
     */
    public String getMaterialCoding() {
        return materialCoding;
    }

    public SinochemintlPoPlanLineDTO setMaterialCoding(String materialCoding) {
        this.materialCoding = materialCoding;
        return this;
    }

    /**
     * @return 物料分类
     */
    public String getMaterialClass() {
        return materialClass;
    }

    public SinochemintlPoPlanLineDTO setMaterialClass(String materialClass) {
        this.materialClass = materialClass;
        return this;
    }

    /**
     * @return 需求数量
     */
    public BigDecimal getRequiredQuantity() {
        return requiredQuantity;
    }

    public SinochemintlPoPlanLineDTO setRequiredQuantity(BigDecimal requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
        return this;
    }

    /**
     * @return 单位
     */
    public String getUomCode() {
        return uomCode;
    }

    public SinochemintlPoPlanLineDTO setUomCode(String uomCode) {
        this.uomCode = uomCode;
        return this;
    }

    /**
     * @return 采购总价
     */
    public BigDecimal getTotalPurchasePrice() {
        return totalPurchasePrice;
    }

    public SinochemintlPoPlanLineDTO setTotalPurchasePrice(BigDecimal totalPurchasePrice) {
        this.totalPurchasePrice = totalPurchasePrice;
        return this;
    }

    /**
     * @return 运费供应商
     */
    public String getFreightSupplier() {
        return freightSupplier;
    }

    public SinochemintlPoPlanLineDTO setFreightSupplier(String freightSupplier) {
        this.freightSupplier = freightSupplier;
        return this;
    }

    /**
     * @return 运费价格
     */
    public BigDecimal getFreightPrice() {
        return freightPrice;
    }

    public SinochemintlPoPlanLineDTO setFreightPrice(BigDecimal freightPrice) {
        this.freightPrice = freightPrice;
        return this;
    }

    /**
     * @return 价格条款
     */
    public String getPriceTerms() {
        return priceTerms;
    }

    public SinochemintlPoPlanLineDTO setPriceTerms(String priceTerms) {
        this.priceTerms = priceTerms;
        return this;
    }

    /**
     * @return 备注
     */
    public String getRemark() {
        return remark;
    }

    public SinochemintlPoPlanLineDTO setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    /**
     * @return 税种
     */
    public String getTaxType() {
        return taxType;
    }

    public SinochemintlPoPlanLineDTO setTaxType(String taxType) {
        this.taxType = taxType;
        return this;
    }

    /**
     * @return 税率
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public SinochemintlPoPlanLineDTO setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
        return this;
    }

    /**
     * @return 申请人
     */
    public String getApplicant() {
        return applicant;
    }

    public SinochemintlPoPlanLineDTO setApplicant(String applicant) {
        this.applicant = applicant;
        return this;
    }

    /**
     * @return 附件id
     */
    public String getAppendixId() {
        return appendixId;
    }

    public SinochemintlPoPlanLineDTO setAppendixId(String appendixId) {
        this.appendixId = appendixId;
        return this;
    }
}
