package com.andypemberton.jboss.portal.theme.renderer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.jboss.portal.Mode;
import org.jboss.portal.WindowState;
import org.jboss.portal.theme.render.renderer.ActionRendererContext;

public final class ModeAndStateComparator implements Comparator {

	public final static ModeAndStateComparator INSTANCE = new ModeAndStateComparator();

	private final Map<Object, Integer> modeOrState2Index = new HashMap<Object, Integer>();

	private int lastModeIndex = 1;

	private int lastStateIndex = 101;

	protected ModeAndStateComparator() {
		modeOrState2Index.put(Mode.EDIT.toString(), new Integer(97));
		modeOrState2Index.put(Mode.HELP.toString(), new Integer(98));
		modeOrState2Index.put(Mode.VIEW.toString(), new Integer(99));
		modeOrState2Index.put(Mode.ADMIN.toString(), new Integer(100));
		modeOrState2Index.put(WindowState.MINIMIZED.toString(), new Integer(198));
		modeOrState2Index.put(WindowState.NORMAL.toString(), new Integer(199));
		modeOrState2Index.put(WindowState.MAXIMIZED.toString(), new Integer(200));
	}

	public int compare(Object o1, Object o2) {
		ActionRendererContext action1 = (ActionRendererContext) o1;
		ActionRendererContext action2 = (ActionRendererContext) o2;

		Object origin1 = action1.getName();
		Object origin2 = action2.getName();

		int index1 = getIndexFor(origin1);
		int index2 = getIndexFor(origin2);

		return index1 - index2;
	}

	private int getIndexFor(Object origin) {
		Integer index = (Integer) modeOrState2Index.get(origin);
		if (index == null) {
			index = (origin instanceof Mode) ? new Integer(lastModeIndex++) : new Integer(lastStateIndex++);
			modeOrState2Index.put(origin, index);
		}
		return index.intValue();
	}
}
