package com.andypemberton.jboss.portal.theme.renderer.reusable;

import java.io.PrintWriter;

import org.jboss.portal.theme.render.AbstractObjectRenderer;
import org.jboss.portal.theme.render.RenderException;
import org.jboss.portal.theme.render.RendererContext;
import org.jboss.portal.theme.render.renderer.PortletRenderer;
import org.jboss.portal.theme.render.renderer.PortletRendererContext;

import com.andypemberton.jboss.portal.theme.renderer.RendererConstants;

public class ReusablePortletRenderer extends AbstractObjectRenderer implements PortletRenderer {

	/**
	 * Render the portlet content; called from WindowRenderer.render
	 * 
	 * @see ReusableWindowRenderer
	 */
	public void render(RendererContext rc, PortletRendererContext prc) throws RenderException {
		PrintWriter markup = rc.getWriter();
		markup.print(String.format("<div class=\"%s\">%s</div>", RendererConstants.PORTLET_CSS_CLASS, prc.getMarkup()));
	}

}
