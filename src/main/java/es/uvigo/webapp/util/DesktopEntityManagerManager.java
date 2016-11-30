package es.uvigo.webapp.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.DesktopCleanup;

/**
 * Creates a EntityManger per ZK Desktop.
 *
 * A ZK Desktop is created per complete page. That is,
 * when user refreshes the page or opens a new tab, or a new
 * browser session, a new Desktop is created for this new page.
 *
 * Mapping an EntityManager to Desktops is useful to keep
 * an opened EntityManager while the user is interacting with
 * the same page, which facilitates keeping entity objects in
 * session (e.g. a ViewModel) while keeping their persistent state,
 * and thus avoiding, for example, lazy initialization exceptions,
 * the need to make merges, re-finding entities by id, etc.
 */
public class DesktopEntityManagerManager {

	private static final String ENTITY_MANAGER_NAME = "__ENTITY_MANAGER__";
	private static EntityManagerFactory emf = null;
	
	public static EntityManager getDesktopEntityManager() {
		Desktop currentDesktop = Executions.getCurrent().getDesktop();
				
		if (currentDesktop != null) {
			if (currentDesktop.hasAttribute(ENTITY_MANAGER_NAME)) {
				return (EntityManager) currentDesktop.getAttribute(ENTITY_MANAGER_NAME);
			} else {

				EntityManager newEm = createNewEntityMamanger();
				currentDesktop.setAttribute(ENTITY_MANAGER_NAME, newEm);
				currentDesktop.addListener(new DesktopCleanup() {
					
					@Override
					public void cleanup(Desktop arg0) throws Exception {
						newEm.close();
					}
				});
				return newEm;
			}
			
		} else {
			throw new IllegalArgumentException("Desktop not found in this execution");
		}
	}
	
	private static EntityManager createNewEntityMamanger() {
		EntityManagerFactory emf = getOrCreateEntityManagerFactory();
		return emf.createEntityManager();
	}

	private static EntityManagerFactory getOrCreateEntityManagerFactory() {
		if (emf != null) {
			return emf;
		}
		
		final String persistenceUnitName = Executions.getCurrent().getDesktop().getWebApp().getConfiguration()
				.getPreference("persistence-unit-name", "");
		if (persistenceUnitName.equals("")) {
			throw new IllegalArgumentException("no 'persistence-unit-name' preference found in zk.xml");
		} else {
			emf = Persistence.createEntityManagerFactory(persistenceUnitName);
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {				
				emf.close();
			}
		});
		
		return emf;
	}
}
