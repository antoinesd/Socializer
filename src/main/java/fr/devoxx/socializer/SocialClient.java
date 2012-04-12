/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.devoxx.socializer;

import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.social.MultiServicesManager;
import org.jboss.seam.social.SocialNetworkServicesHub;
import org.jboss.seam.social.event.SocialEvent;
import org.jboss.seam.social.event.StatusUpdated;
import org.jboss.seam.social.oauth.OAuthService;
import org.jboss.seam.social.oauth.OAuthSession;
import org.jboss.seam.social.oauth.OAuthToken;
import org.jboss.seam.social.twitter.model.Tweet;
import org.jboss.solder.logging.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Named
@SessionScoped
public class SocialClient implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3723552335163650582L;

    private String Status;

    private String selectedService;

    private static final String DEFAULT_THEME = "aristo";
    private String currentTheme = DEFAULT_THEME;


    @Inject
    private Logger log;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }

    @Inject
    private MultiServicesManager manager;

    public MultiServicesManager getManager() {
        return manager;
    }

    public void setManager(MultiServicesManager manager) {
        this.manager = manager;
    }

    public OAuthSession getCurrentSession() {
        return manager.getCurrentSession();
    }

    public void setCurrentSession(OAuthSession currentSession) {
        manager.setCurrentSession(currentSession);
    }

    public Map<String, OAuthSession> getSessionsMap() {
        return Maps.uniqueIndex(getSessions(), new Function<OAuthSession, String>() {

            @Override
            public String apply(OAuthSession arg0) {

                return arg0.toString();
            }

        });
    }

    @Produces
    @Named
    public OAuthService getCurrentService() {
        return manager.getCurrentService();
    }

    @Produces
    @Named
    public SocialNetworkServicesHub getCurrentHub() {
        return manager.getCurrentServiceHub();
    }

    public List<OAuthSession> getSessions() {
        return newArrayList(manager.getActiveSessions());
    }

    public OAuthToken getAccessToken() {
        return getCurrentSession().getAccessToken();
    }

    public void connectCurrentService() {
        manager.connectCurrentService();
    }

    public String getCurrentSessionName() {
        return manager.getCurrentSession() == null ? "" : manager.getCurrentSession().toString();
    }

    public void setCurrentSessionName(String cursrvHdlStr) {
        setCurrentSession(getSessionsMap().get(cursrvHdlStr));
    }

    public void redirectToAuthorizationURL(String url) throws IOException {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        externalContext.redirect(url);
    }

    public String getTimeLineUrl() {
        if (getCurrentSession() != null && getCurrentSession().isConnected())
            return "/WEB-INF/fragments/" + getManager().getCurrentService().getType().toLowerCase() + ".xhtml";
        return "";
    }

    public void serviceInit() throws IOException {

        redirectToAuthorizationURL(manager.initNewSession(selectedService));

    }

    protected void statusUpdateObserver(@Observes @Any StatusUpdated statusUpdate) {
        if (statusUpdate.getStatus().equals(SocialEvent.Status.SUCCESS)) {
            log.debugf("Status update with : %s ", statusUpdate.getMessage());
            setStatus("");
        }
    }

    public void resetConnection() {
        manager.destroyCurrentSession();
    }

    /**
     * @return the selectedService
     */
    public String getSelectedService() {
        return selectedService;
    }

    /**
     * @param selectedService the selectedService to set
     */
    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }
    
    /**
     * A list of fake tweets in order to improve design
     * without consuming rate limit.
     * @return
     */
    public List<Tweet> getDebugTweets()
    {
    	List<Tweet> result = new ArrayList<Tweet>();
    	String text = "Here is a fake tweet like all other";
    	Date createdAt = new Date();
    	String fromUser = "paul_dijou";
    	String profileImageUrl = "http://a1.twimg.com/profile_images/1380912173/Screen_shot_2011-06-03_at_7.35.36_PM_normal.png";
    	Long toUserId = 1L;
    	Long fromUserId = 2L;
    	String languageCode = "en";
    	String source = "Web";
    	
    	for(int i = 0; i < 30; ++i)
    	{
    		long id = 10000L + i;
    		Tweet fakeTweet = new Tweet(id, text, createdAt, fromUser, profileImageUrl, toUserId, fromUserId, languageCode, source);
    		result.add(fakeTweet);
    	}
    	
    	return result;
    }

}
