package com.andypemberton.jboss.portal.theme.renderer.reusable;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.jboss.portal.theme.render.AbstractObjectRenderer;
import org.jboss.portal.theme.render.RenderException;
import org.jboss.portal.theme.render.RendererContext;
import org.jboss.portal.theme.render.renderer.ActionRendererContext;
import org.jboss.portal.theme.render.renderer.DecorationRenderer;
import org.jboss.portal.theme.render.renderer.DecorationRendererContext;

import com.andypemberton.jboss.portal.theme.renderer.ModeAndStateComparator;
import com.andypemberton.jboss.portal.theme.renderer.RendererConstants;

public class ReusableDecorationRenderer extends AbstractObjectRenderer implements DecorationRenderer {

	/**
	 * Render the current window's decoration, delegating to renderTitle and renderControls. Called from
	 * WindowRenderer.render.
	 * 
	 * @see ReusableWindowRenderer
	 */
	public void render(RendererContext rc, DecorationRendererContext drc) throws RenderException {
		boolean hide = Boolean.valueOf(drc.getProperty(RendererConstants.DECORATION_CSS_CLASS + "."
				+ RendererConstants.PROP_HIDE));
		if (!hide) {
			PrintWriter markup = rc.getWriter();
			markup.print(String.format("<div class=\"%s\">", RendererConstants.DECORATION_CSS_CLASS));
			renderTitle(markup, rc, drc);
			renderControls(markup, rc, drc);
			markup.print("</div>");
		}
	}

	/**
	 * Render the current window's title in an HTML h2 element; if <strong>decoration.hide.title</strong> property is
	 * set to true, do not display.
	 * 
	 * @param markup
	 * @param drc
	 */
	private static void renderTitle(PrintWriter markup, RendererContext rc, DecorationRendererContext drc) {
		boolean hide = Boolean.valueOf(drc.getProperty(RendererConstants.DECORATION_CSS_CLASS + "."
				+ RendererConstants.PROP_HIDE + "." + RendererConstants.TITLE_CSS_CLASS));
		if (!hide) {
			markup.print(String.format("<h2 class=\"%s\">%s</h2>", RendererConstants.TITLE_CSS_CLASS, drc.getTitle()));
		}
	}

	/**
	 * Render the current window's controls (available modes/windowstates) as an unordered list of anchor links; if
	 * <strong>decoration.hide.controls</strong> property is set to true, do not display.
	 * 
	 * @param markup
	 * @param drc
	 */
	private static void renderControls(PrintWriter markup, RendererContext rc, DecorationRendererContext drc) {
		boolean hide = Boolean.valueOf(drc.getProperty(RendererConstants.DECORATION_CSS_CLASS + "."
				+ RendererConstants.PROP_HIDE + "." + RendererConstants.CONTROLS_CSS_CLASS));
		if (!hide) {
			markup.print(String.format("<ul class=\"%s\">", RendererConstants.CONTROLS_CSS_CLASS));
			renderSkiplink(markup, rc, drc);
			renderActions(ActionRendererContext.MODES_KEY, markup, rc, drc);
			renderActions(ActionRendererContext.WINDOWSTATES_KEY, markup, rc, drc);
			markup.print("</ul>");
		}
	}

	/**
	 * Render the given set of mode actions or windowstate actions; do not display if configurable hide property is set:
	 * e.g.: for property <strong>decoration.hide.mode.edit</strong> - don't display the edit mode link
	 * 
	 * @param actions
	 * @param markup
	 * @param drc
	 */
	private static void renderActions(String actionType, PrintWriter markup, RendererContext rc,
			DecorationRendererContext drc) {
		ResourceBundle bundle = (ResourceBundle) rc.getAttribute(RendererConstants.DEFAULT_MESSAGES_NAME);
		Collection<ActionRendererContext> actions = drc.getTriggerableActions(actionType);

		if (actions instanceof List) {
			List<ActionRendererContext> list = (List<ActionRendererContext>) actions;
			Collections.sort(list, ModeAndStateComparator.INSTANCE);
			actions = list;
		}

		for (ActionRendererContext action : actions) {
			if (action.isEnabled()) {
				boolean hide = Boolean.valueOf(drc.getProperty(RendererConstants.DECORATION_CSS_CLASS + "."
						+ RendererConstants.PROP_HIDE + "." + actionType + "." + action.getName()));
				if (!hide) {
					String linkText = bundle.getString("renderer.portlet." + actionType + "." + action.getName())
							.replaceAll("\\{0\\}", drc.getTitle());
					markup.print(String.format("<li class=\"%s %s\">", actionType, action.getName()));
					markup.print(String.format("<a href=\"%s\">%s</a>", action.getURL(), linkText));
					markup.print("</li>");
				}
			}
		}

	}

	/**
	 * Render a skiplink for the portlet, skipping over its controls and content.
	 * 
	 * @param markup
	 * @param rc
	 * @param drc
	 */
	private static void renderSkiplink(PrintWriter markup, RendererContext rc, DecorationRendererContext drc) {
		boolean hide = Boolean.valueOf(drc.getProperty(RendererConstants.DECORATION_CSS_CLASS + "."
				+ RendererConstants.PROP_HIDE + "." + RendererConstants.SKIPLINK_CSS_CLASS));
		if (!hide) {
			ResourceBundle bundle = (ResourceBundle) rc.getAttribute(RendererConstants.DEFAULT_MESSAGES_NAME);
			String linkText = bundle.getString("renderer.portlet.skip").replaceAll("\\{0\\}", drc.getTitle());
			markup.print(String.format("<li class=\"%s\">", RendererConstants.SKIPLINK_CSS_CLASS));
			markup.print(String.format("<a href=\"#%s-%s\">%s</a>", RendererConstants.SKIPLINK_CSS_CLASS, (String) rc
					.getAttribute(RendererConstants.ATTR_WINDOW_ID), linkText));
			markup.print("</li>");
		}
	}
}
