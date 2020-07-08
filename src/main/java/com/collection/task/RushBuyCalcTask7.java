package com.collection.task;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.collection.common.BaseTask;
import com.collection.frame.Crontask;
import com.collection.service.IRushBuyClacService;
import com.collection.util.DateUtil;

/**
 * 金尊会员卡定时分配抢购会员卡计算任务
 * 执行时刻读表三个字段
 * @author Administrator
 *
 */
@BaseTask
public class RushBuyCalcTask7 extends Crontask{
	@Resource
	private IRushBuyClacService service;

	@Override
	public void run() {
		//计算时间
		String calctime = DateUtil.dateTimeToString(this.getRunTimeNoDelay());
		//金尊会员卡调用计算分配逻辑
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("forder", 7);
		data.put("calctime", calctime);
		service.rushBuyCalc(data);
	}
	
}
