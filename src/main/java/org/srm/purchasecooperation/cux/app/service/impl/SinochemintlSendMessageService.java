package org.srm.purchasecooperation.cux.app.service.impl;

import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import org.hzero.boot.message.MessageClient;
import org.hzero.boot.message.entity.Receiver;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.core.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.stereotype.Component;
import org.srm.purchasecooperation.cux.api.dto.MessageSenderDTO;
import org.srm.purchasecooperation.cux.api.dto.SinochemintlPoPlanHeaderDTO;
import org.srm.purchasecooperation.cux.domain.repository.SinochemintlPoPlanHeaderRepository;
import org.srm.purchasecooperation.cux.infra.constant.SinochemintlConstant;
import org.srm.purchasecooperation.cux.infra.constant.SinochemintlMessageConstant;
import scala.Int;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * description
 *
 * @author jiaxing.huang@hand-china.com  2021/08/16 15:49
 */
@Component
public class SinochemintlSendMessageService {

    @Autowired
    private MessageClient messageClient;
    @Autowired
    private SinochemintlPoPlanHeaderRepository sinochemintlPoPlanHeaderRepository;
    @Autowired
    public LovAdapter lovAdapter;


    /**
     * 发送邮件
     *
     * @param messageSender
     */
    public void sendEmail(MessageSenderDTO messageSender) {
        messageClient.async().sendEmail(messageSender.getTenantId(), messageSender.getServerCode(), messageSender.getMessageCode(), messageSender.getReceiverList(), messageSender.getParamMap());
    }

    /**
     * 发送短信
     *
     * @param messageSender
     */
    public void sendSms(MessageSenderDTO messageSender) {
        messageClient.async().sendSms(messageSender.getTenantId(), messageSender.getServerCode(), messageSender.getMessageCode(), messageSender.getReceiverList(), messageSender.getParamMap());
    }

    /**
     * 发送站内消息
     *
     * @param messageSender
     */
    public void sendWebMessage(MessageSenderDTO messageSender) {
        messageClient.async().sendWebMessage(messageSender.getTenantId(), messageSender.getMessageCode(), messageSender.getLang(), messageSender.getReceiverList(), messageSender.getParamMap());
    }

    /**
     * 获取消息模板参数
     *
     * @param poPlanHeader
     * @return
     */
    public Map<String, String> getCommonParam(SinochemintlPoPlanHeaderDTO poPlanHeader) {
        Map<String, String> paramMap = new HashMap<>(BaseConstants.Digital.SIXTEEN);
        paramMap.put(SinochemintlMessageConstant.MessageTemplateParameters.Parameter_Company_Name, poPlanHeader.getCompanyName());
        paramMap.put(SinochemintlMessageConstant.MessageTemplateParameters.Parameter_Applicant, poPlanHeader.getApplicant());
        paramMap.put(SinochemintlMessageConstant.MessageTemplateParameters.Parameter_Po_Plan_Number, poPlanHeader.getPoPlanNumber());
        paramMap.put(SinochemintlMessageConstant.MessageTemplateParameters.Parameter_Title, poPlanHeader.getTitle());
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        paramMap.put(SinochemintlMessageConstant.MessageTemplateParameters.Parameter_Deadline, dateFormat.format(poPlanHeader.getDeadline()));
        return paramMap;
    }

    /**
     * 获取接受者
     *
     * @param companyIds
     * @return
     */
    public List<Receiver> getReceiverList(Set<Integer> companyIds) {
        CustomUserDetails user = DetailsHelper.getUserDetails();
        List<LovValueDTO> lovValues = lovAdapter.queryLovValue(SinochemintlConstant.CodingCode.SPUC_SINOCHEMINTL_PURCHASING_AGENT, user.getTenantId());
        List<String> companys = new ArrayList<>();
        StringBuffer receivers = new StringBuffer();
        companys = sinochemintlPoPlanHeaderRepository.getCompanies(new ArrayList<Integer>(companyIds));
        for(String string : companys){
            for(LovValueDTO lovValueDTO : lovValues){
                if(lovValueDTO.getValue().intern().equals(string) || lovValueDTO.getValue().intern().equals("中化现代农业有限公司")){
                    receivers.append(lovValueDTO.getMeaning()).append(",");
                }
            }
        }
        String[] receiver = receivers.toString().replaceAll(" ","").trim().split(",");
        List<String> list = Arrays.asList(receiver);
        return sinochemintlPoPlanHeaderRepository.getLovEmployeeList(list);
    }
}

