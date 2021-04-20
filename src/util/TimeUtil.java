package util;

public class TimeUtil {
	public static int minToSec(String time) {
		String[] nums = time.split("'");
		int secs = Integer.parseInt(nums[0]) * 60;
		
		if (nums.length >= 2) {
			secs += Integer.parseInt(nums[1]);
		}
		
		return secs;
	}
}