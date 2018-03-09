package com.yikuyi.party.listener;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yikuyi.party.vendor.vo.PartyProductLineRequest;
import com.yikuyi.party.vendorManage.bll.VendorManage2;
import com.ykyframework.exception.BusinessException;
import com.ykyframework.mqservice.receiver.MsgReceiveListener;

@Service
public class PartyExportReceiveListener implements MsgReceiveListener{

	private static final Logger logger = LoggerFactory.getLogger(PartyExportReceiveListener.class);
	
	@Autowired
	private VendorManage2 vendorManage2;
	@Override
	public void onMessage(Serializable msg) {
		if (null == msg) {
			logger.error("ProductLineReceiveListener msg is null!");
			return;
		}
		if(msg instanceof PartyProductLineRequest){
			PartyProductLineRequest partyProductLineRequest = (PartyProductLineRequest)msg;
			try {
				vendorManage2.doExport(partyProductLineRequest);
			} catch (BusinessException e) {
				logger.error("ProductLineReceiveListener export file error");
			}
		}
	}

}
