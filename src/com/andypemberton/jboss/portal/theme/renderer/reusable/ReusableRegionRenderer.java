package com.andypemberton.jboss.portal.theme.renderer.reusable;

import java.io.PrintWriter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.jboss.portal.theme.render.AbstractObjectRenderer;
import org.jboss.portal.theme.render.ObjectRendererContext;
import org.jboss.portal.theme.render.RenderException;
import org.jboss.portal.theme.render.RendererContext;
import org.jboss.portal.theme.render.renderer.RegionRenderer;
import org.jboss.portal.theme.render.renderer.RegionRendererContext;
import org.jboss.portal.theme.render.renderer.WindowRendererContext;

import com.andypemberton.jboss.portal.theme.renderer.RendererConstants;

public class ReusableRegionRenderer extends AbstractObjectRenderer implements RegionRenderer {

	private static final Logger log = Logger.getLogger(ReusableRegionRenderer.class.getName());

	/**
	 * Render the region, including windows within the region; if <strong>region.hide.empty</strong> property is set,
	 * don't display region if it has no windows.
	 */
	public void renderBody(RendererContext rc, RegionRendererContext rrc) throws RenderException {
		boolean hide = Boolean.valueOf(rrc.getProperty(RendererConstants.REGION_CSS_CLASS + "."
				+ RendererConstants.PROP_HIDE + "." + RendererConstants.PROP_EMPTY))
				&& (rrc.getWindows() == null || rrc.getWindows().size() == 0);
		if (!hide) {
			PrintWriter markup = rc.getWriter();
			markup.print(String.format("<div id=\"%s\" class=\"%s\">", (rrc.getCSSId() == null ? "" : rrc.getCSSId()),
					RendererConstants.REGION_CSS_CLASS));
			for (Object wrc : rrc.getWindows()) {
				rc.render((WindowRendererContext) wrc);
			}
			markup.print("</div>");
		}
	}

	public void renderFooter(RendererContext rc, RegionRendererContext rrc) throws RenderException {
	}

	public void renderHeader(RendererContext rc, RegionRendererContext rrc) throws RenderException {
	}

	/**
	 * Load and store the i18n properties for this region
	 */
	@Override
	public void startContext(RendererContext rc, ObjectRendererContext orc) {
		this.loadMessageBundle(rc);
	}

	/**
	 * Attempt to load custom i18n properties, otherwise load default.
	 */
	// TODO investigate a higher-level place to load 18n - same for all portlets
	public void loadMessageBundle(RendererContext rc) {
		if (rc.getAttribute(RendererConstants.DEFAULT_MESSAGES_NAME) == null) {
			Locale locale = rc.getDispatcher().getRequest().getLocale();
			ResourceBundle bundle = null;
			String customBundleBaseName = rc.getProperty(RendererConstants.PROP_MESSAGES_BASENAME);
			try {
				if (customBundleBaseName != null) {
					bundle = ResourceBundle.getBundle(customBundleBaseName, locale);
				}
			} catch (MissingResourceException mre) {
				log.warning("No bundle for property: " + customBundleBaseName);
			} finally {
				if (bundle == null) {
					bundle = ResourceBundle.getBundle(this.getClass().getPackage().getName().toString() + ".i18n."
							+ RendererConstants.DEFAULT_MESSAGES_NAME, locale);
				}
			}
			rc.setAttribute(RendererConstants.DEFAULT_MESSAGES_NAME, bundle);
		}
	}
}
