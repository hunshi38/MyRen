package io.renren.modules.generator.util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共方法
 * 
 * @author love_yu
 *
 */
public class CommonUtil {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return {@link Boolean}
	 */
	public static boolean isEmpty(String str) {
		if (str != null && !str.equals("") && str.trim().length() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断数字是否为空,值是否大于0
	 * 
	 * @param str
	 * @return {@link Boolean}
	 */
	public static boolean isEmpty(Long str) {
		if (str != null && str > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断数字是否为空,值是否大于0
	 * 
	 * @param str
	 * @return {@link Boolean}
	 */
	public static boolean isEmpty(Integer str) {
		if (str != null && str > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 判断list是否为空
	 * 
	 * @param list
	 * @return {@link Boolean}
	 */
	@SuppressWarnings({ "rawtypes" })
	public static boolean isEmpty(List list) {
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(Double str) {
		if (str != null && str > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 生成随机数字和字母,
	 * 
	 * @param length
	 *            length 生成的字符串长度
	 * @return
	 */
	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}

	/**
	 * 对手机号码的中间4位打码
	 * 
	 * @param phone
	 * @return
	 */
	public static String get4StarPhone(String phone) {
		if (phone != null && !"".equals(phone) && phone.length() == 11) {
			return phone.substring(0, 3) + "****" + phone.substring(7);
		} else {
			return phone;
		}
	}

	/**
	 * 格式化数字，若数字长度不够，向左补0 数字补位 1 ---->01or001....
	 * 
	 * @param number
	 *            需格式化的数字
	 * @param digit
	 *            格式化后最小长度
	 * @return
	 */
	public static String getNumberFormat(int number, int digit) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(digit);
		formatter.setGroupingUsed(false);
		return formatter.format(number);
	}

	/**
	 * 获得日期+随机数 yyyyMMdd+Random(5)
	 * 
	 * @return
	 */
	public static String getRandom() {
		SimpleDateFormat simpleDateFormat;
		simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String str = simpleDateFormat.format(date);
		Random random = new Random();
		int rannum = (int) (random.nextDouble() * (99999));// 获取5位随机数
		String randomNum = getNumberFormat(rannum, 5);
		return str + randomNum;// 当前时间
	}

	/**
	 * 判断数组中是否存在该元素
	 * 
	 * @param array
	 *            目标数组
	 * @param v
	 *            目标元素
	 * @return
	 */
	public static <T> boolean contains(final T[] array, final T v) {
		for (final T e : array)
			if ((v != null && v.equals(e)) || e == v)
				return true;

		return false;
	}

	/**
	 * 字符串转List<Integer>
	 * 
	 * @param a
	 *            格式:1，2，3，4，556，2
	 * @return
	 */
	public static List<Integer> stringToIntegers(String a) {
		String[] str = a.split(",");
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < str.length; i++) {
			list.add(Integer.valueOf(str[i].trim()));
		}
		return list;
	}

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

}
