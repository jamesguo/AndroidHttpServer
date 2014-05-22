package autodriver.command;

public class AndroidActionType {
	public final static int NULL = 0x000;
	public final static int CLICK = 0x001;
	public final static int FIND = 0x002;
	public final static int SWIP = 0x003;
	public final static int SCROLLTO = 0x004;
	public final static int SETTEXT = 0x005;
	public final static int GETTEXT = 0x006;
	public final static int PRESSKEY = 0x007;
	public final static int BACK = 0x008;
	public final static int MENU = 0x009;
	public final static int WAIT = 0x00A;
	public final static int WAKEUP = 0x00B;
	public final static int SCREENSHOT = 0x00C;
	public final static int VIEWDUMP = 0x00D;
	public final static int DEVICEINFO = 0x00E;
	public final static int FINISH = 0x00F;
	public static final int SEE = 0x010;
	public static final int WAITTODISAPPEAR = 0x011;

	public static int getActionFromStr(String action) {
		// TODO Auto-generated method stub
		String temp = action.toUpperCase().trim();
		if (temp.equals("CLICK")) {
			return AndroidActionType.CLICK;
		} else if (temp.startsWith("FIND")) {
			return AndroidActionType.FIND;
		} else if (temp.equals("SWIP")) {
			return AndroidActionType.SWIP;
		} else if (temp.equals("SCROLLTO")) {
			return AndroidActionType.SCROLLTO;
		} else if (temp.equals("SETTEXT")) {
			return AndroidActionType.SETTEXT;
		} else if (temp.equals("GETTEXT")) {
			return AndroidActionType.GETTEXT;
		} else if (temp.equals("PRESSKEY")) {
			return AndroidActionType.PRESSKEY;
		} else if (temp.equals("BACK")) {
			return AndroidActionType.BACK;
		} else if (temp.equals("MENU")) {
			return AndroidActionType.MENU;
		} else if (temp.equals("WAIT")) {
			return AndroidActionType.WAIT;
		} else if (temp.equals("WAKEUP")) {
			return AndroidActionType.WAKEUP;
		} else if (temp.equals("SCREENSHOT")) {
			return AndroidActionType.SCREENSHOT;
		} else if (temp.equals("VIEWDUMP")) {
			return AndroidActionType.VIEWDUMP;
		} else if (temp.equals("DEVICEINFO")) {
			return AndroidActionType.DEVICEINFO;
		} else if (temp.equals("FINISH")) {
			return AndroidActionType.FINISH;
		} else if (temp.startsWith("SEE")) {
			return AndroidActionType.SEE;
		} else if (temp.equals("WAITTODISAPPEAR")) {
			return AndroidActionType.WAITTODISAPPEAR;
		}
		return AndroidActionType.FINISH;
	}
}
