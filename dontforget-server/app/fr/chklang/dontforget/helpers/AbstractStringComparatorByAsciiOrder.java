package fr.chklang.dontforget.helpers;

import java.util.Comparator;

public abstract class AbstractStringComparatorByAsciiOrder<T> implements Comparator<T>{

	public int compare(String o1, String o2) {
		int lLengthUuid1 = o1.length();
		int lLengthUuid2 = o2.length();
		for (int i=0; i<lLengthUuid1 && i<lLengthUuid2; i++) {
			int lCompareResult = Character.compare(o1.charAt(i), o2.charAt(i));
			if (lCompareResult != 0) {
				return lCompareResult;
			}
		}
		if (lLengthUuid1 < lLengthUuid2) {
			return -1;
		} else if (lLengthUuid1 < lLengthUuid2) {
			return 1;
		} else {
			return 0;
		}
	}

	public static int compareStatic(String o1, String o2) {
		int lLengthUuid1 = o1.length();
		int lLengthUuid2 = o2.length();
		for (int i=0; i<lLengthUuid1 && i<lLengthUuid2; i++) {
			int lCompareResult = Character.compare(o1.charAt(i), o2.charAt(i));
			if (lCompareResult != 0) {
				return lCompareResult;
			}
		}
		if (lLengthUuid1 < lLengthUuid2) {
			return -1;
		} else if (lLengthUuid1 < lLengthUuid2) {
			return 1;
		} else {
			return 0;
		}
	}
}
