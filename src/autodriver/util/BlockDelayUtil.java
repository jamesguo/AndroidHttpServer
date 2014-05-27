package autodriver.util;

public class BlockDelayUtil {
	public static void runBlock(ExecuteBlock executeBlock, float timeout, Object... objects) {
		long starTime = System.currentTimeMillis();
		while (executeBlock.excute(objects) == ExecuteBlock.StepResultWait && (System.currentTimeMillis() - starTime) < timeout * 1000) {
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static void waitforTime(float timeout) {
		runBlock(new ExecuteBlock() {

			@Override
			public int excute(Object... objects) {
				return ExecuteBlock.StepResultWait;
			}
		}, timeout);
	}
}
