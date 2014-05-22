package autodriver.util;

public interface ExecuteBlock {
	public final static int StepResultSuccess = 0;
	public final static int StepResultFail = 1;
	public final static int StepResultWait = 2;
	public int excute(Object... objects);
}
