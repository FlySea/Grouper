package cn.com.libbasic.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * String工具类
 * 
 * 
 */
public class StringUtil {

    static final String TAG = "StringUtil";
    public static String PASSWORD = "^[0-9a-zA-Z@#%*&$';<>+-.,_]{6,24}$";

    public static String TELFORMAT = "^[0-9-]{7,20}$";

    /**
     * 验证是否是正确的邮箱格式
     * 
     * @param email
     * @return true表示是正确的邮箱格式,false表示不是正确邮箱格式
     */
    public static boolean isEmail(String email) {
        // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线、点、中横线
        String regular = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (email != null) {
            Matcher matcher = pattern.matcher(email);
            flag = matcher.matches();
        }
        return flag;
    }

    /**
     * 座机电话校验
     * 
     * @param tel
     * @return
     */
    public static boolean isRightTel(String tel) {
        Pattern pattern = Pattern.compile(TELFORMAT);
        Matcher matcher = pattern.matcher(tel);
        return matcher.matches();
    }

    /**
     * 证件编号校验
     * 
     * @param tel
     * @return
     */
    public static boolean isPcNO(String pcno) {
        String format = "^[0-9]{5,50}$";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(pcno);
        return matcher.matches();
    }

    /**
     * 是否为手机号
     * 
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("0?(13|14|15|18|17)[0-9]{9}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static String getFloat(String f, String format) {
        if (isEmpty(f)) {
            return "";
        }
        float fl = Float.valueOf(f.trim());
        java.text.DecimalFormat df2 = new DecimalFormat(format);
        return df2.format(fl);
    }

    /**
     * 验证密码是否合适
     * 
     * @param password
     * @return
     */
    public static boolean checkPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.trim().length() <= 0 || "null".equals(s);
    }

    public static int getChineseLength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 判断字符是否被包括在字符数组里
     * 
     * @param str
     * @param arry
     * @param ch
     * @return
     */
    public static boolean contiansInArray(String str, String arry, String ch) {
        if (arry != null && arry.length() > 0) {
            String[] idArry = arry.split(ch);
            if (idArry != null && idArry.length > 0) {
                for (int i = 0; i < idArry.length; i++) {
                    if (idArry[i].trim().equals(str.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 先用,分隔，再用_分隔，判断字符是否被包括在分隔后的第一个字符里
     * 
     * @param str
     * @param arry
     * @param ch
     * @return
     */
    public static boolean contiansInArray(String str, String arry) {
        if (arry != null && arry.length() > 0) {
            String[] idArry = arry.split(",");
            if (idArry != null && idArry.length > 0) {
                for (int i = 0; i < idArry.length; i++) {
                    LogUtil.d(TAG, "i=" + i + ";idArry=" + idArry[i]);
                    String inquiry = idArry[i].split("_")[0];
                    if (inquiry.trim().equals(str.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static HashMap<String, String> getMapInArray(String arry, String ch) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (arry != null && arry.length() > 0) {
            String[] idArry = arry.split(ch);
            if (idArry != null && idArry.length > 0) {
                for (int i = 0; i < idArry.length; i++) {
                    map.put(idArry[i], idArry[i]);
                }
            }
        }
        return map;
    }

    /**
     * 删除字符数组中的字符,并去掉最后一位分隔符
     * 
     * @param str
     * @param arry
     * @param ch
     * @return
     */
    public static String deleteStringInArray(String str, String arry, String ch) {
        StringBuffer sbBuffer = new StringBuffer();
        if (arry != null && arry.length() > 0) {
            String[] idArry = arry.split(ch);
            if (idArry != null && idArry.length > 0) {
                for (int i = 0; i < idArry.length; i++) {
                    if (!idArry[i].trim().equals(str.trim())) {
                        sbBuffer.append(idArry[i] + ",");
                    }
                }
            }
        }
        if (sbBuffer.length() > 0) {
            String endChar = String.valueOf(sbBuffer.charAt(sbBuffer.length() - 1));
            if (endChar != null && endChar.equals(ch)) {
                sbBuffer.deleteCharAt(sbBuffer.length() - 1);
            }
        }
        if (sbBuffer.length() > 0) {
            String beginChar = String.valueOf(sbBuffer.charAt(0));
            if (beginChar != null && beginChar.equals(ch)) {
                sbBuffer.deleteCharAt(0);
            }
        }
        return sbBuffer.toString();
    }

    /**
     * 将字符串转成MD5值
     * 
     * @param string
     * @return
     */
    public static String stringToMD5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * , 分割标签 tags
     * 
     * @param str
     * @return
     */
    public static String[] splitStrings(String str) {
        String[] strs = str.split(",");
        return strs;
    }

    public static boolean isDraw(int price) {// 提现金额
        boolean flag = false;
        if (price >= 50 && price <= 50000) {
            flag = true;
        }
        return flag;
    }

    public static boolean isRecharge(int price) {// 充值金额
        boolean flag = false;
        if (price > 0 && price <= 99999) {
            flag = true;
        }
        return flag;
    }

    public static boolean isServicePrice(int price) {// 服务价格
        boolean flag = false;
        if (price <= 9999) {
            flag = true;
        }
        return flag;
    }

    public static boolean isServiceLimit(int limit) {// 服务周期
        boolean flag = false;
        if (limit >= 2 && limit <= 999) {
            flag = true;
        }
        return flag;
    }

    public static boolean isVideoPrice(int video) {// 视频定价
        boolean flag = false;
        if (video >= 1 && video <= 9999) {
            flag = true;
        }
        return flag;
    }

    /**
     * 验证是否是 2-12个字的姓名格式
     * 
     * @param realName
     * @return
     */
    public static boolean isRealName(String realName) {
        String regular = "^[.a-zA-Z\u4E00-\u9FA5]{2,12}$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!StringUtil.isEmpty(realName)) {
            Matcher matcher = pattern.matcher(realName.trim());
            flag = matcher.matches();
        }
        return flag;
    }

    /**
     * 判断身高
     * 
     * @param heightStr
     * @return
     */
    public static boolean isHeight(String heightStr) {
        try {
            int height = Integer.parseInt(heightStr);
            if (height >= 1 && height <= 300) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断体重
     * 
     * @param heightStr
     * @return
     */
    public static boolean isWeight(String weightStr) {
        // String regular = "^[123]\\d{0,2}(\\.\\d*)?$";
        // Pattern pattern = Pattern.compile(regular);
        // boolean flag = false;
        // if (!StringUtil.isEmpty(weightStr)) {
        // Matcher matcher = pattern.matcher(weightStr);
        // flag = matcher.matches();
        // }
        // if (!flag) {
        // return false;
        // }
        try {
            float weight = Float.parseFloat(weightStr);
            if (weight >= 1 && weight <= 300) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 验证是否是emoji表情 true是不包含，false是包含
     * 
     * @param content
     * @return
     */
    public static boolean containEmoji(String nickName) {
        if (isEmpty(nickName)) {
            return true;
        }
        nickName = nickName.replace("\n", "").replace("\r", "");
        String regular = "^[0-9a-zA-Z\u0024-\uFFFF #!]+$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!StringUtil.isEmpty(nickName)) {
            Matcher matcher = pattern.matcher(nickName.trim());
            flag = matcher.matches();
        }
        return flag;
    }

    /**
     * 验证是否是 1-8个字的昵称格式
     * 
     * @param nickName
     * @return
     */
    public static boolean checkNickName(String nickName) {
        String regular = "^[0-9a-zA-Z\u4E00-\u9FA5 ]{1,8}$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!StringUtil.isEmpty(nickName)) {
            Matcher matcher = pattern.matcher(nickName);
            flag = matcher.matches();
        }
        return flag;
    }

    /**
     * 验证是否是 1-12个字的卡片名格式
     * 
     * @param nickName
     * @return
     */
    public static boolean isCardName(String nickName) {
        String regular = "^[0-9a-zA-Z\u4E00-\u9FA5]{1,12}$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!StringUtil.isEmpty(nickName)) {
            Matcher matcher = pattern.matcher(nickName);
            flag = matcher.matches();
        }
        return flag;
    }

    static int[] codeArray = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
    static Map<Integer, String> checkCodeDic = new HashMap<Integer, String>();
    static {
        checkCodeDic.put(0, "1");
        checkCodeDic.put(1, "0");
        checkCodeDic.put(2, "X");
        checkCodeDic.put(3, "9");
        checkCodeDic.put(4, "8");
        checkCodeDic.put(5, "7");
        checkCodeDic.put(6, "6");
        checkCodeDic.put(7, "5");
        checkCodeDic.put(8, "4");
        checkCodeDic.put(9, "3");
        checkCodeDic.put(10, "2");
    }

    /**
     * 验证是否是身份证号
     * 
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
        if (isEmpty(idCard) || idCard.length() != 18) {
            return false;
        }
        int sumValue = 0;
        for (int i = 0; i < 17; i++) {
            char ch = idCard.charAt(i);
            if (!Character.isDigit(ch)) {
                return false;
            }
            sumValue += Integer.valueOf("" + ch) * codeArray[i];
        }
        int checkKey = sumValue % 11;
        String checkCode = checkCodeDic.get(checkKey);
        return idCard.substring(idCard.length() - 1).toUpperCase().equals(checkCode);
    }

    /**
     * 验证是否是 6-11个数字的邀请码格式
     * 
     * @param inviteCode
     * @return
     */
    public static boolean isInviteCode(String inviteCode) {
        String regular = "^[0-9]{6,11}$";
        Pattern pattern = Pattern.compile(regular);
        boolean flag = false;
        if (!StringUtil.isEmpty(inviteCode)) {
            Matcher matcher = pattern.matcher(inviteCode);
            flag = matcher.matches();
        }
        return flag;
    }

    /**
     * 字符类型识别函数
     * 
     * @param ch
     *            如果不是汉字、字母、数字则返回false
     */
    public static boolean charDistinguish(char ch) {
        if (Character.getType(ch) == Character.OTHER_LETTER) {// 汉字
            System.out.println("chinese_leter:" + ch);
            return true;
        } else if (Character.isDigit(ch)) {// 数字
            System.out.println("digit:" + ch);
            return true;
        } else if (Character.isLetter(ch)) {// 字母
            System.out.println("letter:" + ch);
            return true;
        } else {// 其它字符
            System.out.println("others:" + ch);
            return false;
        }
    }
    
    public static String convertUnicode(String ori) {
		char aChar;
		int len = ori.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = ori.charAt(x++);
			if (aChar == '\\') {
				aChar = ori.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = ori.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);

		}
		return outBuffer.toString();

	}
    
    /**
     * 把元转成分
     */
    public static String getStringForMonery(float monery){
        DecimalFormat decimalFormat=new DecimalFormat(".00");
        return decimalFormat.format(monery);
    }

    /**
     * 判断是中文
     * 
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 把map转为json
     * @param map
     * @return
     */
    public static String mapToJson(HashMap map){
    	Gson gson = new Gson();
    	String jsonStr = gson.toJson(map);
    	return jsonStr;
    }

    /**
     * 把json转为对象
     * @param str
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T parseJsonToBean(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    /**
     * 把对象转为json
     * @param obj
     * @return
     */
    public static String parseBeanToJson(Object obj) {
        String retStr = "";
        Gson gson = new Gson();
        try {
            retStr = gson.toJson(obj);
        } catch (Exception e1) {
            Log.e(TAG, e1 + "");
        }
        return retStr;
    }

    /**
     * 判断输入值是否为空值，如null或者空格
     * 
     * @param obj
     *            要判断的对象
     * @return true/false,如为空则返回true
     */
    public static boolean isBlank(Object obj) {
        return obj == null || obj.toString().trim().length() == 0;
    }

    public static void setTextHight(Context con, TextView tv, int resid, int begin, int end) {
        Spannable span = (Spannable) tv.getText();
        span.setSpan(new ForegroundColorSpan(con.getResources().getColor(resid)), begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 
     * @param inStr
     * @return
     */
    public static String getMD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    public static String getStringRemoveHtml(String text) {
        return text.replace("<br/>", "").replace("<br />", "").replace("<p>", "").replace("</p>", "");
    }

    public static String getString(int id) {
        int tmp = id > -1 ? id : 0;
        return "" + tmp;
    }
}
