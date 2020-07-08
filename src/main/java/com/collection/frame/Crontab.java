package com.collection.frame;

/*
 * Crontab.java(秒级)
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * 定时任务表达式类
 */
public class Crontab {
	//任务crontab
	private String   m_crontab  = null;
	//任务开始日期
	private Calendar m_startDate = null;
	//任务结束日期
	private Calendar m_endDate = null;
	//任务起始时间
	private Calendar m_startTime = null;
	//任务结束时间
	private Calendar m_endTime  = null;
	//任务延迟执行秒数
	private int      m_delaySec = 0;
	//秒
	private static final int SECOND = 0;
	//分
	private static final int MINUTE = 1;
	//时
	private static final int HOUR = 2;
	//日
	private static final int DAY_OF_MONTH = 3;
	//月
	private static final int MONTH = 4;
	//周
	private static final int DAY_OF_WEEK = 5;
	//*
	private static final int ALL_SPEC_INT = 99;
	//'*'
	private static final Integer ALL_SPEC = ALL_SPEC_INT;
	//非法字符校验正则表达式
	private final Pattern check = Pattern.compile("[0-9 ,*/-]+$");
	//固定频率校验正则表达式
	private final Pattern frequ = Pattern.compile("^[1-9]{1}(\\d+?)*$");
	//固定时间格式校验正则表达式
	private final Pattern fixck = Pattern.compile("^([0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2})(\\,([0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}))*$");
	//域内容校验正则表达式
	private final Pattern yield = Pattern.compile("^(([0-9]{1,2}|[*]{1})((-[0-9]{1,2})?(/\\d+)?)?)(\\,([0-9]{1,2}|[*]{1})((-[0-9]{1,2})?(/\\d+)?)?)*$");
	//空格分割正则表达式
	private final Pattern space = Pattern.compile("[ ]+");
	//冒号分割正则表达式
	private final Pattern colon = Pattern.compile("[:]+");
	//包含-的正则表达式
	private final Pattern minus = Pattern.compile("[-]+");
	//包含,的正则表达式
	private final Pattern dot   = Pattern.compile("[,]+");
	//包含/的正则表达式
	private final Pattern oblus = Pattern.compile("[/]+");
	//秒、分、时、日、月、周的值
	private TreeSet<Integer> m_seconds = null;
	private TreeSet<Integer> m_minutes = null;
	private TreeSet<Integer> m_hours   = null;
	private TreeSet<Integer> m_daysOfMonth = null;
	private TreeSet<Integer> m_months  = null;
	private TreeSet<Integer> m_daysOfWeek = null;
	//固定时间串
	private TreeSet<Calendar> m_timeOfDay = null;
	//固定频率(单位秒)
	private Integer m_frequency = null;
	//任务开始和结束日期格式
	private final SimpleDateFormat fmtAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//每日起始和终止时间格式
	private final SimpleDateFormat fmtTime = new SimpleDateFormat("HH:mm:ss");
	//最大年数(当前年+100)
	private final int MAX_YEAR = Calendar.getInstance().get(Calendar.YEAR) + 100;
	//月份和星期初始化
	private static final String[][] replaceStr = {
		// 月份
		{ "JAN", "1" }, { "FEB", "2" }, { "MAR", "3" }, { "APR", "4" }, 
		{ "MAY", "5" }, { "JUN", "6" }, { "JUL", "7" }, { "AUG", "8" }, 
		{ "SEP", "9" }, { "OCT", "10"}, { "NOV", "11"}, { "DEC", "12"},
		// 星期
		{ "SUN", "0" }, { "MON", "1" }, { "TUE", "2" }, { "WED", "3" },
		{ "THU", "4" }, { "FRI", "5" }, { "SAT", "6" } };
	//日志操作
	private static final Logger m_logger = Logger.getLogger(Crontab.class);
	
	/**
	 * 构造方法
	 * @param crontab 定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 */
	public Crontab(String crontab) {
		setCrontab(crontab);
	}
	
	/**
	 * 构造方法
	 * @param crontab  定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 * @param delaySec 任务延迟执行的秒数(即执行时间需要+delaySec秒,默认为0)
	 */
	public Crontab(String crontab, int delaySec) {
		setCrontab(crontab);
		setDelaySec(delaySec);
	}
	
	/**
	 * 构造方法
	 * @param crontab   定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 * @param startTime 定时任务每天的开始时间,格式HH:mm:ss
	 * @param endTime   定时任务每天的结束时间,格式HH:mm:ss
	 */
	public Crontab(String crontab, String startTime, String endTime) {
		setCrontab(crontab);
		setStartTime(startTime);
		setEndTime(endTime);
	}
	
	/**
	 * 构造方法
	 * @param crontab   定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 * @param startTime 定时任务每天的开始时间,格式HH:mm:ss
	 * @param endTime   定时任务每天的结束时间,格式HH:mm:ss
	 * @param delaySec  任务延迟执行的秒数(即执行时间需要+delaySec秒,默认为0)
	 */
	public Crontab(String crontab, String startTime, String endTime, int delaySec) {
		setCrontab(crontab);
		setStartTime(startTime);
		setEndTime(endTime);
		setDelaySec(delaySec);
	}
	
	/**
	 * 构造方法
	 * @param crontab   定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 * @param startTime 定时任务每天的开始时间,格式HH:mm:ss
	 * @param endTime   定时任务每天的结束时间,格式HH:mm:ss
	 * @param startDate 定时任务开始日期,格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 * @param endDate   定时任务结束日期,格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 */
	public Crontab(String crontab, String startTime, String endTime, String startDate, String endDate) {
		setCrontab(crontab);
		setStartTime(startTime);
		setEndTime(endTime);
		setStartDate(startDate);
		setEndDate(endDate);
	}
	
	/**
	 * 构造方法
	 * @param crontab   定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 * @param startTime 定时任务每天的开始时间,格式HH:mm:ss
	 * @param endTime   定时任务每天的结束时间,格式HH:mm:ss
	 * @param startDate 定时任务开始日期,格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 * @param endDate   定时任务结束日期,格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 * @param delaySec  任务延迟执行的秒数(即执行时间需要+delaySec秒,默认为0)
	 */
	public Crontab(String crontab, String startTime, String endTime, String startDate, String endDate, int delaySec) {
		setCrontab(crontab);
		setStartTime(startTime);
		setEndTime(endTime);
		setStartDate(startDate);
		setEndDate(endDate);
		setDelaySec(delaySec);
	}

	/**
	 * 设置crontab表达式
	 * @param crontab 定时任务表达式,格式:(秒 分 时 日 月 周(空格分隔))或(逗号分隔的固定时间串(HH:mm:ss,HH:mm:ss不能含空格))或(固定频率(单位秒))
	 */
	public void setCrontab(String crontab) {
		//crontab是否为空
		if (crontab == null || crontab.trim().length() <= 0) {
			throw new EBusiness("输入的调度时间不能为空");
		}
		
		//初始化
		m_seconds     = null;
		m_minutes     = null;
		m_hours       = null;
		m_daysOfMonth = null;
		m_months      = null;
		m_daysOfWeek  = null;
		m_timeOfDay   = null;
		m_frequency   = null;
		
		//固定频率(单位秒)
		if (frequ.matcher(crontab.trim()).matches())
		{
			m_crontab = crontab.trim();
			m_logger.debug("crontab为:" + m_crontab);
			
			//设置频率
			m_frequency = new Integer(m_crontab);
		}
		//固定时间串格式(HH:mm:ss,HH:mm:ss,HH:mm:ss)
		else if (fixck.matcher(crontab.trim()).matches())
		{
			m_crontab = crontab.trim();
			m_logger.debug("crontab为:" + m_crontab);
			
			//按逗号进行分割
			String[] result = dot.split(m_crontab);
			for (int i = 0; i < result.length; i++)
			{
				storeTime(result[i]);
			}
		}
		//<秒 分 时 日 月 周>格式
		else
		{
			m_crontab = crontab.trim().toUpperCase(Locale.US);
			m_logger.debug("crontab为:" + m_crontab);
			
			String cron = m_crontab;
			for (int i = 0; i < replaceStr.length; i++) {
				cron = cron.replace(replaceStr[i][0], replaceStr[i][1]); 
			}
			m_logger.debug("crontab为:" + cron);
			
			//判断是否包含非法字符
			if (!check.matcher(cron).matches())
			{
				throw new EBusiness("输入的调度时间[" + m_crontab + "]格式不正确");
			}
			
			//按空格进行分割
			String[] result = space.split(cron);
			if (result.length != 6)
			{
				throw new EBusiness("输入的调度时间[" + m_crontab + "]格式不正确: (秒 分 时 日 月 周)或(HH:mm:ss,HH:mm:ss,...))或(固定频率(秒))");
			}
			
			//校验每个时间域格式
			for (int i = 0; i < result.length; i++)
			{
				m_logger.debug("["
						+ (i == SECOND ? "秒" : (i == MINUTE ? "分" : (i == HOUR ? "时"
						: (i == DAY_OF_MONTH ? "日" : (i == MONTH ? "月" : "周"))))) + "]为:"
						+ result[i]);
				storeYiead(i, result[i]);
			}
		}
	}
	
	/**
	 * 获取定时任务表达式
	 * @return 定时任务表达式
	 */
	public String getCrontab() {
		return m_crontab;
	}
	
	/**
	 * @param value
	 */
	private void storeTime(String value) {
		if (value == null || value.length() < 0)
		{
			throw new EBusiness("固定时间串格式不正确");
		}

		String[] result = colon.split(value);
		if (result.length != 3)
		{
			throw new EBusiness("输入的调度时间[" + value + "]格式不正确, 正确格式为HH:mm:ss");
		}
		
		//校验时间格式
		int hour   = Integer.valueOf(result[0]);
		int minute = Integer.valueOf(result[1]);
		int second = Integer.valueOf(result[2]);
		if (hour < 0 || hour > 23)
		{
			throw new EBusiness("输入的调度时间[" + value + "]的小时数必须在0-23之间");
		}
		
		if (minute < 0 || minute > 59)
		{
			throw new EBusiness("输入的调度时间[" + value + "]的分钟数必须在0-59之间");
		}
		
		if (second < 0 || second > 59)
		{
			throw new EBusiness("输入的调度时间[" + value + "]的秒数必须在0-59之间");
		}
		
		try
		{
			Date date = fmtTime.parse(value);
			Calendar time = Calendar.getInstance();
			time.setTime(date);
			time.set(Calendar.YEAR, 1);
			time.set(Calendar.MONTH, Calendar.JANUARY);
			time.set(Calendar.DAY_OF_MONTH, 1);
			time.set(Calendar.MILLISECOND, 0);
			
			if (m_timeOfDay == null) {
				m_timeOfDay = new TreeSet<Calendar>();
			}
			m_timeOfDay.add(time);
		}
		catch (ParseException e)
		{
			throw new EBusiness("时间[" + value + "]格式不正确,正确格式应该是HH:mm:ss");
		}
	}
	
	/**
	 * 解析保存域值
	 */
	private void storeYiead(int type, String value) {
		// 检查字符串合法性
		if (!yield.matcher(value).matches()) {
			throw new EBusiness("错误的表达式格式:" + value);
		}
		
		try
		{	
			String[] dots = null;
			String[] obls = null;
			String[] mins = null;
			// ','号分割
			dots = dot.split(value);
			for (int i = 0; i < dots.length; i++)
			{
				// '-'号分割
				mins = minus.split(dots[i]);
				if (mins.length == 1)
				{
					// '/'号分割
					obls = oblus.split(mins[0]);
					// 单个xx的格式
					if (obls.length == 1)
					{
						// '*'格式
						if (obls[0].equals("*"))
						{
							addToSet(type, ALL_SPEC_INT, -1, 0);
						}
						else
						{
							addToSet(type, Integer.valueOf(obls[0]), -1, -1);
						}
					}
					// 'xx/xx'格式
					else
					{
						// '*/xx'格式
						if (obls[0].equals("*"))
						{
							addToSet(type, ALL_SPEC_INT, -1, Integer.valueOf(obls[1]));
						}
						else
						{
							addToSet(type, Integer.valueOf(obls[0]), -1, Integer.valueOf(obls[1]));
						}
					}
				}
				else
				{
					// '/'号分割
					obls = oblus.split(mins[1]);
					if (obls.length == 1)
					{
						// '*-xx'格式效果同'*'
						if (mins[0].equals("*"))
						{
							addToSet(type, ALL_SPEC_INT, -1, 0);
						}
						else
						{
							addToSet(type, Integer.valueOf(mins[0]), Integer.valueOf(obls[0]), 1);
						}
					}
					else
					{
						// '*-xx/xx'格式效果同'*/xx'
						if (mins[0].equals("*")) {
							addToSet(type, ALL_SPEC_INT, -1, Integer.valueOf(obls[1]));
						}
						else
						{
							addToSet(type, Integer.valueOf(mins[0]), Integer.valueOf(obls[0]), Integer.valueOf(obls[1]));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new EBusiness("错误的定时任务表达式:" + e.getMessage());
		}
	}
	
	/**
	 * 添加打Set集中
	 */
	private void addToSet(int type, int val, int end, int incr)
			throws Exception {
		TreeSet<Integer> set = null;
		switch (type) {
		case SECOND:
			if (m_seconds == null) {
				m_seconds = new TreeSet<Integer>();
			}
			set = m_seconds;
			break;
		case MINUTE:
			if (m_minutes == null) {
				m_minutes = new TreeSet<Integer>();
			}
			set = m_minutes;
			break;
		case HOUR:
			if (m_hours == null) {
				m_hours = new TreeSet<Integer>();
			}
			set = m_hours;
			break;
		case DAY_OF_MONTH:
			if (m_daysOfMonth == null) {
				m_daysOfMonth = new TreeSet<Integer>();
			}
			set = m_daysOfMonth;
			break;
		case MONTH:
			if (m_months == null) {
				m_months = new TreeSet<Integer>();
			}
			set = m_months;
			break;
		case DAY_OF_WEEK:
			if (m_daysOfWeek == null) {
				m_daysOfWeek = new TreeSet<Integer>();
			}
			set = m_daysOfWeek;
			break;
		default:
			throw new Exception("错误的域类型:" + type);
		}

		if (type == SECOND || type == MINUTE) {
			if ((val < 0 || val > 59 || end > 59) && (val != ALL_SPEC_INT)) {
				throw new Exception("秒和分的值必须在0-59之间");
			}
		} else if (type == HOUR) {
			if ((val < 0 || val > 23 || end > 23) && (val != ALL_SPEC_INT)) {
				throw new Exception("时(天)的值必须在0-23之间");
			}
		} else if (type == DAY_OF_MONTH) {
			if ((val < 1 || val > 31 || end > 31) && (val != ALL_SPEC_INT)) {
				throw new Exception("天(月)的值必须在1-31之间");
			}
		} else if (type == MONTH) {
			if ((val < 1 || val > 12 || end > 12) && (val != ALL_SPEC_INT)) {
				throw new Exception("月份的值必须在 1-12之间");
			}
		} else if (type == DAY_OF_WEEK) {
			if ((val < 0 || val > 6 || end > 6) && (val != ALL_SPEC_INT)) {
				throw new Exception("天(周)的值必须在 0-6之间, 0代表周日");
			}
		}

		if ((incr == 0 || incr == -1) && val != ALL_SPEC_INT) {
			m_logger.debug("Add["
					+ (type == SECOND ? "秒" : (type == MINUTE ? "分" : (type == HOUR ? "时"
					: (type == DAY_OF_MONTH ? "日" : (type == MONTH ? "月" : "周"))))) + "]为:"
					+ val);
			
			set.add(val);
			return;
		}

		int startAt = val;
		int stopAt = end;

		if (val == ALL_SPEC_INT && incr <= 0) {
			incr = 1;
			
			m_logger.debug("Add["
					+ (type == SECOND ? "秒" : (type == MINUTE ? "分" : (type == HOUR ? "时"
					: (type == DAY_OF_MONTH ? "日" : (type == MONTH ? "月" : "周"))))) + "]为:"
					+ val);
			
			set.add(ALL_SPEC);
		}

		if (type == SECOND || type == MINUTE) {
			if (stopAt == -1) {
				stopAt = 59;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 0;
			}
		} else if (type == HOUR) {
			if (stopAt == -1) {
				stopAt = 23;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 0;
			}
		} else if (type == DAY_OF_MONTH) {
			if (stopAt == -1) {
				stopAt = 31;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 1;
			}
		} else if (type == MONTH) {
			if (stopAt == -1) {
				stopAt = 12;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 1;
			}
		} else if (type == DAY_OF_WEEK) {
			if (stopAt == -1) {
				stopAt = 6;
			}
			if (startAt == -1 || startAt == ALL_SPEC_INT) {
				startAt = 0;
			}
		}

		int max = -1;
		if (stopAt < startAt) {
			switch (type) {
			case SECOND:
				max = 60;
				break;
			case MINUTE:
				max = 60;
				break;
			case HOUR:
				max = 24;
				break;
			case DAY_OF_MONTH:
				max = 31;
				break;
			case MONTH:
				max = 12;
				break;
			case DAY_OF_WEEK:
				max = 7;
				break;
			default:
				throw new Exception("错误的域类型:" + type);
			}
			stopAt += max;
		}

		//获取各个域的值
		for (int i = startAt; i <= stopAt; i += incr) {
			if (max == -1) {
				m_logger.debug("Add["
						+ (type == SECOND ? "秒" : (type == MINUTE ? "分" : (type == HOUR ? "时"
						: (type == DAY_OF_MONTH ? "日" : (type == MONTH ? "月" : "周"))))) + "]为:"
						+ i);
				
				set.add(i);
			} else {
				int i2 = i % max;
				if (i2 == 0 && (type == MONTH || type == DAY_OF_WEEK || type == DAY_OF_MONTH)) {
					i2 = max;
				}
				
				m_logger.debug("Add["
						+ (type == SECOND ? "秒" : (type == MINUTE ? "分" : (type == HOUR ? "时"
						: (type == DAY_OF_MONTH ? "日" : (type == MONTH ? "月" : "周"))))) + "]为:"
						+ i2);
				
				set.add(i2);
			}
		}
	}
	
	/**
	 * 设置定时任务每天的开始时间
	 * @param startTime 定时任务每天的开始时间,格式HH:mm:ss
	 */
	public void setStartTime(String startTime) {
		if (startTime != null && startTime.trim().length() > 0)
		{
			String[] result = colon.split(startTime);
			if (result.length != 3)
			{
				throw new EBusiness("输入的开始时间[" + startTime + "]格式不正确, 正确格式为HH:mm:ss");
			}
			
			//校验时间格式
			int hour   = Integer.valueOf(result[0]);
			int minute = Integer.valueOf(result[1]);
			int second = Integer.valueOf(result[2]);
			if (hour < 0 || hour > 23)
			{
				throw new EBusiness("输入的开始时间[" + startTime + "]的小时数必须在0-23之间");
			}
			
			if (minute < 0 || minute > 59)
			{
				throw new EBusiness("输入的开始时间[" + startTime + "]的分钟数必须在0-59之间");
			}
			
			if (second < 0 || second > 59)
			{
				throw new EBusiness("输入的开始时间[" + startTime + "]的秒数必须在0-59之间");
			}
			
			try
			{
				Date date = fmtTime.parse(startTime);
				if (m_startTime == null) {
					m_startTime = Calendar.getInstance();
				}
				m_startTime.setTime(date);
				m_startTime.set(Calendar.YEAR, 1);
				m_startTime.set(Calendar.MONTH, Calendar.JANUARY);
				m_startTime.set(Calendar.DAY_OF_MONTH, 1);
				m_startTime.set(Calendar.MILLISECOND, 0);
			}
			catch (ParseException e)
			{
				throw new EBusiness("起始时间[" + startTime + "]格式不正确,正确格式应该是HH:mm:ss");
			}
		}
		else
		{
			m_startTime = null;
		}

		//计算开始时间
		if (m_startDate != null && m_startTime != null)
		{
			Calendar startdate = Calendar.getInstance();
			startdate.set(Calendar.YEAR, m_startDate.get(Calendar.YEAR));
			startdate.set(Calendar.MONTH, m_startDate.get(Calendar.MONTH));
			startdate.set(Calendar.DAY_OF_MONTH, m_startDate.get(Calendar.DAY_OF_MONTH));
			startdate.set(Calendar.HOUR_OF_DAY, m_startTime.get(Calendar.HOUR_OF_DAY));
			startdate.set(Calendar.MINUTE, m_startTime.get(Calendar.MINUTE));
			startdate.set(Calendar.SECOND, m_startTime.get(Calendar.SECOND));
			startdate.set(Calendar.MILLISECOND, m_startTime.get(Calendar.MILLISECOND));
			if (m_startDate.before(startdate)) {
				m_startDate = startdate;
			}
		}
	}
	
	/**
	 * 获取定时任务每天的开始时间
	 * @return 定时任务每天的开始时间
	 */
	public String getStartTime() {
		if (m_startTime == null) {
			return null;
		}
		
		return fmtTime.format(m_startTime.getTime());
	}
	
	/**
	 * 设置定时任务每天的结束时间
	 * @param endTime 定时任务每天的结束时间,格式HH:mm:ss
	 */
	public void setEndTime(String endTime) {
		if (endTime != null && endTime.trim().length() > 0)
		{
			String[] result = colon.split(endTime);
			if (result.length != 3)
			{
				throw new EBusiness("输入的结束时间[" + endTime + "]格式不正确, 正确格式为HH:mm:ss");
			}
			
			//校验时间格式
			int hour   = Integer.valueOf(result[0]);
			int minute = Integer.valueOf(result[1]);
			int second = Integer.valueOf(result[2]);
			if (hour < 0 || hour > 23)
			{
				throw new EBusiness("输入的结束时间[" + endTime + "]的小时数必须在0-23之间");
			}
			
			if (minute < 0 || minute > 59)
			{
				throw new EBusiness("输入的结束时间[" + endTime + "]的分钟数必须在0-59之间");
			}
			
			if (second < 0 || second > 59)
			{
				throw new EBusiness("输入的结束时间[" + endTime + "]的秒数必须在0-59之间");
			}
			
			try
			{
				Date date = fmtTime.parse(endTime);
				if (m_endTime == null) {
					m_endTime = Calendar.getInstance();
				}
				m_endTime.setTime(date);
				m_endTime.set(Calendar.YEAR, 1);
				m_endTime.set(Calendar.MONTH, Calendar.JANUARY);
				m_endTime.set(Calendar.DAY_OF_MONTH, 1);
				m_endTime.set(Calendar.MILLISECOND, 0);
			}
			catch (ParseException e)
			{
				throw new EBusiness("终止时间[" + endTime + "]格式不正确,正确格式应该是HH:mm:ss");
			}
		}
		else
		{
			m_endTime = null;
		}
		
		//计算结束时间
		if (m_endDate != null && m_endTime != null)
		{
			Calendar enddate = Calendar.getInstance();
			enddate.set(Calendar.YEAR, m_endDate.get(Calendar.YEAR));
			enddate.set(Calendar.MONTH, m_endDate.get(Calendar.MONTH));
			enddate.set(Calendar.DAY_OF_MONTH, m_endDate.get(Calendar.DAY_OF_MONTH));
			enddate.set(Calendar.HOUR_OF_DAY, m_endTime.get(Calendar.HOUR_OF_DAY));
			enddate.set(Calendar.MINUTE, m_endTime.get(Calendar.MINUTE));
			enddate.set(Calendar.SECOND, m_endTime.get(Calendar.SECOND));
			enddate.set(Calendar.MILLISECOND, m_endTime.get(Calendar.MILLISECOND));
			if (m_endDate.after(enddate)) {
				m_endDate = enddate;
			}
		}
	}
	
	/**
	 * 获取定时任务每天的结束时间
	 * @return 定时任务每天的结束时间
	 */
	public String getEndTime() {
		if (m_endTime == null) {
			return null;
		}
		
		return fmtTime.format(m_endTime.getTime());
	}
	
	/**
	 * 设置定时任务的开始日期
	 * @param startDate 定时任务的开始日期,格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 */
	public void setStartDate(String startDate) {
		if (startDate != null && startDate.trim().length() > 0)
		{
			if (startDate.trim().length() <= 10) {
				startDate = startDate.trim() + " 00:00:00";
			}
			
			try
			{
				Date date = fmtAll.parse(startDate);
				if (m_startDate == null) {
					m_startDate = Calendar.getInstance();
				}
				m_startDate.setTime(date);
				m_startDate.set(Calendar.MILLISECOND, 0);
			}
			catch (ParseException e)
			{
				throw new EBusiness("开始日期[" + startDate + "]格式不正确,正确格式应该是yyyy-MM-dd或yyyy-MM-dd HH:mm:ss");
			}
		}
		else
		{
			m_startDate = null;
		}
		
		//计算开始时间
		if (m_startDate != null && m_startTime != null)
		{
			Calendar startdate = Calendar.getInstance();
			startdate.set(Calendar.YEAR, m_startDate.get(Calendar.YEAR));
			startdate.set(Calendar.MONTH, m_startDate.get(Calendar.MONTH));
			startdate.set(Calendar.DAY_OF_MONTH, m_startDate.get(Calendar.DAY_OF_MONTH));
			startdate.set(Calendar.HOUR_OF_DAY, m_startTime.get(Calendar.HOUR_OF_DAY));
			startdate.set(Calendar.MINUTE, m_startTime.get(Calendar.MINUTE));
			startdate.set(Calendar.SECOND, m_startTime.get(Calendar.SECOND));
			startdate.set(Calendar.MILLISECOND, m_startTime.get(Calendar.MILLISECOND));
			if (m_startDate.before(startdate)) {
				m_startDate = startdate;
			}
		}
	}
	
	/**
	 * 获取定时任务的开始日期
	 * @return 定时任务的开始日期yyyy-MM-dd HH:mm:ss
	 */
	public String getStartDate() {
		if (m_startDate == null) {
			return null;
		}
		
		return fmtAll.format(m_startDate.getTime());
	}
	
	/**
	 * 设置定时任务的结束日期
	 * @param endDate 定时任务的结束日期,格式yyyy-MM-dd或yyyy-MM-dd HH:mm:ss
	 */
	public void setEndDate(String endDate) {
		if (endDate != null && endDate.trim().length() > 0)
		{
			if (endDate.trim().length() <= 10) {
				endDate = endDate.trim() + " 23:59:59";
			}
			
			try
			{
				Date date = fmtAll.parse(endDate);
				if (m_endDate == null) {
					m_endDate = Calendar.getInstance();
				}
				m_endDate.setTime(date);
				m_endDate.set(Calendar.MILLISECOND, 999);
			}
			catch (ParseException e)
			{
				throw new EBusiness("终止时间[" + endDate + "]格式不正确,正确格式应该是yyyy-MM-dd或yyyy-MM-dd HH:mm:ss");
			}
		}
		else
		{
			m_endDate = null;
		}
		
		//计算结束时间
		if (m_endDate != null && m_endTime != null)
		{
			Calendar enddate = Calendar.getInstance();
			enddate.set(Calendar.YEAR, m_endDate.get(Calendar.YEAR));
			enddate.set(Calendar.MONTH, m_endDate.get(Calendar.MONTH));
			enddate.set(Calendar.DAY_OF_MONTH, m_endDate.get(Calendar.DAY_OF_MONTH));
			enddate.set(Calendar.HOUR_OF_DAY, m_endTime.get(Calendar.HOUR_OF_DAY));
			enddate.set(Calendar.MINUTE, m_endTime.get(Calendar.MINUTE));
			enddate.set(Calendar.SECOND, m_endTime.get(Calendar.SECOND));
			enddate.set(Calendar.MILLISECOND, m_endTime.get(Calendar.MILLISECOND));
			if (m_endDate.after(enddate)) {
				m_endDate = enddate;
			}
		}
	}
	
	/**
	 * 获取定时任务的结束日期
	 * @return 定时任务的结束日期yyyy-MM-dd HH:mm:ss
	 */
	public String getEndDate() {
		if (m_endDate == null) {
			return null;
		}
		
		return fmtAll.format(m_endDate.getTime());
	}
	
	/**
	 * 设置执行时间延迟秒数(即执行时间需要+delaySec秒,默认为0)
	 * @param delaySec 执行时间延迟秒数
	 */
	public void setDelaySec(int delaySec) {
		m_delaySec = delaySec;
	}
	
	/**
	 * 获取执行时间延迟秒数
	 * @return 执行时间延迟秒数
	 */
	public int getDelaySec() {
		return m_delaySec;
	}
	
	/**
	 * 以当前时间为基准计算下次执行时间
	 * @return 定时任务下次执行时间
	 */
	public Date calcNextTime() {
		return calcNextTime(null);
	}
	
	/**
	 * 计算定时任务在calcTime后的下次执行时间
	 * @param calcTime 计算基准时间
	 * @return 定时任务下次执行时间
	 * @throws Exception 
	 */
	public Date calcNextTime(Date calcTime) {
		//如果为空则用系统当前时间
		if (calcTime == null) {
			calcTime = new Date();
		}
		
		//下次执行时间
		Calendar nextTime = null;
		//获取计算基准时间
		Calendar afterTime = Calendar.getInstance();
		afterTime.setTime(calcTime);
		//当前的时间(HH:mm:ss)
		Calendar time = (Calendar) afterTime.clone();
		time.set(Calendar.YEAR, 1);
		time.set(Calendar.MONTH, Calendar.JANUARY);
		time.set(Calendar.DAY_OF_MONTH, 1);
		time.set(Calendar.MILLISECOND, 0);
		
		//判断开始日期和结束日期有效性
		if (m_startDate != null && m_endDate != null && m_startDate.after(m_endDate))
		{
			m_logger.warn("定时任务表达式无效:开始日期晚于结束日期");
			return null;
		}
		
		//如果当前日期早于开始日期
		if (m_startDate != null && afterTime.before(m_startDate))
		{
			afterTime.setTime(new Date(m_startDate.getTimeInMillis() - 1000));
		}
		
		try
		{
			//如果下一次执行时间为空或者在afterTime之前则重新计算下次执行时间
			while (nextTime == null || afterTime.after(nextTime))
			{
				//计算下次执行时间
				nextTime = getNextTime(afterTime.getTime());

				//如果计算的下次执行时间为空或者下次执行时间晚于结束日期则返回空
				if (nextTime == null || (m_endDate != null && nextTime.after(m_endDate)))
				{
					nextTime = null;
					break;
				}
				
				//获取下次执行时间(HH:mm:ss)
				time.set(Calendar.HOUR_OF_DAY, nextTime.get(Calendar.HOUR_OF_DAY));
				time.set(Calendar.MINUTE, nextTime.get(Calendar.MINUTE));
				time.set(Calendar.SECOND, nextTime.get(Calendar.SECOND));
				time.set(Calendar.MILLISECOND, 0);
				
				//1.开始时间非空结束时间为空且下次执行时间早于开始时间
				//2.开始时间非空结束时间非空且非跨天情况下,下次执行时间早于开始时间
				//3.开始时间非空结束时间非空且在跨天情况下,下次执行时间早于开始时间晚于结束时间
				if ((m_startTime != null && m_endTime == null && time.before(m_startTime))
				 || (m_startTime != null && m_endTime != null && m_startTime.before(m_endTime) && time.before(m_startTime))
				 || (m_startTime != null && m_endTime != null &&!m_startTime.before(m_endTime) && time.before(m_startTime) && time.after(m_endTime)))
				{
					afterTime.set(Calendar.YEAR, nextTime.get(Calendar.YEAR));
					afterTime.set(Calendar.MONTH, nextTime.get(Calendar.MONTH));
					afterTime.set(Calendar.DAY_OF_MONTH, nextTime.get(Calendar.DAY_OF_MONTH));
					afterTime.set(Calendar.HOUR_OF_DAY, (m_startTime == null ? 0 : m_startTime.get(Calendar.HOUR_OF_DAY)));
					afterTime.set(Calendar.MINUTE, (m_startTime == null ? 0 : m_startTime.get(Calendar.MINUTE)));
					afterTime.set(Calendar.SECOND, (m_startTime == null ? 0 : m_startTime.get(Calendar.SECOND)));
					afterTime.setTime(new Date(afterTime.getTimeInMillis() - 1000));
					continue;
				}
				
				//1.开始时间为空结束时间非空且下次执行时间晚于结束时间
				//2.开始时间非空结束时间非空且非跨天情况下，下次执行时间晚于结束时间
				if ((m_startTime == null && m_endTime != null && time.after(m_endTime))
				 || (m_startTime != null && m_endTime != null && m_startTime.before(m_endTime) && time.after(m_endTime)))
				{
					afterTime.set(Calendar.HOUR_OF_DAY, (m_startTime == null ? 0 : m_startTime.get(Calendar.HOUR_OF_DAY)));
					afterTime.set(Calendar.MINUTE, (m_startTime == null ? 0 : m_startTime.get(Calendar.MINUTE)));
					afterTime.set(Calendar.SECOND, (m_startTime == null ? 0 : m_startTime.get(Calendar.SECOND)));
					afterTime.setTime(new Date(afterTime.getTimeInMillis() + 24*60*60*1000 - 1000));
					continue;
				}
			}
		}
		catch (Exception e)
		{
			nextTime = null;
			m_logger.error("下次执行时间计算失败:" + e.getMessage());
		}
		
		//如果为空则返回空
		if (nextTime == null) {
			return null;
		}
		
		//执行时间增加m_delaySec秒
		if (m_delaySec > 0)
		{
			nextTime.setTime(new Date(nextTime.getTimeInMillis() + m_delaySec * 1000));
		}
		
		return nextTime.getTime();
	}
	
	/**
	 * 获取没有延迟的时间
	 * @param calcTime
	 * @return
	 */
	public Date getTimeNoDelay(Date calcTime) {
		if (calcTime == null) {
			return null;
		}
		if (m_delaySec > 0) {
			return new Date(calcTime.getTime() - m_delaySec * 1000);
		}
		return calcTime;
	}
	
	/**
	 * 获取定时任务在afterTime后的下次执行时间
	 * @param afterTime 计算基准时间
	 * @return 定时任务下次执行时间
	 */
	private Calendar getNextTime(Date afterTime) {
		if (afterTime == null) {
			throw new EBusiness("计算基准时间不允许为空");
		}
		
		// 秒进1,毫秒置0
		Calendar nextTime = Calendar.getInstance();
		nextTime.setTime(new Date(afterTime.getTime() + 1000));
		nextTime.set(Calendar.MILLISECOND, 0);

		// 秒 分 时 日 月 周
		if (m_timeOfDay == null && m_frequency == null)
		{
			while (true) {
				if (nextTime.get(Calendar.YEAR) > MAX_YEAR) {
					return null;
				}
	
				int sec = nextTime.get(Calendar.SECOND);
				int min = nextTime.get(Calendar.MINUTE);
				// 获取秒
				SortedSet<Integer> st = m_seconds.tailSet(sec);
				if (st != null && st.size() != 0) {
					sec = st.first();
				} else {
					sec = m_seconds.first();
					min++;
					nextTime.set(Calendar.MINUTE, min);
				}
				nextTime.set(Calendar.SECOND, sec);
	
				min = nextTime.get(Calendar.MINUTE);
				int hour = nextTime.get(Calendar.HOUR_OF_DAY);
				int tmp = -1;
				// 获取分
				st = m_minutes.tailSet(min);
				if (st != null && st.size() != 0) {
					tmp = min;
					min = st.first();
				} else {
					min = m_minutes.first();
					hour++;
				}
				if (min != tmp) {
					nextTime.set(Calendar.SECOND, 0);
					nextTime.set(Calendar.MINUTE, min);
					nextTime.set(Calendar.HOUR_OF_DAY, hour);
					if (nextTime.get(Calendar.HOUR_OF_DAY) != hour && hour != 24) {
						nextTime.set(Calendar.HOUR_OF_DAY, hour + 1);
					}
					continue;
				}
				nextTime.set(Calendar.MINUTE, min);
	
				hour = nextTime.get(Calendar.HOUR_OF_DAY);
				int day = nextTime.get(Calendar.DAY_OF_MONTH);
				tmp = -1;
				// 获取时
				st = m_hours.tailSet(hour);
				if (st != null && st.size() != 0) {
					tmp = hour;
					hour = st.first();
				} else {
					hour = m_hours.first();
					day++;
				}
				if (hour != tmp) {
					nextTime.set(Calendar.SECOND, 0);
					nextTime.set(Calendar.MINUTE, 0);
					nextTime.set(Calendar.HOUR_OF_DAY, hour);
					if (nextTime.get(Calendar.HOUR_OF_DAY) != hour && hour != 24) {
						nextTime.set(Calendar.HOUR_OF_DAY, hour + 1);
					}
					nextTime.set(Calendar.DAY_OF_MONTH, day);
					continue;
				}
				nextTime.set(Calendar.HOUR_OF_DAY, hour);
	
				day = nextTime.get(Calendar.DAY_OF_MONTH);
				int mon = nextTime.get(Calendar.MONTH) + 1;
				int tmon = mon;
				tmp = -1;
				// 获取天
				boolean dayOfMSpec = m_daysOfMonth.contains(ALL_SPEC);
				boolean dayOfWSpec = m_daysOfWeek.contains(ALL_SPEC);
				// 获取天(周)
				if (dayOfMSpec && !dayOfWSpec) {
					int cDow = nextTime.get(Calendar.DAY_OF_WEEK) - 1;
					int dow = m_daysOfWeek.first();
					st = m_daysOfWeek.tailSet(cDow);
					if (st != null && st.size() > 0) {
						dow = st.first();
					}
	
					int daysToAdd = 0;
					if (cDow < dow) {
						daysToAdd = dow - cDow;
					}
					if (cDow > dow) {
						daysToAdd = dow + (7 - cDow);
					}
	
					int lDay = getLastDayOfMonth(mon, nextTime.get(Calendar.YEAR));
					if (day + daysToAdd > lDay) {
						nextTime.set(Calendar.SECOND, 0);
						nextTime.set(Calendar.MINUTE, 0);
						nextTime.set(Calendar.HOUR_OF_DAY, 0);
						nextTime.set(Calendar.DAY_OF_MONTH, 1);
						nextTime.set(Calendar.MONTH, mon);
						continue;
					} else if (daysToAdd > 0) {
						nextTime.set(Calendar.SECOND, 0);
						nextTime.set(Calendar.MINUTE, 0);
						nextTime.set(Calendar.HOUR_OF_DAY, 0);
						nextTime.set(Calendar.DAY_OF_MONTH, day + daysToAdd);
						nextTime.set(Calendar.MONTH, mon - 1);
						continue;
					}
				}
				// 获取天(月)
				else {
					st = m_daysOfMonth.tailSet(day);
					if (st != null && st.size() != 0) {
						tmp = day;
						day = st.first();
						int lastDay = getLastDayOfMonth(mon, nextTime.get(Calendar.YEAR));
						if (day > lastDay) {
							day = m_daysOfMonth.first();
							mon++;
						}
					} else {
						day = m_daysOfMonth.first();
						mon++;
					}
					if (day != tmp || mon != tmon) {
						nextTime.set(Calendar.SECOND, 0);
						nextTime.set(Calendar.MINUTE, 0);
						nextTime.set(Calendar.HOUR_OF_DAY, 0);
						nextTime.set(Calendar.DAY_OF_MONTH, day);
						nextTime.set(Calendar.MONTH, mon - 1);
						continue;
					}
				}
				nextTime.set(Calendar.DAY_OF_MONTH, day);
	
				mon = nextTime.get(Calendar.MONTH) + 1;
				int year = nextTime.get(Calendar.YEAR);
				tmp = -1;
				// 获取月
				st = m_months.tailSet(mon);
				if (st != null && st.size() != 0) {
					tmp = mon;
					mon = st.first();
				} else {
					mon = m_months.first();
					year++;
				}
				if (mon != tmp) {
					nextTime.set(Calendar.SECOND, 0);
					nextTime.set(Calendar.MINUTE, 0);
					nextTime.set(Calendar.HOUR_OF_DAY, 0);
					nextTime.set(Calendar.DAY_OF_MONTH, 1);
					nextTime.set(Calendar.MONTH, mon - 1);
					nextTime.set(Calendar.YEAR, year);
					continue;
				}
				nextTime.set(Calendar.MONTH, mon - 1);
				nextTime.set(Calendar.YEAR, year);
				// 计算完成
				break;
			}
		}
		// 固定时间串
		else
		{
			Calendar time = Calendar.getInstance();
			time.setTime(nextTime.getTime());
			time.set(Calendar.YEAR, 1);
			time.set(Calendar.MONTH, Calendar.JANUARY);
			time.set(Calendar.DAY_OF_MONTH, 1);
			
			if (m_timeOfDay != null)
			{
				if (nextTime.get(Calendar.YEAR) > MAX_YEAR) {
					return null;
				}
				
				SortedSet<Calendar> st = m_timeOfDay.tailSet(time);
				Calendar next = null;
				if (st != null && st.size() != 0) {
					next = st.first();
					nextTime.set(Calendar.HOUR_OF_DAY, next.get(Calendar.HOUR_OF_DAY));
					nextTime.set(Calendar.MINUTE, next.get(Calendar.MINUTE));
					nextTime.set(Calendar.SECOND, next.get(Calendar.SECOND));
				} else {
					next = m_timeOfDay.first();
					nextTime.set(Calendar.HOUR_OF_DAY, next.get(Calendar.HOUR_OF_DAY));
					nextTime.set(Calendar.MINUTE, next.get(Calendar.MINUTE));
					nextTime.set(Calendar.SECOND, next.get(Calendar.SECOND));
					nextTime.setTime(new Date(nextTime.getTimeInMillis() + 24*60*60*1000));
				}
			}
			else
			{
				Calendar startTime = null;
				if (m_startTime != null)
				{
					startTime = (Calendar) m_startTime.clone();
				}
				else
				{
					startTime = Calendar.getInstance();
					startTime.set(Calendar.YEAR, 1);
					startTime.set(Calendar.MONTH, Calendar.JANUARY);
					startTime.set(Calendar.DAY_OF_MONTH, 1);
					startTime.set(Calendar.HOUR_OF_DAY, 0);
					startTime.set(Calendar.MINUTE, 0);
					startTime.set(Calendar.SECOND, 0);
					startTime.set(Calendar.MILLISECOND, 0);
				}
				
				//计算与下次执行时间相差的毫秒数
				long dtime = (time.get(Calendar.HOUR_OF_DAY) * 60 * 60 + time.get(Calendar.MINUTE) * 60 + time.get(Calendar.SECOND) - (startTime.get(Calendar.HOUR_OF_DAY) * 60 * 60 + startTime.get(Calendar.MINUTE) * 60 + startTime.get(Calendar.SECOND))) * 1000 % (m_frequency * 1000);
				long ltime = 0;
				if (dtime != 0) {
					ltime = dtime > 0 ? (m_frequency * 1000 - dtime) : -1 * dtime;
				}
				nextTime.setTime(new Date(nextTime.getTimeInMillis() + ltime));
			}
		}

		return nextTime;
	}
	
	/**
	 * 获取每个月的最后一天
	 */
	private int getLastDayOfMonth(int month, int year) {
		switch (month) {
		case 1:
			return 31;
		case 2:
			return ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) ? 29 : 28;
		case 3:
			return 31;
		case 4:
			return 30;
		case 5:
			return 31;
		case 6:
			return 30;
		case 7:
			return 31;
		case 8:
			return 31;
		case 9:
			return 30;
		case 10:
			return 31;
		case 11:
			return 30;
		case 12:
			return 31;
		default:
			throw new EBusiness("错误的月份数字:" + month);
		}
	}

	/**
	 * 转换成字符串
	 */
	public String toString() {
		return "crontab:\"" + m_crontab + "\","
	         + "startDate:\"" + (m_startDate == null ? "" : fmtAll.format(m_startDate.getTime())) + "\","
	         + "endDate:\"" + (m_endDate == null ? "" : fmtAll.format(m_endDate.getTime())) + "\","
	         + "startTime:\"" + (m_startTime == null ? "" : fmtTime.format(m_startTime.getTime())) + "\","
	         + "endTime:\"" + (m_endTime == null ? "" : fmtTime.format(m_endTime.getTime())) + "\","
	         + "delaySec:\"" + m_delaySec + "\"";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			SimpleDateFormat fmtAll = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Crontab contab = new Crontab("9:30:00,9:50:00,19:30:00", "9:30:00", "20:00:00"); //
			m_logger.info(fmtAll.format(contab.calcNextTime()));
			
			contab.setCrontab("0 0 9-19 * * *");
			m_logger.info(fmtAll.format(contab.calcNextTime()));
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, 2015);
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.DAY_OF_MONTH, 31);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			m_logger.info(fmtAll.format(contab.calcNextTime(cal.getTime()).getTime()));
			
			contab.setCrontab("0-30/4,30-55/4 * * * * *");
			contab.setStartTime("09:00:00");
			contab.setEndTime("22:00:00");
			contab.setStartDate("2015-04-22");
			contab.setEndDate("2015-04-24");
			m_logger.info(fmtAll.format(contab.calcNextTime()));
			
			contab.setCrontab("30");
			m_logger.info(fmtAll.format(contab.calcNextTime()));
		}
		catch (Exception e)
		{
			m_logger.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
