package com.andypemberton.jboss.portal.theme.renderer.reusable;

import java.io.PrintWriter;

import org.jboss.portal.WindowState;
import org.jboss.portal.theme.ThemeConstants;
import org.jboss.portal.theme.render.AbstractObjectRenderer;
import org.jboss.portal.theme.render.ObjectRendererContext;
import org.jboss.portal.theme.render.RenderException;
import org.jboss.portal.theme.render.RendererContext;
import org.jboss.portal.theme.render.renderer.ActionRendererContext;
import org.jboss.portal.theme.render.renderer.WindowRenderer;
import org.jboss.portal.theme.render.renderer.WindowRendererContext;

import com.andypemberton.jboss.portal.theme.renderer.RendererConstants;

public class ReusableWindowRenderer extends AbstractObjectRenderer implements WindowRenderer {

	/**
	 * Render the portlet window; if <strong>window.hide.empty</strong> property is set, don't display window
	 * (decoration) if portlet has no content.
	 */
	public void render(RendererContext rc, WindowRendererContext wrc) throws RenderException {
		boolean hide = Boolean.valueOf(wrc.getProperty(RendererConstants.WINDOW_CSS_CLASS + "."
				+ RendererConstants.PROP_HIDE + "." + RendererConstants.PROP_EMPTY))
				&& (wrc.getPortlet().getMarkup() == null || "".equals(wrc.getPortlet().getMarkup()));
		if (hide) {
			// hide portlet if no content and minimized if window.hide.empty.minimized property is set
			hide = Boolean.valueOf(wrc.getProperty(RendererConstants.WINDOW_CSS_CLASS + "."
					+ RendererConstants.PROP_HIDE + "." + RendererConstants.PROP_EMPTY + "."
					+ WindowState.MINIMIZED.toString()))
					&& (wrc.getWindowState().equals(WindowState.MINIMIZED));
		}

		if (!hide) {
			PrintWriter markup = rc.getWriter();
			// if no specific renderer for window, use parent page renderset as window classname
			String renderSet = wrc.getProperty(ThemeConstants.PORTAL_PROP_WINDOW_RENDERER);
			if (renderSet == null) {
				renderSet = wrc.getProperty(ThemeConstants.PORTAL_PROP_RENDERSET);
			}
			markup.print(String.format("<div class=\"%s %s %s-%s %s-%s\">", RendererConstants.WINDOW_CSS_CLASS,
					renderSet, ActionRendererContext.MODES_KEY, wrc.getMode().toString(),
					ActionRendererContext.WINDOWSTATES_KEY, wrc.getWindowState().toString()));
			rc.render(wrc.getDecoration());
			rc.render(wrc.getPortlet());
			markup.print("</div>");
			markup.print(String.format("<a name=\"%s-%s\"></a>", RendererConstants.SKIPLINK_CSS_CLASS, wrc.getId()));
		}
	}

	@Override
	public void startContext(RendererContext rc, ObjectRendererContext orc) {
		rc.setAttribute(RendererConstants.ATTR_WINDOW_ID, ((WindowRendererContext) orc).getId());
	}

}
