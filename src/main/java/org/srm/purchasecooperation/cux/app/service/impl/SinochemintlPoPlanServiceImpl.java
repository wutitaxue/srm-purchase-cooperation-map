package org.srm.purchasecooperation.cux.app.service.impl;

import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.pagehelper.PageHelper;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.boot.platform.code.builder.CodeRuleBuilder;
import org.hzero.boot.platform.code.constant.CodeConstants;
import org.hzero.core.base.BaseAppService;
import org.hzero.core.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.srm.purchasecooperation.cux.api.dto.MessageSenderDTO;
import org.srm.purchasecooperation.cux.api.dto.SinochemintlPoPlanExcelDTO;
import org.srm.purchasecooperation.cux.api.dto.SinochemintlPoPlanHeaderDTO;
import org.srm.purchasecooperation.cux.api.dto.SinochemintlPoPlanLineDTO;
import org.srm.purchasecooperation.cux.app.service.SinochemintlPoPlanService;
import org.srm.purchasecooperation.cux.domain.repository.SinochemintlPoPlanHeaderRepository;
import org.srm.purchasecooperation.cux.domain.repository.SinochemintlPoPlanLineRepository;
import org.srm.purchasecooperation.cux.infra.constant.SinochemintlConstant;
import org.srm.purchasecooperation.cux.infra.constant.SinochemintlMessageConstant;

import java.util.*;

/**
 * 采购计划应用服务默认实现
 *
 * @author tianjing.gui@hand-china.com 2021-09-06 09:59:20
 */
@Service
public class SinochemintlPoPlanServiceImpl extends BaseAppService implements SinochemintlPoPlanService {

    private static final Logger logger = LoggerFactory.getLogger(SinochemintlPoPlanServiceImpl.class);

    private final SinochemintlPoPlanHeaderRepository sinochemintlPoPlanHeaderRepository;

    private final SinochemintlPoPlanLineRepository sinochemintlPoPlanLineRepository;

    private final SinochemintlSendMessageService sinochemintlSendMessageService;

    @Autowired
    private CodeRuleBuilder codeRuleBuilder;

    @Autowired
    public SinochemintlPoPlanServiceImpl(SinochemintlPoPlanHeaderRepository sinochemintlPoPlanHeaderRepository, SinochemintlPoPlanLineRepository sinochemintlPoPlanLineRepository, SinochemintlSendMessageService sinochemintlSendMessageService) {
        this.sinochemintlPoPlanHeaderRepository = sinochemintlPoPlanHeaderRepository;
        this.sinochemintlPoPlanLineRepository = sinochemintlPoPlanLineRepository;
        this.sinochemintlSendMessageService = sinochemintlSendMessageService;
    }

    /**
     * 采购计划头表查询参数
     *
     * @param sinochemintlPoPlanHeaderDTO 采购计划头表
     * @param pageRequest                 分页
     * @return 采购计划头表列表
     */
    @Override
    public Page<SinochemintlPoPlanHeaderDTO> list(SinochemintlPoPlanHeaderDTO sinochemintlPoPlanHeaderDTO, PageRequest pageRequest) {
        //当自己为采购创建人时，只能查询到状态为新建或拼单中的采购计划  当自己为共享省区对应人时，只能查询到状态为拼单中的采购计划数据
        //获取用户当前登录用户所在公司
        CustomUserDetails user = DetailsHelper.getUserDetails();
        SinochemintlPoPlanLineDTO sinochemintlPoPlanLineDTO = sinochemintlPoPlanHeaderRepository.getDefaultCompanyId(user.getUserId());
        //非总部人员只可查看和自己有关的数据
        if (!"1510".equals(sinochemintlPoPlanLineDTO.getProvinceCompany())) {
            sinochemintlPoPlanHeaderDTO.setUserCompany(sinochemintlPoPlanLineDTO.getProvinceCompanyId());
            sinochemintlPoPlanHeaderDTO.setCreateId(user.getUserId());
        }
        //采购计划维护分页查询逻辑重写
        if ("MAINTAIN".equals(sinochemintlPoPlanHeaderDTO.getStatus())) {
            return PageHelper.doPage(pageRequest, () -> sinochemintlPoPlanHeaderRepository.maintain(sinochemintlPoPlanHeaderDTO));
        } else {
            return PageHelper.doPage(pageRequest, () -> sinochemintlPoPlanHeaderRepository.list(sinochemintlPoPlanHeaderDTO));
        }
    }

    /**
     * 新增/保存/修改采购计划
     *
     * @param sinochemintlPoPlanHeaderDTO 采购计划头表和行表数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SinochemintlPoPlanHeaderDTO addPoPlan(SinochemintlPoPlanHeaderDTO sinochemintlPoPlanHeaderDTO, PageRequest pageRequest) {
        //创建头表并保存
        Date date = new Date();
        CustomUserDetails user = DetailsHelper.getUserDetails();
        if (StringUtils.isEmpty(sinochemintlPoPlanHeaderDTO.getPoPlanHeaderId())) {
            //id无值 状态无值 新增操作
            if (StringUtils.isEmpty(sinochemintlPoPlanHeaderDTO.getStatus()) && StringUtils.isEmpty(sinochemintlPoPlanHeaderDTO.getCreateName())) {
                //获取采购计划单号
                String poPlanNumber = codeRuleBuilder.generateCode(sinochemintlPoPlanHeaderDTO.getTenantId(), SinochemintlConstant.CodingCode.SCUX_ZHNY_RULES_PO_PLAN,
                        CodeConstants.CodeRuleLevelCode.GLOBAL, CodeConstants.CodeRuleLevelCode.GLOBAL, null);
                sinochemintlPoPlanHeaderDTO.setPoPlanNumber(poPlanNumber);
                sinochemintlPoPlanHeaderDTO.setStatus(SinochemintlConstant.StatusCode.STATUS_NEW);
                //获取当前登录人信息
                sinochemintlPoPlanHeaderDTO.setCreateId(user.getUserId());
                sinochemintlPoPlanHeaderDTO.setCreateName(user.getRealName());
                //系统自动带出当前申请人
                sinochemintlPoPlanHeaderDTO.setApplicant(user.getRealName());
                sinochemintlPoPlanHeaderDTO.setApplicationDate(new Date());
                sinochemintlPoPlanHeaderDTO.setEstablishDate(date);
                //系统自动带出单据来源于哪个系统 暂时只默认SRM系统
                sinochemintlPoPlanHeaderDTO.setPoSource("SRM");
                return sinochemintlPoPlanHeaderDTO;
            }
            //id无值 保存操作
            sinochemintlPoPlanHeaderDTO.setCreationDate(date);
            sinochemintlPoPlanHeaderDTO.setCreatedBy(user.getUserId());
            sinochemintlPoPlanHeaderDTO.setLastUpdateDate(date);
            sinochemintlPoPlanHeaderDTO.setLastUpdatedBy(user.getUserId());
            sinochemintlPoPlanHeaderRepository.setOne(sinochemintlPoPlanHeaderDTO);
            Long poPlanHeaderId = sinochemintlPoPlanHeaderRepository.getPoPlanHeaderId(sinochemintlPoPlanHeaderDTO);
            sinochemintlPoPlanHeaderDTO.setPoPlanHeaderId(poPlanHeaderId);
        } else {
            //有值 修改操作
            sinochemintlPoPlanHeaderDTO.setLastUpdateDate(date);
            sinochemintlPoPlanHeaderDTO.setLastUpdatedBy(user.getUserId());
            sinochemintlPoPlanHeaderRepository.updateByKey(sinochemintlPoPlanHeaderDTO);
        }
        //获取行表数据
        List<SinochemintlPoPlanLineDTO> sinochemintlPoPlanLineList = sinochemintlPoPlanHeaderDTO.getSinochemintlPoPlanLineList();
        if (sinochemintlPoPlanLineList != null && !sinochemintlPoPlanLineList.isEmpty()) {
            for (SinochemintlPoPlanLineDTO sinochemintlPoPlanLineDTO : sinochemintlPoPlanLineList) {
                //拼接计划共享省区
                StringBuilder planSharedProvince = new StringBuilder("|");
                for (String planSharedProvinceName : sinochemintlPoPlanLineDTO.getPlanSharedProvinceName()) {
                    planSharedProvince.append(planSharedProvinceName).append("|");
                }
                sinochemintlPoPlanLineDTO.setPlanSharedProvince(planSharedProvince.toString());
                if (StringUtils.isEmpty(sinochemintlPoPlanLineDTO.getPoPlanLineId())) {
                    //id无值 新增操作
                    sinochemintlPoPlanLineDTO.setPoPlanHeaderId(sinochemintlPoPlanHeaderDTO.getPoPlanHeaderId());
                    sinochemintlPoPlanLineDTO.setTenantId(sinochemintlPoPlanHeaderDTO.getTenantId());
                    sinochemintlPoPlanLineDTO.setStatus(SinochemintlConstant.StatusCode.STATUS_NEW);
                    sinochemintlPoPlanLineDTO.setApplicant(user.getRealName());
                    sinochemintlPoPlanLineDTO.setCreationDate(date);
                    sinochemintlPoPlanLineDTO.setCreatedBy(user.getUserId());
                    sinochemintlPoPlanLineDTO.setLastUpdateDate(date);
                    sinochemintlPoPlanLineDTO.setLastUpdatedBy(user.getUserId());
                    sinochemintlPoPlanLineDTO.setSerialNum(sinochemintlPoPlanLineRepository.getSerialNum(String.valueOf(sinochemintlPoPlanHeaderDTO.getPoPlanHeaderId())) + 1);
                    sinochemintlPoPlanLineRepository.setOne(sinochemintlPoPlanLineDTO);
                } else {
                    //有值 修改操作
                    sinochemintlPoPlanLineDTO.setLastUpdateDate(date);
                    sinochemintlPoPlanLineDTO.setLastUpdatedBy(user.getUserId());
                    sinochemintlPoPlanLineRepository.updateByKey(sinochemintlPoPlanLineDTO);
                }
            }
        }
        return sinochemintlPoPlanHeaderDTO;
    }

    /**
     * 删除采购计划头表
     *
     * @param ids 头表id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delHeader(List<Long> ids) {
        if (!ids.isEmpty()) {
            for (Long id : ids) {
                SinochemintlPoPlanHeaderDTO sinochemintlPoPlanHeaderDTO = sinochemintlPoPlanHeaderRepository.selectByKey(id);
                if (sinochemintlPoPlanHeaderDTO == null) {
                    throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_PARAMETER_ERROR);
                }
                if (!sinochemintlPoPlanHeaderDTO.getStatus().equals(SinochemintlConstant.StatusCode.STATUS_NEW)) {
                    throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_CANT_DELETE);
                }
                //只要新增状态才可以删除
                sinochemintlPoPlanHeaderRepository.deleteByKey(sinochemintlPoPlanHeaderDTO.getPoPlanHeaderId());
            }
        }
    }

    /**
     * 删除采购计划行表
     *
     * @param ids 行表id集合
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delLine(List<Long> ids) {
        if (!ids.isEmpty()) {
            for (Long id : ids) {
                SinochemintlPoPlanLineDTO sinochemintlPoPlanLineDTO = sinochemintlPoPlanLineRepository.selectByKey(id);
                if (sinochemintlPoPlanLineDTO == null) {
                    throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_PARAMETER_ERROR);
                }
                if (!sinochemintlPoPlanLineDTO.getStatus().equals(SinochemintlConstant.StatusCode.STATUS_NEW)) {
                    throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_CANT_DELETE);
                }
                //只有新增状态才可以删除
                sinochemintlPoPlanLineRepository.deleteByKey(sinochemintlPoPlanLineDTO.getPoPlanLineId());
            }
        }
    }

    /**
     * 获取头行单表
     *
     * @param organizationId 租户id
     * @param poPlanHeaderId 头表id
     */
    @Override
    public SinochemintlPoPlanHeaderDTO getPoPlan(Long organizationId, Long poPlanHeaderId, PageRequest pageRequest) {
        //获取头数据
        SinochemintlPoPlanHeaderDTO sinochemintlPoPlanHeaderDTO = sinochemintlPoPlanHeaderRepository.selectByKey(poPlanHeaderId);
        if (sinochemintlPoPlanHeaderDTO == null) {
            throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_PARAMETER_ERROR);
        }

        return sinochemintlPoPlanHeaderDTO;
    }

    /**
     * 获取行表列表
     *
     * @param organizationId 租户id
     * @param poPlanHeaderId 头表id
     * @param pageRequest    分页参数
     * @return 行表列表
     */
    @Override
    public Page<SinochemintlPoPlanLineDTO> getPoPlanLine(Long organizationId, Long poPlanHeaderId, PageRequest pageRequest) {
        //获取行数据
        Page<SinochemintlPoPlanLineDTO> sinochemintlPoPlanLineList = PageHelper.doPage(pageRequest, () -> sinochemintlPoPlanLineRepository.selectByHeaderId(organizationId, poPlanHeaderId));
        if (!sinochemintlPoPlanLineList.isEmpty()) {
            for (SinochemintlPoPlanLineDTO sinochemintlPoPlanLineDTO : sinochemintlPoPlanLineList) {
                if (!StringUtils.isEmpty(sinochemintlPoPlanLineDTO.getPlanSharedProvince())) {
                    List<String> strings = Arrays.asList(sinochemintlPoPlanLineDTO.getPlanSharedProvince().substring(1).split("\\|"));
                    sinochemintlPoPlanLineDTO.setPlanSharedProvinceName(strings);
                }
            }
        }
        return sinochemintlPoPlanLineList;
    }

    /**
     * 提交采购计划
     *
     * @param poPlanHeaderId 头表id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(Long organizationId, Long poPlanHeaderId) {
        //提交采购计划
        SinochemintlPoPlanHeaderDTO sinochemintlPoPlanHeaderDTO = sinochemintlPoPlanHeaderRepository.selectByKey(poPlanHeaderId);
        List<SinochemintlPoPlanLineDTO> sinochemintlPoPlanLineList = sinochemintlPoPlanLineRepository.selectByHeaderId(organizationId, poPlanHeaderId);
        sinochemintlPoPlanHeaderDTO.setStatus(SinochemintlConstant.StatusCode.STATUS_SPLICING_DOC_MIDDLE);
        for (SinochemintlPoPlanLineDTO sinochemintlPoPlanLineDTO : sinochemintlPoPlanLineList) {
            if (sinochemintlPoPlanLineDTO == null) {
                throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_LINE_NO_DATA);
            }
            if (sinochemintlPoPlanLineDTO.getSharedProvinceId() == 0) {
                //校验共享省区数量，若均已填写，则状态更新为【拼单完成】
                List<String> strings = Arrays.asList(sinochemintlPoPlanLineDTO.getPlanSharedProvince().substring(1).split("\\|"));
                if (strings.size() <= sinochemintlPoPlanLineDTO.getSpellDocProvince()) {
                    sinochemintlPoPlanHeaderDTO.setStatus(SinochemintlConstant.StatusCode.STATUS_SPLICING_DOC_COMPLETE);
                    if (sinochemintlPoPlanLineDTO.getSpellDocProvince() != 1) {
                        List<Receiver> receiverList = new ArrayList<>();
                        for (String string : strings) {
                            receiverList.addAll(sinochemintlSendMessageService.getReceiverList(string));
                        }
                        try {
                            Map<String, String> paramMap = new HashMap<>(BaseConstants.Digital.SIXTEEN);
                            paramMap.putAll(sinochemintlSendMessageService.getCommonParam(sinochemintlPoPlanHeaderDTO));
                            sinochemintlSendMessageService.sendEmail(new MessageSenderDTO(organizationId, SinochemintlMessageConstant.Message_Submit_Template_Code, SinochemintlMessageConstant.Email_Server_Code, receiverList, paramMap, null));
                            sinochemintlSendMessageService.sendSms(new MessageSenderDTO(organizationId, SinochemintlMessageConstant.Message_Submit_Template_Code, SinochemintlMessageConstant.Sms_Server_Code, receiverList, paramMap, null));
                            sinochemintlSendMessageService.sendWebMessage(new MessageSenderDTO(organizationId, SinochemintlMessageConstant.Message_Submit_Template_Code, null, receiverList, paramMap, SinochemintlMessageConstant.Web_Message_Language_Chinese));
                        } catch (IllegalArgumentException e) {
                            logger.error("Message sending failure:{}", receiverList);
                        }
                    }
                }
            }
        }
        sinochemintlPoPlanHeaderDTO.setLastUpdateDate(new Date());
        sinochemintlPoPlanHeaderDTO.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
        sinochemintlPoPlanHeaderRepository.updateByKey(sinochemintlPoPlanHeaderDTO);
    }

    /**
     * 取消采购计划
     *
     * @param poPlanHeaderId 头表id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Long poPlanHeaderId) {
        //取消采购计划
        SinochemintlPoPlanHeaderDTO sinochemintlPoPlanHeaderDTO = sinochemintlPoPlanHeaderRepository.selectByKey(poPlanHeaderId);
        if (!sinochemintlPoPlanHeaderDTO.getCreateId().equals(DetailsHelper.getUserDetails().getUserId())) {
            //只有创建人才可以取消
            throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_NOT_FOUNDER_CANCEL);
        }
        if (!sinochemintlPoPlanHeaderDTO.getStatus().equals(SinochemintlConstant.StatusCode.STATUS_NEW)) {
            //非新建状态无法取消
            throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_NON_NEW_NOT_CANCEL);
        }
        sinochemintlPoPlanHeaderDTO.setStatus(SinochemintlConstant.StatusCode.STATUS_CANCELLED);
        sinochemintlPoPlanHeaderDTO.setLastUpdateDate(new Date());
        sinochemintlPoPlanHeaderDTO.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
        sinochemintlPoPlanHeaderRepository.updateByKey(sinochemintlPoPlanHeaderDTO);
    }

    /**
     * 采购计划导出
     *
     * @param ids 勾选的头表id
     * @return 需要导出的结果
     */
    @Override
    public List<SinochemintlPoPlanExcelDTO> excel(List<Long> ids) {
        List<SinochemintlPoPlanExcelDTO> excel = sinochemintlPoPlanHeaderRepository.excel(ids);
        //转换共享省区
        for (SinochemintlPoPlanExcelDTO sinochemintlPoPlanExcelDTO : excel) {
            List<String> strings = Arrays.asList(sinochemintlPoPlanExcelDTO.getPlanSharedProvince().substring(1).split("\\|"));
            //获取共享计划省区
            List<String> list = sinochemintlPoPlanLineRepository.getPlanSharedProvince(strings);
            StringBuilder planSharedProvince = new StringBuilder();
            for (String string : list) {
                planSharedProvince.append(string).append("、");
            }
            sinochemintlPoPlanExcelDTO.setPlanSharedProvince(planSharedProvince.toString());
        }
        return excel;
    }

    /**
     * 采购计划确认
     *
     * @param dto 修改后的数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirm(SinochemintlPoPlanHeaderDTO dto) {
        CustomUserDetails user = DetailsHelper.getUserDetails();
        if (!user.getUserId().equals(dto.getCreateId())) {
            throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_NOT_FOUNDER);
        }
        for (SinochemintlPoPlanLineDTO sinochemintlPoPlanLineDTO : dto.getSinochemintlPoPlanLineList()) {
            if (StringUtils.isEmpty(sinochemintlPoPlanLineDTO.getEndSupplier()) || StringUtils.isEmpty(sinochemintlPoPlanLineDTO.getEndPrice())) {
                throw new CommonException(SinochemintlConstant.ErrorCode.ERROR_RESULTS_NOT_ENTERED);
            }
            sinochemintlPoPlanLineDTO.setStatus(SinochemintlConstant.StatusCode.STATUS_INPUT_COMPLETE);
            sinochemintlPoPlanLineRepository.updateByKey(sinochemintlPoPlanLineDTO);
            //录入完成 发送消息
            if (!StringUtils.isEmpty(sinochemintlPoPlanLineDTO.getPlanSharedProvince())) {
                List<String> strings = Arrays.asList(sinochemintlPoPlanLineDTO.getPlanSharedProvince().substring(1).split("\\|"));
                Long organizationId = dto.getTenantId();
                List<Receiver> receiverList = new ArrayList<>();
                if (strings.size() > 0) {
                    for (String string : strings) {
                        receiverList.addAll(sinochemintlSendMessageService.getReceiverList(string));
                    }
                    try {
                        Map<String, String> paramMap = new HashMap<>(BaseConstants.Digital.SIXTEEN);
                        paramMap.putAll(sinochemintlSendMessageService.getCommonParam(dto));
                        sinochemintlSendMessageService.sendEmail(new MessageSenderDTO(organizationId, SinochemintlMessageConstant.Message_Input_Template_Code, SinochemintlMessageConstant.Email_Server_Code, receiverList, paramMap, null));
                        sinochemintlSendMessageService.sendSms(new MessageSenderDTO(organizationId, SinochemintlMessageConstant.Message_Input_Template_Code, SinochemintlMessageConstant.Sms_Server_Code, receiverList, paramMap, null));
                        sinochemintlSendMessageService.sendWebMessage(new MessageSenderDTO(organizationId, SinochemintlMessageConstant.Message_Input_Template_Code, null, receiverList, paramMap, SinochemintlMessageConstant.Web_Message_Language_Chinese));
                    } catch (IllegalArgumentException e) {
                        logger.error("Message sending failure:{}", receiverList);
                    }
                }
            }
        }
        dto.setStatus(SinochemintlConstant.StatusCode.STATUS_INPUT_COMPLETE);
        dto.setLastUpdateDate(new Date());
        dto.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
        sinochemintlPoPlanHeaderRepository.updateByKey(dto);
    }

}
