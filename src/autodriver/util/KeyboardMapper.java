package autodriver.util;

import java.util.ArrayList;

import android.os.SystemClock;
import android.view.KeyEvent;

public class KeyboardMapper {

	public static int getPrimaryKeyCode(char character) {

		int keyCode = 0;

		switch (character) {

		case 'a':
			keyCode = KeyEvent.KEYCODE_A;
			break;

		case 'b':
			keyCode = KeyEvent.KEYCODE_B;
			break;

		case 'c':
			keyCode = KeyEvent.KEYCODE_C;
			break;

		case 'd':
			keyCode = KeyEvent.KEYCODE_D;
			break;

		case 'e':
			keyCode = KeyEvent.KEYCODE_E;
			break;

		case 'f':
			keyCode = KeyEvent.KEYCODE_F;
			break;

		case 'g':
			keyCode = KeyEvent.KEYCODE_G;
			break;

		case 'h':
			keyCode = KeyEvent.KEYCODE_H;
			break;

		case 'i':
			keyCode = KeyEvent.KEYCODE_I;
			break;

		case 'j':
			keyCode = KeyEvent.KEYCODE_J;
			break;

		case 'k':
			keyCode = KeyEvent.KEYCODE_K;
			break;

		case 'l':
			keyCode = KeyEvent.KEYCODE_L;
			break;

		case 'm':
			keyCode = KeyEvent.KEYCODE_M;
			break;

		case 'n':
			keyCode = KeyEvent.KEYCODE_N;
			break;

		case 'o':
			keyCode = KeyEvent.KEYCODE_O;
			break;

		case 'p':
			keyCode = KeyEvent.KEYCODE_P;
			break;

		case 'q':
			keyCode = KeyEvent.KEYCODE_Q;
			break;

		case 'r':
			keyCode = KeyEvent.KEYCODE_R;
			break;

		case 's':
			keyCode = KeyEvent.KEYCODE_S;
			break;

		case 't':
			keyCode = KeyEvent.KEYCODE_T;
			break;

		case 'u':
			keyCode = KeyEvent.KEYCODE_U;
			break;

		case 'v':
			keyCode = KeyEvent.KEYCODE_V;
			break;

		case 'w':
			keyCode = KeyEvent.KEYCODE_W;
			break;

		case 'x':
			keyCode = KeyEvent.KEYCODE_X;
			break;

		case 'y':
			keyCode = KeyEvent.KEYCODE_Y;
			break;

		case 'z':
			keyCode = KeyEvent.KEYCODE_Z;
			break;

		case '0':
			keyCode = KeyEvent.KEYCODE_0;
			break;

		case '1':
			keyCode = KeyEvent.KEYCODE_1;
			break;

		case '2':
			keyCode = KeyEvent.KEYCODE_2;
			break;

		case '3':
			keyCode = KeyEvent.KEYCODE_3;
			break;

		case '4':
			keyCode = KeyEvent.KEYCODE_4;
			break;

		case '5':
			keyCode = KeyEvent.KEYCODE_5;
			break;

		case '6':
			keyCode = KeyEvent.KEYCODE_6;
			break;

		case '7':
			keyCode = KeyEvent.KEYCODE_7;
			break;

		case '8':
			keyCode = KeyEvent.KEYCODE_8;
			break;

		case '9':
			keyCode = KeyEvent.KEYCODE_9;
			break;

		case '`':
			keyCode = KeyEvent.KEYCODE_GRAVE;
			break;

		case '-':
			keyCode = KeyEvent.KEYCODE_MINUS;
			break;

		case '=':
			keyCode = KeyEvent.KEYCODE_EQUALS;
			break;

		case '[':
			keyCode = KeyEvent.KEYCODE_LEFT_BRACKET;
			break;

		case ']':
			keyCode = KeyEvent.KEYCODE_RIGHT_BRACKET;
			break;

		case '\\':
			keyCode = KeyEvent.KEYCODE_BACKSLASH;
			break;

		case ';':
			keyCode = KeyEvent.KEYCODE_SEMICOLON;
			break;

		case '\'':
			keyCode = KeyEvent.KEYCODE_APOSTROPHE;
			break;

		case ',':
			keyCode = KeyEvent.KEYCODE_COMMA;
			break;

		case '.':
			keyCode = KeyEvent.KEYCODE_PERIOD;
			break;

		case '/':
			keyCode = KeyEvent.KEYCODE_SLASH;
			break;

		// require modifier KEYCODE_SHIFT_LEFT 59
		case 'A':
			keyCode = KeyEvent.KEYCODE_A;
			break;

		case 'B':
			keyCode = KeyEvent.KEYCODE_B;
			break;

		case 'C':
			keyCode = KeyEvent.KEYCODE_C;
			break;

		case 'D':
			keyCode = KeyEvent.KEYCODE_D;
			break;

		case 'E':
			keyCode = KeyEvent.KEYCODE_E;
			break;

		case 'F':
			keyCode = KeyEvent.KEYCODE_F;
			break;

		case 'G':
			keyCode = KeyEvent.KEYCODE_G;
			break;

		case 'H':
			keyCode = KeyEvent.KEYCODE_H;
			break;

		case 'I':
			keyCode = KeyEvent.KEYCODE_I;
			break;

		case 'J':
			keyCode = KeyEvent.KEYCODE_J;
			break;

		case 'K':
			keyCode = KeyEvent.KEYCODE_K;
			break;

		case 'L':
			keyCode = KeyEvent.KEYCODE_L;
			break;

		case 'M':
			keyCode = KeyEvent.KEYCODE_M;
			break;

		case 'N':
			keyCode = KeyEvent.KEYCODE_N;
			break;

		case 'O':
			keyCode = KeyEvent.KEYCODE_O;
			break;

		case 'P':
			keyCode = KeyEvent.KEYCODE_P;
			break;

		case 'Q':
			keyCode = KeyEvent.KEYCODE_Q;
			break;

		case 'R':
			keyCode = KeyEvent.KEYCODE_R;
			break;

		case 'S':
			keyCode = KeyEvent.KEYCODE_S;
			break;

		case 'T':
			keyCode = KeyEvent.KEYCODE_T;
			break;

		case 'U':
			keyCode = KeyEvent.KEYCODE_U;
			break;

		case 'V':
			keyCode = KeyEvent.KEYCODE_V;
			break;

		case 'W':
			keyCode = KeyEvent.KEYCODE_W;
			break;

		case 'X':
			keyCode = KeyEvent.KEYCODE_X;
			break;

		case 'Y':
			keyCode = KeyEvent.KEYCODE_Y;
			break;

		case 'Z':
			keyCode = KeyEvent.KEYCODE_Z;
			break;

		case ')':
			keyCode = KeyEvent.KEYCODE_0;
			break;

		case '!':
			keyCode = KeyEvent.KEYCODE_1;
			break;

		case '@':
			keyCode = KeyEvent.KEYCODE_2;
			break;

		case '#':
			keyCode = KeyEvent.KEYCODE_3;
			break;

		case '$':
			keyCode = KeyEvent.KEYCODE_4;
			break;

		case '%':
			keyCode = KeyEvent.KEYCODE_5;
			break;

		case '^':
			keyCode = KeyEvent.KEYCODE_6;
			break;

		case '&':
			keyCode = KeyEvent.KEYCODE_7;
			break;

		case '*':
			keyCode = KeyEvent.KEYCODE_8;
			break;

		case '(':
			keyCode = KeyEvent.KEYCODE_9;
			break;

		case '~':
			keyCode = KeyEvent.KEYCODE_GRAVE;
			break;

		case '_':
			keyCode = KeyEvent.KEYCODE_MINUS;
			break;

		case '+':
			keyCode = KeyEvent.KEYCODE_EQUALS;
			break;

		case '{':
			keyCode = KeyEvent.KEYCODE_LEFT_BRACKET;
			break;

		case '}':
			keyCode = KeyEvent.KEYCODE_RIGHT_BRACKET;
			break;

		case '|':
			keyCode = KeyEvent.KEYCODE_BACKSLASH;
			break;

		case ':':
			keyCode = KeyEvent.KEYCODE_SEMICOLON;
			break;

		case '"':
			keyCode = KeyEvent.KEYCODE_APOSTROPHE;
			break;

		case '<':
			keyCode = KeyEvent.KEYCODE_COMMA;
			break;

		case '>':
			keyCode = KeyEvent.KEYCODE_PERIOD;
			break;

		case '?':
			keyCode = KeyEvent.KEYCODE_SLASH;
			break;

		// no modifier
		case '\t':
			keyCode = KeyEvent.KEYCODE_TAB;
			break;

		case '\n':
			keyCode = KeyEvent.KEYCODE_ENTER;
			break;

		default:
			break;
		}

		return keyCode;

	}

	public static int getModifierKeyCode(char character) {

		int keyCode = 0;

		switch (character) {

		case 'a':
		case 'b':
		case 'c':
		case 'd':
		case 'e':
		case 'f':
		case 'g':
		case 'h':
		case 'i':
		case 'j':
		case 'k':
		case 'l':
		case 'm':
		case 'n':
		case 'o':
		case 'p':
		case 'q':
		case 'r':
		case 's':
		case 't':
		case 'u':
		case 'v':
		case 'w':
		case 'x':
		case 'y':
		case 'z':
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
		case '`':
		case '-':
		case '=':
		case '[':
		case ']':
		case '\\':
		case ';':
		case '\'':
		case ',':
		case '.':
		case '/':
			keyCode = 0;
			break;

		// require modifier KEYCODE_SHIFT_LEFT 59
		case 'A':
		case 'B':
		case 'C':
		case 'D':
		case 'E':
		case 'F':
		case 'G':
		case 'H':
		case 'I':
		case 'J':
		case 'K':
		case 'L':
		case 'M':
		case 'N':
		case 'O':
		case 'P':
		case 'Q':
		case 'R':
		case 'S':
		case 'T':
		case 'U':
		case 'V':
		case 'W':
		case 'X':
		case 'Y':
		case 'Z':
		case ')':
		case '!':
		case '@':
		case '#':
		case '$':
		case '%':
		case '^':
		case '&':
		case '*':
		case '(':
		case '~':
		case '_':
		case '+':
		case '{':
		case '}':
		case '|':
		case ':':
		case '"':
		case '<':
		case '>':
		case '?':
			keyCode = KeyEvent.META_SHIFT_ON;
			break;

		// no modifier
		case '\t':
		case '\n':
			keyCode = 0;
			break;
		default:
			break;
		}

		return keyCode;
	}

	public static ArrayList<KeyEvent> getKeyEvents(char character) {

		ArrayList<KeyEvent> events = new ArrayList<KeyEvent>();

		int modifier = KeyboardMapper.getModifierKeyCode(character);
		int primary = KeyboardMapper.getPrimaryKeyCode(character);

		if (primary > 0) {
			// primary down key event
			long downtime = SystemClock.uptimeMillis();
			KeyEvent event = new KeyEvent(downtime, SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, primary, 0, modifier, 0, 0);
			events.add(event);
			// primary up key event
			KeyEvent event2 = new KeyEvent(downtime, SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, primary, 0, modifier, 0, 0);
			events.add(event2);
		}

		return events;
	}

	public static ArrayList<KeyEvent> getKeyEvents(String string) {

		ArrayList<KeyEvent> events = new ArrayList<KeyEvent>();

		for (int i = 0; i < string.length(); ++i) {
			ArrayList<KeyEvent> theseEvents = KeyboardMapper.getKeyEvents(string.charAt(i));
			events.addAll(theseEvents);
		}

		return events;
	}

}